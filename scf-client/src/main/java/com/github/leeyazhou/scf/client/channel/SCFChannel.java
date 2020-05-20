package com.github.leeyazhou.scf.client.channel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.NotYetConnectedException;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.leeyazhou.scf.client.entity.AutoResetEvent;
import com.github.leeyazhou.scf.client.entity.SocketPoolProfile;
import com.github.leeyazhou.scf.core.entity.ByteConverter;
import com.github.leeyazhou.scf.core.exception.DataOverFlowException;
import com.github.leeyazhou.scf.core.exception.ProtocolException;
import com.github.leeyazhou.scf.core.exception.TimeoutException;
import com.github.leeyazhou.scf.core.utils.SystemUtil;
import com.github.leeyazhou.scf.protocol.ProtocolConst;
import com.github.leeyazhou.scf.protocol.sfp.SFPStruct;

/**
 * CSocket
 *
 */
public class SCFChannel {

  private static final Logger logger = LoggerFactory.getLogger(SCFChannel.class);
  private Socket socket;
  private byte[] DESKey;
  /** DES密钥 */
  private boolean rights;
  /** 是否启用认证 */
  private ChannelFactory pool;
  private SocketChannel channel;
  private boolean _inPool = false;
  private boolean _connecting = false;
  private boolean waitDestroy = false;
  private SocketPoolProfile socketConfig;
  private DataReceiver dataReceiver = null;
  private ByteBuffer receiveBuffer, sendBuffer;
  private final Object sendLockHelper = new Object();
  private final Object receiveLockHelper = new Object();
  private CByteArrayOutputStream receiveData = new CByteArrayOutputStream();
  private ConcurrentHashMap<Integer, WindowData> WaitWindows = new ConcurrentHashMap<Integer, WindowData>();

  private ExecutorService executorService = Executors.newFixedThreadPool(SystemUtil.getHalfCpuProcessorCount());
  private static NIOHandler handler = null;

  protected SCFChannel(InetSocketAddress endpoint, ChannelFactory _pool, SocketPoolProfile config) throws Exception {
    this.socketConfig = config;
    this.pool = _pool;

    channel = SocketChannel.open();
    channel.configureBlocking(false);
    channel.socket().setSendBufferSize(config.getSendBufferSize());
    channel.socket().setReceiveBufferSize(config.getRecvBufferSize());
    receiveBuffer = ByteBuffer.allocate(config.getBufferSize());
    sendBuffer = ByteBuffer.allocate(config.getMaxPakageSize());
    channel.connect(endpoint);

    long begin = System.currentTimeMillis();
    while (true) {
      if ((System.currentTimeMillis() - begin) > 2000) {
        channel.close();
        throw new Exception("connect to " + endpoint + " timeout：2000ms");
      }
      channel.finishConnect();
      if (channel.isConnected()) {
        break;
      } else {
        try {
          Thread.sleep(50);
        } catch (InterruptedException e) {
          logger.error("", e);
        }
      }
    }

    socket = channel.socket();
    _connecting = true;
    dataReceiver = DataReceiver.instance();
    dataReceiver.RegSocketChannel(this);

    handler = NIOHandler.getInstance();
    handler.start();

    logger.info("MaxPakageSize:" + config.getMaxPakageSize());
    logger.info("SendBufferSize:" + config.getSendBufferSize());
    logger.info("RecvBufferSize:" + config.getRecvBufferSize());
    logger.info("create a new connection :" + this.toString());
  }

  protected SCFChannel(String addr, int port, ChannelFactory _pool, SocketPoolProfile config) throws Exception {
    this.socketConfig = config;
    this.pool = _pool;

    channel = SocketChannel.open();
    channel.configureBlocking(false);
    channel.socket().setSendBufferSize(config.getSendBufferSize());
    channel.socket().setReceiveBufferSize(config.getRecvBufferSize());
    receiveBuffer = ByteBuffer.allocate(config.getBufferSize());
    sendBuffer = ByteBuffer.allocate(config.getMaxPakageSize());
    channel.connect(new InetSocketAddress(addr, port));

    long begin = System.currentTimeMillis();
    while (true) {
      if ((System.currentTimeMillis() - begin) > 2000) {
        channel.close();
        throw new Exception("connect to " + addr + ":" + port + " timeout：2000ms");
      }
      channel.finishConnect();
      if (channel.isConnected()) {
        break;
      } else {
        try {
          Thread.sleep(50);
        } catch (InterruptedException e) {
          logger.error("", e);
        }
      }
    }

    socket = channel.socket();
    _connecting = true;
    dataReceiver = DataReceiver.instance();
    dataReceiver.RegSocketChannel(this);

    handler = NIOHandler.getInstance();
    handler.start();

    logger.info("MaxPakageSize:" + config.getMaxPakageSize());
    logger.info("SendBufferSize:" + config.getSendBufferSize());
    logger.info("RecvBufferSize:" + config.getRecvBufferSize());
    logger.info("create a new connection :" + this.toString());
  }

  /**
   * send message
   * 
   * @param data
   * @return
   * @throws IOException
   * @throws Throwable
   */
  public int send(byte[] data) throws IOException, Throwable {
    try {
      synchronized (sendLockHelper) {
        int pakageSize = data.length + ProtocolConst.P_START_TAG.length + ProtocolConst.P_END_TAG.length;
        if (sendBuffer.capacity() < pakageSize) {
          throw new DataOverFlowException("数据包(size:" + pakageSize + ")超过最大限制,请修改或增加配置文件中的<SocketPool maxPakageSize=\""
              + socketConfig.getMaxPakageSize() + "\"/>节点属性！");
        }

        int count = 0;
        sendBuffer.clear();
        sendBuffer.put(ProtocolConst.P_START_TAG);
        sendBuffer.put(data);
        sendBuffer.put(ProtocolConst.P_END_TAG);
        sendBuffer.flip();

        int retryCount = 0;
        while (sendBuffer.hasRemaining()) {
          count += channel.write(sendBuffer);

          if (retryCount++ > 30) {
            throw new Exception("retry write count(" + retryCount + ") above 30");
          }
        }
        return count;
      }
    } catch (IOException ex) {
      _connecting = false;
      throw ex;
    } catch (NotYetConnectedException ex) {
      _connecting = false;
      throw ex;
    }
  }

  /**
   * receive message
   * 
   * @param sessionId
   * @param queueLen
   * @return
   * @throws IOException
   * @throws TimeoutException
   * @throws Exception
   */
  public byte[] receive(int sessionId, int queueLen) throws IOException, TimeoutException, Exception {
    WindowData wd = WaitWindows.get(sessionId);
    if (wd == null) {
      throw new Exception("Need invoke 'registerRec' method before invoke 'receive' method!");
    }
    AutoResetEvent event = wd.getEvent();
    int timeout = getReadTimeout(socketConfig.getReceiveTimeout(), queueLen);
    if (!event.waitOne(timeout)) {
      throw new TimeoutException("ServiceName:[" + this.getServiceName() + "],ServiceIP:[" + this.getServiceIP()
          + "],Receive data timeout or error!timeout:" + timeout + "ms,queue length:" + queueLen);
    }
    byte[] data = wd.getData();
    int offset = SFPStruct.Version;
    int len = ByteConverter.bytesToIntLittleEndian(data, offset);
    if (len != data.length) {
      throw new ProtocolException("The data length inconsistent!datalen:" + data.length + ",check len:" + len);
    }
    return data;
  }

  private volatile int index = 0;
  private volatile boolean handling = false;

  /**
   * get receive message byte
   * 
   * @throws Exception
   */
  protected void frameHandle() throws Exception {
    if (handling) {
      return;
    }
    synchronized (receiveLockHelper) {
      handling = true;
      try {
        if (waitDestroy && isIdle()) {
          logger.info("Shrinking the connection:" + this.toString());
          dispose(true);
          return;
        }
        receiveBuffer.clear();
        try {
          int re = channel.read(receiveBuffer);
          if (re < 0) {
            this.closeAndDisponse();
            logger.error("server is close.this socket will close.");
            return;
          }
        } catch (IOException ex) {
          _connecting = false;
          throw ex;
        } catch (NotYetConnectedException e) {
          _connecting = false;
          throw e;
        }
        receiveBuffer.flip();
        if (receiveBuffer.remaining() == 0) {
          return;
        }

        while (receiveBuffer.remaining() > 0) {
          byte b = receiveBuffer.get();
          receiveData.write(b);
          if (b == ProtocolConst.P_END_TAG[index]) {
            index++;
            if (index == ProtocolConst.P_END_TAG.length) {
              byte[] pak = receiveData.toByteArray(ProtocolConst.P_START_TAG.length,
                  receiveData.size() - ProtocolConst.P_END_TAG.length - ProtocolConst.P_START_TAG.length);
              int pSessionId = ByteConverter.bytesToIntLittleEndian(pak, SFPStruct.Version + SFPStruct.TotalLen);
              WindowData wd = WaitWindows.get(pSessionId);
              if (wd != null) {
                if (wd.getFlag() == 0) {
                  wd.setData(pak);
                  wd.getEvent().set();
                } else if (wd.getFlag() == 1) {
                  /** 异步 */
                  wd.getReceiveHandler().notify(pak, executorService);
                  TimeOut timeOut = new TimeOut(pSessionId, wd, this);
                  handler.offerTimeOut(timeOut);
                  unregisterRec(pSessionId);
                }
              }
              index = 0;
              receiveData.reset();
              continue;
            }
          } else if (index != 0) {
            if (b == ProtocolConst.P_END_TAG[0]) {
              index = 1;
            } else {
              index = 0;
            }
          }
        }
      } catch (Exception ex) {
        index = 0;
        throw ex;
      } finally {
        handling = false;
      }
    }
  }

  public void registerRec(int sessionId) {
    AutoResetEvent event = new AutoResetEvent();
    WindowData wd = new WindowData(event);
    WaitWindows.put(sessionId, wd);
  }

  public void registerRec(int sessionId, WindowData wd) {
    WaitWindows.put(sessionId, wd);
  }

  public void unregisterRec(int sessionId) {
    WaitWindows.remove(sessionId);
  }

  public void closeAndDisponse() {
    this.close();
    dispose(true);
  }

  public void close() {
    pool.release(this);
  }

  public void dispose() throws Exception {
    dispose(false);
  }

  public void dispose(boolean flag) {
    if (flag) {
      logger.warn("destory a connection");
      try {
        dataReceiver.UnRegSocketChannel(this);
      } finally {
        pool.destroy(this);
      }
    } else {
      close();
    }
  }

  protected void disconnect() throws IOException {
    if (channel != null) {
      channel.close();
    }
    if (socket != null) {
      socket.close();
    }
    _connecting = false;
  }

  public void offerAsyncWrite(WindowData wd) {
    handler.offerWriteData(wd);
  }

  public int getTimeOut() {
    return getReadTimeout(socketConfig.getReceiveTimeout(), 1);
  }

  private int getReadTimeout(int baseReadTimeout, int queueLen) {
    if (!socketConfig.isProtected()) {
      return baseReadTimeout;
    }
    if (queueLen <= 0) {
      queueLen = 1;
    }
    int result = baseReadTimeout;
    int flag = (queueLen - 100) / 10;
    if (flag >= 0) {
      if (flag == 0) {
        flag = 1;
      }
      result = baseReadTimeout / (2 * flag);
    } else if (flag < -7) {
      result = baseReadTimeout - ((flag) * (baseReadTimeout / 10));
    }

    if (result > 2 * baseReadTimeout) {
      result = baseReadTimeout;
    } else if (result < 5) {
      result = 5;/** min timeout is 5ms */
    }
    if (queueLen > 50) {
      logger.warn("--ServiceName:[" + this.getServiceName() + "],ServiceIP:[" + this.getServiceIP() + "],IsProtected:true,queueLen:"
          + queueLen + ",timeout:" + result + ",baseReadTimeout:" + baseReadTimeout);
    }
    return result;
  }

  @Override
  protected void finalize() throws Throwable {
    try {
      if (_connecting || (channel != null && channel.isOpen())) {
        dispose(true);
      }
    } catch (Throwable t) {
      logger.error("Pool Release Error!:", t);
    } finally {
      super.finalize();
    }
  }

  /**
   * Get Socket statu
   * 
   * @return Socket statu
   */
  public boolean connecting() {
    return _connecting;
  }

  protected boolean inPool() {
    return _inPool;
  }

  protected void setInPool(boolean inPool) {
    _inPool = inPool;
  }

  protected SocketChannel getChannle() {
    return channel;
  }

  /**
   * 该链接是否是空闲状态
   */
  protected boolean isIdle() {
    return !(WaitWindows.size() > 0);
  }

  protected void waitDestroy() {
    this.waitDestroy = true;
  }

  public boolean isRights() {
    return rights;
  }

  public void setRights(boolean rights) {
    this.rights = rights;
  }

  public byte[] getDESKey() {
    return DESKey;
  }

  public void setDESKey(byte[] dESKey) {
    DESKey = dESKey;
  }

  public String getServiceIP() {
    if (socket != null && !socket.isClosed()) {
      try {
        return socket.getInetAddress().getHostAddress();
      } catch (Exception ex) {
        return null;
      }
    }
    return null;
  }

  public String getServiceName() {
    if (pool != null) {
      return pool.getServicename();
    }
    return null;
  }

  public int getConfigTime() {
    return getReadTimeout(socketConfig.getReceiveTimeout(), 1);
  }

  @Override
  public String toString() {
    try {
      return (socket == null) ? "" : socket.toString();
    } catch (Throwable ex) {
      return "Socket[error:" + ex.getMessage() + "]";
    }
  }
}
