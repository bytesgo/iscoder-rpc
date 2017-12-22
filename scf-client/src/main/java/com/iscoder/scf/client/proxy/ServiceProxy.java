package com.iscoder.scf.client.proxy;

import java.io.IOException;
import java.nio.channels.UnresolvedAddressException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iscoder.scf.client.entity.ServiceConfig;
import com.iscoder.scf.client.loadbalance.Dispatcher;
import com.iscoder.scf.client.loadbalance.Server;
import com.iscoder.scf.client.loadbalance.component.ServerChoose;
import com.iscoder.scf.client.loadbalance.component.ServerState;
import com.iscoder.scf.client.proxy.builder.InvokeResult;
import com.iscoder.scf.client.proxy.builder.Parameter;
import com.iscoder.scf.client.proxy.builder.ReceiveHandler;
import com.iscoder.scf.common.SCFConst;
import com.iscoder.scf.common.entity.KeyValuePair;
import com.iscoder.scf.common.exception.ExceptionProtocol;
import com.iscoder.scf.common.exception.RebootException;
import com.iscoder.scf.common.exception.ThrowErrorHelper;
import com.iscoder.scf.common.exception.TimeoutException;
import com.iscoder.scf.protocol.entity.CompressType;
import com.iscoder.scf.protocol.entity.PlatformType;
import com.iscoder.scf.protocol.entity.SDPType;
import com.iscoder.scf.protocol.sdp.HandclaspProtocol;
import com.iscoder.scf.protocol.sdp.RequestProtocol;
import com.iscoder.scf.protocol.sdp.ResponseProtocol;
import com.iscoder.scf.protocol.sfp.Protocol;

/**
 * ServiceProxy
 */
public class ServiceProxy {

  private static final Logger logger = LoggerFactory.getLogger(ServiceProxy.class);
  private int count = 0;
  private int sessionId = 1;
  private int requestTime = 0;
  /** 超时重连次数 */
  private int ioreconnect = 0;
  /** IO服务切换次数 */
  private ServiceConfig config;
  private Dispatcher dispatcher;
  @SuppressWarnings("unused")
  private AtomicInteger requestCount = new AtomicInteger(0);
  private static final Object locker = new Object();
  private static final Object lockerSessionID = new Object();
  private static final HashMap<String, ServiceProxy> serviceProxys = new HashMap<String, ServiceProxy>();
  private static HashMap<String, ServerChoose> methodServer = new HashMap<String, ServerChoose>();

  private ServiceProxy(String serviceName) throws Exception {
    config = ServiceConfig.GetConfig(serviceName);
    dispatcher = new Dispatcher(config);

    requestTime = config.getSocketPool().getReconnectTime();
    int serverCount = 1;
    if (dispatcher.GetAllServer() != null && dispatcher.GetAllServer().size() > 0) {
      serverCount = dispatcher.GetAllServer().size();
    }
    ioreconnect = serverCount - 1;
    count = requestTime;

    if (ioreconnect > requestTime) {
      count = ioreconnect;
    }
  }

  private void destroy() {
    List<Server> serverList = dispatcher.GetAllServer();
    if (serverList != null) {
      for (Server server : serverList) {
        try {
          server.getScoketpool().destroy();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
  }

  public static ServiceProxy getProxy(String serviceName) throws Exception {
    ServiceProxy serviceProxy = serviceProxys.get(serviceName.toLowerCase());
    if (serviceProxy == null) {
      synchronized (locker) {
        serviceProxy = serviceProxys.get(serviceName.toLowerCase());
        if (serviceProxy == null) {
          serviceProxy = new ServiceProxy(serviceName);
          serviceProxys.put(serviceName.toLowerCase(), serviceProxy);
        }
      }
    }
    return serviceProxy;
  }

  @SuppressWarnings("rawtypes")
  public InvokeResult<?> invoke(Parameter returnType, String typeName, String methodName, Parameter[] paras) throws Exception, Throwable {
    long watcher = System.currentTimeMillis();
    List<KeyValuePair> listPara = new ArrayList<KeyValuePair>();
    for (Parameter p : paras) {
      listPara.add(new KeyValuePair(p.getSimpleName(), p.getValue()));
    }
    RequestProtocol requestProtocol = new RequestProtocol(typeName, methodName, listPara);
    Protocol sendProtocol = new Protocol(createSessionId(), (byte) config.getServiceid(), SDPType.Request, CompressType.UnCompress,
        config.getProtocol().getSerializerType(), PlatformType.Java, requestProtocol);

    Protocol receiveProtocol = null;
    Server server = null;
    String methodPara[] = this.getMethodPara(typeName, methodName, paras);
    for (int i = 0; i <= count; i++) {
      server = this.getKeyServer(methodPara);

      if (server == null) {
        logger.error("cannot get server");
        throw new Exception("cannot get server");
      }
      try {
        receiveProtocol = server.request(sendProtocol);
        break;
      } catch (IOException io) {
        if (count == 0 || i == ioreconnect) {
          throw io;
        }
        if (i < count && i < ioreconnect) {
          logger.error(server.getName() + " server has IOException,system will change normal server!");
          continue;
        }
      } catch (RebootException rb) {
        this.createReboot(server);
        if (count == 0 || i == ioreconnect) {
          throw new IOException("connect fail!");
        }
        if (i < count && i < ioreconnect) {
          logger.error(server.getName() + " server has reboot,system will change normal server!");
          continue;
        }
      } catch (TimeoutException toex) {
        if (count == 0 || i == requestTime) {
          throw toex;
        }
        if (i < count && i < requestTime) {
          logger.error(server.getName() + " server has TimeoutException,system will change normal server!");
          continue;
        }
      } catch (UnresolvedAddressException uaex) {
        /** 无法完全解析给定的远程地址 */
        this.createDead(server);

        throw uaex;
      } catch (Throwable ex) {
        logger.error("invoke other Exception", ex);
        throw ex;
      }

    }

    if (receiveProtocol == null) {
      throw new Exception("userdatatype error!");
    }

    if (receiveProtocol.getSDPType() == SDPType.Response) {
      ResponseProtocol response = (ResponseProtocol) receiveProtocol.getSdpEntity();
      logger.debug("invoke time:" + (System.currentTimeMillis() - watcher) + "ms");
      return new InvokeResult(response.getResult(), response.getOutpara());
    } else if (receiveProtocol.getSDPType() == SDPType.Reset) {
      /** 服务重启 */
      logger.warn(server.getName() + " server is reboot, system will change normal server!");
      this.createReboot(server);
      return invoke(returnType, typeName, methodName, paras);
    } else if (receiveProtocol.getSDPType() == SDPType.Exception) {
      ExceptionProtocol ep = (ExceptionProtocol) receiveProtocol.getSdpEntity();
      throw ThrowErrorHelper.throwServiceError(ep.getErrorCode(), ep.getErrorMsg());
    } else {
      throw new Exception("userdatatype error!");
    }
  }

  public void invoke(Parameter returnType, String typeName, String methodName, Parameter[] paras, ReceiveHandler rh)
      throws Exception, Throwable {
    List<KeyValuePair> listPara = new ArrayList<KeyValuePair>();
    for (Parameter p : paras) {
      listPara.add(new KeyValuePair(p.getSimpleName(), p.getValue()));
    }
    RequestProtocol requestProtocol = new RequestProtocol(typeName, methodName, listPara);
    Protocol sendP = new Protocol(createSessionId(), (byte) config.getServiceid(), SDPType.Request, CompressType.UnCompress,
        config.getProtocol().getSerializerType(), PlatformType.Java, requestProtocol);

    Server server = null;
    String[] methodPara = this.getMethodPara(typeName, methodName, paras);
    for (int i = 0; i <= count; i++) {
      server = this.getKeyServer(methodPara);
      if (server == null) {
        logger.error("cannot get server");
        throw new Exception("cannot get server");
      }
      try {
        rh.setServer(server);
        server.requestAsync(sendP, rh);
        break;
      } catch (IOException io) {
        if (count == 0 || i == ioreconnect) {
          throw io;
        }
        if (i < count && i < ioreconnect) {
          logger.error(server.getName() + " server has IOException,system will change normal server!");
          continue;
        }
      } catch (RebootException rb) {
        this.createReboot(server);
        if (count == 0 || i == ioreconnect) {
          throw new IOException("connect fail!");
        }
        if (i < count && i < ioreconnect) {
          logger.error(server.getName() + " server has reboot,system will change normal server!");
          continue;
        }
      } catch (TimeoutException toex) {
        if (count == 0 || i == requestTime) {
          throw toex;
        }
        if (i < count && i < requestTime) {
          logger.error(server.getName() + " server has TimeoutException,system will change normal server!");
          continue;
        }
      } catch (UnresolvedAddressException uaex) {
        /** 无法完全解析给定的远程地址 */
        this.createDead(server);
        throw uaex;
      } catch (Throwable ex) {
        logger.error("invoke other Exception", ex);
        throw ex;
      }
    }
  }

  /**
   * 设置当前重启服务
   * 
   * @param server
   * @throws Throwable
   * @throws Exception
   */
  private void createReboot(Server server) throws Exception, Throwable {
    server.createReboot();
  }

  /**
   * 设置当前服务状态为dead
   * 
   * @param server
   * @throws Throwable
   * @throws Exception
   */
  private void createDead(Server server) throws Exception, Throwable {
    server.createDead();
  }

  /**
   * 权限协议
   * 
   * @param data
   * @return Protocol
   * @throws Exception
   */
  public Protocol createProtocol(HandclaspProtocol hp) throws Exception {
    Protocol sendRightsProtocol = new Protocol(createSessionId(), (byte) config.getServiceid(), SDPType.Request, CompressType.UnCompress,
        config.getProtocol().getSerializerType(), PlatformType.Java, hp);
    return sendRightsProtocol;
  }

  /**
   * get Server info
   * 
   * @param name Server name
   * @return if Server exist return Server info else return empty
   */
  public String getServer(String name) {
    Server server = dispatcher.GetServer(name);
    if (server == null) {
      return "";
    }
    return server.toString();
  }

  public static void destroyAll() {
    Collection<ServiceProxy> spList = serviceProxys.values();
    if (spList != null) {
      for (ServiceProxy sp : spList) {
        sp.destroy();
      }
    }
  }

  private int createSessionId() {
    synchronized (lockerSessionID) {
      if (sessionId > SCFConst.MAX_SESSIONID) {
        sessionId = 1;
      }
      return sessionId++;
    }
  }

  /**
   * 设置方法固定到具体服务器
   * 
   * @param lookup 类名
   * @param serverName 服务器名
   * @throws Exception
   */
  public static void setServer(String lookup, String methodName, List<String> para, String[] serverName) throws Exception {

    if (serverName != null) {
      ServerChoose sc = new ServerChoose();
      sc.setServerName(serverName);
      sc.setServiceCount(serverName.length);

      StringBuffer sb = new StringBuffer();
      sb.append(lookup);
      sb.append(methodName);
      if (para != null) {
        for (String str : para) {
          sb.append(str);
        }
      }
      if (!methodServer.containsKey(sb.toString())) {
        methodServer.put(sb.toString(), sc);
      }

    } else {
      logger.error("serverName is null");
      throw new Exception("para or serverName is null");
    }
  }

  /**
   * 根据类名、方法名确定这一类方法在某些服务器上执行
   * 
   */
  public static void setServer(String lookup, String methodName, String[] serverName) throws Exception {
    if (serverName != null) {
      ServerChoose sc = new ServerChoose();
      sc.setServerName(serverName);
      sc.setServiceCount(serverName.length);

      StringBuffer sb = new StringBuffer();
      sb.append(lookup);
      sb.append(methodName);

      if (!methodServer.containsKey(sb.toString())) {
        methodServer.put(sb.toString(), sc);
      }

    } else {
      logger.error("serverName is null");
      throw new Exception("para or serverName is null");
    }
  }

  /**
   * 根据类名确定这个类的所有方法，固定发送到具体服务器
   * 
   */
  public static void setServer(String lookup, String[] serverName) throws Exception {
    if (serverName != null) {
      ServerChoose sc = new ServerChoose();
      sc.setServerName(serverName);
      sc.setServiceCount(serverName.length);

      StringBuffer sb = new StringBuffer();
      sb.append(lookup);

      if (!methodServer.containsKey(sb.toString())) {
        methodServer.put(sb.toString(), sc);
      }

    } else {
      logger.error("serverName is null");
      throw new Exception("para or serverName is null");
    }
  }

  public String[] getMethodPara(String lookup, String methodName, Parameter[] paras) {
    String[] str = new String[3];
    StringBuffer sb = new StringBuffer();
    sb.append(lookup);
    str[2] = sb.toString();
    sb.append(methodName);
    str[1] = sb.toString();
    if (paras != null && paras.length == 0) {
      sb.append("null");
    }
    for (Parameter p : paras) {
      sb.append(p.getSimpleName());
    }
    str[0] = sb.toString();
    return str;
  }

  private Server getKeyServer(String[] key) {
    Server server = null;
    for (int i = 0; i < key.length; i++) {
      if (methodServer.containsKey(key[i])) {
        server = dispatcher.GetServer(methodServer.get(key[i]));
        if (server != null) {
          break;
        }
      }
    }
    if (server == null) {
      server = dispatcher.GetServer();
    }

    if (server.getState() == ServerState.Dead) {
      if (!server.isTesting()) {
        if (server.testing()) {
          server.setTesting(true);
          server.setState(ServerState.Testing);
        } else {
          server.setTesting(false);
        }
      }
    }
    return server;
  }

}
