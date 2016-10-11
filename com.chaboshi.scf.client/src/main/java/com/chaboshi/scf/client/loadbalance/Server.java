/*
 * Copyright 2010 www.58.com, Inc.
 * @author Service Platform Architecture Team mail: spat@58.com web: http://www.58.com
 */
package com.chaboshi.scf.client.loadbalance;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chaboshi.scf.client.communication.socket.SCFSocket;
import com.chaboshi.scf.client.communication.socket.SocketPool;
import com.chaboshi.scf.client.communication.socket.ThreadRenameFactory;
import com.chaboshi.scf.client.communication.socket.WindowData;
import com.chaboshi.scf.client.configuration.loadbalance.ServerProfile;
import com.chaboshi.scf.client.loadbalance.component.ServerState;
import com.chaboshi.scf.client.proxy.builder.ReceiveHandler;
import com.chaboshi.scf.protocol.sfp.v1.Protocol;

/**
 * Server
 *
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class Server {

  private static final Logger logger = LoggerFactory.getLogger(Server.class);
  private int port;
  private int weight;
  private String name;
  private long deadTime;
  private String address;
  private int deadTimeout;
  private float weightRage;
  private int currUserCount;
  private ServerState state;
  private SocketPool scoketpool;
  private boolean testing = false;
  private final ScheduledExecutorService scheduler;

  protected Server(ServerProfile config) {
    this.name = config.getName();
    this.address = config.getHost();
    this.port = config.getPort();
    this.weightRage = config.getWeithtRate();
    this.deadTimeout = config.getDeadTimeout();
    if (this.weightRage >= 0) {
      this.state = ServerState.Normal;
    } else {
      this.state = ServerState.Disable;
    }
    scheduler = Executors.newScheduledThreadPool(2, new ThreadRenameFactory("Async " + this.getName() + "-Server Thread"));
  }

  public long getDeadTime() {
    return deadTime;
  }

  public void setDeadTime(long deadTime) {
    this.deadTime = deadTime;
  }

  public String getName() {
    return name;
  }

  public String getAddress() {
    return address;
  }

  public int getCurrUserCount() {
    return currUserCount;
  }

  public int getPort() {
    return port;
  }

  public SocketPool getScoketpool() {
    return scoketpool;
  }

  protected void setScoketpool(SocketPool scoketpool) {
    this.scoketpool = scoketpool;
  }

  public ServerState getState() {
    return state;
  }

  public synchronized void setState(ServerState state) {
    this.state = state;
  }

  public int getWeight() {
    return weight;
  }

  public float getWeightRage() {
    return weightRage;
  }

  public int getDeadTimeout() {
    return deadTimeout;
  }

  protected void setDeadTimeout(int deadTimeout) {
    this.deadTimeout = deadTimeout;
  }

  public boolean isTesting() {
    return testing;
  }

  public void setTesting(boolean testing) {
    this.testing = testing;
  }

  public Protocol request(Protocol p) throws Exception, Throwable {
    if (state == ServerState.Dead) {
      logger.warn("This proxy server is unavailable.state:" + state + "+host:" + address);
      throw new Exception("This proxy server is unavailable.state:" + state + "+host:" + address);
    }
    increaseCU();
    SCFSocket socket = null;
    try {
      try {
        socket = this.scoketpool.getSocket();
        byte[] data = p.toBytes(socket.isRights(), socket.getDESKey());
        socket.registerRec(p.getSessionId());
        socket.send(data);
      } catch (Throwable ex) {
        logger.error("Server get socket Exception", ex);
        throw ex;
      } finally {
        if (socket != null) {
          socket.dispose();
        }
      }
      byte[] buffer = socket.receive(p.getSessionId(), currUserCount);
      Protocol result = Protocol.fromBytes(buffer, socket.isRights(), socket.getDESKey());
      if (this.state == ServerState.Testing) {
        relive();
      }
      return result;
    } catch (IOException ex) {
      logger.error("io exception", ex);
      if (socket == null || !socket.connecting()) {
        if (!test()) {
          markAsDead();
        }
      }
      throw ex;
    } catch (Throwable ex) {
      logger.error("request other Exception", ex);
      throw ex;
    } finally {
      if (state == ServerState.Testing) {
        markAsDead();
      }
      if (socket != null) {
        socket.unregisterRec(p.getSessionId());
      }
      decreaseCU();
    }
  }

  /**
   * 异步
   * 
   * @param p
   * @return
   * @throws Exception
   * @throws Throwable
   */
  public void requestAsync(Protocol p, ReceiveHandler receiveHandler) throws Exception, Throwable {
    if (state == ServerState.Dead) {
      logger.warn("This proxy server is unavailable.state:" + state + "+host:" + address);
      throw new Exception("This proxy server is unavailable.state:" + state + "+host:" + address);
    }
    increaseCU();
    SCFSocket socket = null;
    try {
      try {
        socket = this.scoketpool.getSocket();
        byte[] data = p.toBytes(socket.isRights(), socket.getDESKey());
        WindowData wd = new WindowData(receiveHandler, socket, data);
        socket.registerRec(p.getSessionId(), wd);
        socket.offerAsyncWrite(wd);
      } catch (Throwable ex) {
        logger.error("Server get socket Exception", ex);
        throw ex;
      } finally {
        if (socket != null) {
          socket.dispose();
        }
      }
    } catch (IOException ex) {
      logger.error("io exception", ex);
      if (socket == null || !socket.connecting()) {
        if (!test()) {
          markAsDead();
        }
      }
      throw ex;
    } catch (Throwable ex) {
      logger.error("request other Exception", ex);
      throw ex;
    } finally {
      decreaseCU();
    }
  }

  @Override
  public String toString() {
    return "Name:" + name + ",Address:" + address + ",Port:" + port + ",Weight:" + weight + ",State:" + state.toString() + ",CurrUserCount:"
        + currUserCount + ",ScoketPool:" + scoketpool.count();
  }

  /**
   * Increase current user
   */
  private synchronized void increaseCU() {
    currUserCount++;
  }

  /**
   * Decrease current user
   */
  private synchronized void decreaseCU() {
    currUserCount--;
    if (currUserCount <= 0) {
      currUserCount = 0;
    }
  }

  /**
   * mark Server to dead
   * 
   * @throws IOException
   */
  private void markAsDead() throws Exception {
    logger.info("markAsDead server:" + this.state + "--server hashcode:" + this.hashCode() + "--conn count:" + this.scoketpool.count());
    if (this.state == ServerState.Dead) {
      logger.info("before markAsDead the server is dead!!!");
      return;
    }
    synchronized (this) {
      if (this.state == ServerState.Dead) {
        logger.info("before markAsDead the server is dead!!!");
        return;
      }
      logger.warn("this server is dead!host:" + address);
      this.setState(ServerState.Dead);
      this.deadTime = System.currentTimeMillis();
      this.scoketpool.destroy();
    }
  }

  /**
   * 设置当前重启服务
   * 
   * @param server
   * @throws Throwable
   * @throws Exception
   */
  public void createReboot() throws Exception, Throwable {
    synchronized (this) {
      if (this.state == ServerState.Reboot) {
        logger.info("before markAsReboot the server is Reboot!");
        return;
      }

      logger.warn("this server is reboot! host:" + address);
      this.setState(ServerState.Reboot);// 设置当前服务为重启状态
      this.setDeadTime(System.currentTimeMillis());
      /**
       * 如果当前连接处于重启状态则注销当前服务所有socket 任务调度 3秒后执行
       */
      scheduler.schedule(new TimerJob(this), 3, TimeUnit.SECONDS);
    }
  }

  public void createDead() throws Exception {
    this.markAsDead();
  }

  /**
   * 设置当前服务为正常状态
   */
  public void markAsNormal() {
    this.relive();
  }

  /**
   * relive Server if this Server is died
   */
  public void relive() {
    logger.info("this server is relive!host:" + address);
    if (this.state == ServerState.Normal) {
      return;
    }
    synchronized (this) {
      if (this.state == ServerState.Normal) {
        return;
      }
      logger.info("this server is relive!host:" + address);
      this.state = ServerState.Normal;
    }
  }

  private boolean test() {
    if (testing) {
      return true;
    }
    testing = true;
    boolean result = false;
    try {
      Socket socket = new Socket();
      socket.connect(new InetSocketAddress(this.address, this.port), 100);
      socket.close();
      result = true;
    } catch (Exception e) {
    } finally {
      logger.info("test server :" + this.address + ":" + this.port + "--alive:" + result);
      testing = false;
    }
    return result;
  }

  public boolean testing() {
    return this.test();
  }
}
