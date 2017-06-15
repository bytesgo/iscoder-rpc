package com.chaboshi.scf.client.loadbalance;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chaboshi.scf.client.channel.ChannelFactory;
import com.chaboshi.scf.client.entity.ServerProfile;
import com.chaboshi.scf.client.entity.ServiceConfig;
import com.chaboshi.scf.client.loadbalance.component.ServerChoose;
import com.chaboshi.scf.client.loadbalance.component.ServerState;

/**
 * Dispatcher
 */
public class Dispatcher {

  private static final Logger logger = LoggerFactory.getLogger(Dispatcher.class);
  private List<Server> ServerPool = new ArrayList<Server>();
  private AtomicInteger requestCount = new AtomicInteger(0);

  /**
   * Constructor
   * 
   * @param Configuration.Configuration object
   */
  public Dispatcher(ServiceConfig config) {
    logger.info("starting init servers");
    logger.debug("init connection begin:" + System.currentTimeMillis());
    for (ServerProfile ser : config.getServers()) {
      if (ser.getWeithtRate() > 0) {
        Server s = new Server(ser);
        if (s.getState() != ServerState.Disable) {
          // ScoketPool sp = new ScoketPool(s, config.getSocketPool());
          ChannelFactory sp = new ChannelFactory(s, config);
          s.setScoketpool(sp);
          ServerPool.add(s);
        }
      }
    }
    logger.debug("init connection end:" + System.currentTimeMillis());
    logger.info("init servers end");
  }

  /**
   * get Server from Server pool
   * 
   * @return return a Server minimum of current user
   */
  public Server GetServer() {
    if (ServerPool == null || ServerPool.size() == 0) {
      return null;
    }
    Server result = null;
    int currUserCount = -1;

    int count = ServerPool.size();// server num
    int start = requestCount.get() % count;
    if (requestCount.get() > 100) {
      requestCount.set(0);
    } else {
      requestCount.getAndIncrement(); // requestCount++的同步表示。
                                      // 当requestCount大于10时，重新置为0,
    }

    for (int i = start; i < start + count; i++) {
      int index = i % count;
      Server server = ServerPool.get(index);

      if (server.getState() == ServerState.Dead && (System.currentTimeMillis() - server.getDeadTime()) > server.getDeadTimeout()) {
        synchronized (this) {
          if (server.getState() == ServerState.Dead && (System.currentTimeMillis() - server.getDeadTime()) > server.getDeadTimeout()) {
            result = server;
            server.setState(ServerState.Testing);
            server.setDeadTime(0);
            logger.warn("find server that need to test!host:" + server.getAddress());
            break;
          }
        }
      }

      if (server.getState() == ServerState.Reboot && (System.currentTimeMillis() - server.getDeadTime()) > server.getDeadTimeout()) {
        synchronized (this) {
          if (server.getState() == ServerState.Reboot && (System.currentTimeMillis() - server.getDeadTime()) > server.getDeadTimeout()) {
            result = server;
            server.setState(ServerState.Testing);
            server.setDeadTime(0);
            // --requestCount;
            requestCount.getAndDecrement();// requestCount--;
            logger.warn("find server that need to test!host:" + server.getAddress());
            break;
          }
        }
      }

      if ((server.getCurrUserCount() < currUserCount * server.getWeightRage() || currUserCount < 0)
          && server.getState() == ServerState.Normal) {
        currUserCount = server.getCurrUserCount();
        result = server;
      }
    }

    if (result == null) {
      result = ServerPool.get(new Random().nextInt(count));
      logger.warn("Not get server, This server is " + result.getState() + " DeadTime:" + result.getDeadTime() + " DeadTimeout"
          + result.getDeadTimeout());
    }
    return result;
  }

  public Server GetServer(String name) {
    for (Server s : ServerPool) {
      if (s.getName().equalsIgnoreCase(name)) {
        return s;
      }
    }
    return null;
  }

  public List<Server> GetAllServer() {
    return ServerPool;
  }

  /**
   * 根据特定服务器集合ServerChoose.serverName[]选中服务器
   * 
   */
  public Server GetServer(ServerChoose sc) {
    if (ServerPool == null || ServerPool.size() == 0) {
      return null;
    }
    Server result = null;

    int count = sc.getServiceCount();// server num
    int start = requestCount.get() % count;
    if (requestCount.get() > 100) {
      requestCount.set(0);
    } else {
      requestCount.getAndIncrement(); // requestCount++的同步表示。
                                      // 当requestCount大于10时，重新置为0,
    }

    for (int i = start; i < start + count; i++) {
      int index = i % count;
      Server server = this.GetServer(sc.getServerName()[index]);
      int currUserCount = -1;
      if (server.getState() == ServerState.Dead && (System.currentTimeMillis() - server.getDeadTime()) > server.getDeadTimeout()) {
        synchronized (this) {
          if (server.getState() == ServerState.Dead && (System.currentTimeMillis() - server.getDeadTime()) > server.getDeadTimeout()) {
            server.setState(ServerState.Testing);
            server.setDeadTime(0);
            result = server;
            logger.warn("find server that need to test!host:" + server.getAddress());
            break;
          }
        }
      }

      if (server.getState() == ServerState.Reboot && (System.currentTimeMillis() - server.getDeadTime()) > server.getDeadTimeout()) {
        synchronized (this) {
          if (server.getState() == ServerState.Reboot && (System.currentTimeMillis() - server.getDeadTime()) > server.getDeadTimeout()) {
            server.setState(ServerState.Testing);
            server.setDeadTime(0);
            result = server;
            requestCount.getAndDecrement();
            logger.warn("find server that need to test!host:" + server.getAddress());
            break;
          }
        }
      }

      if ((server.getCurrUserCount() < currUserCount * server.getWeightRage() || currUserCount < 0)
          && server.getState() == ServerState.Normal) {
        currUserCount = server.getCurrUserCount();
        result = server;
      }
    }

    if (result == null) {
      if (ServerPool.size() - sc.getServiceCount() == 0) {
        result = ServerPool.get(new Random().nextInt(count));
      } else {
        int counts = requestCount.get() % (ServerPool.size() - sc.getServiceCount());
        result = this.GetServer(getNoName(sc.getServerName())[counts]);
      }

      logger.warn("Not get Specified server, This server is " + result.getState() + " DeadTime:" + result.getDeadTime() + " DeadTimeout"
          + result.getDeadTimeout());
    }
    return result;
  }

  /**
   * 获取指定服务器外的，服务器集合
   * 
   */
  private String[] getNoName(String[] serverName) {
    String[] str = new String[ServerPool.size() - serverName.length];
    int count = 0;
    for (Server s : ServerPool) {
      for (String strName : serverName) {
        if (!s.getName().equals(strName)) {
          str[count++] = s.getName();
        }
      }
    }
    return str;
  }
}
