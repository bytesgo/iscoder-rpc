package com.iscoder.scf.client.channel;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iscoder.scf.common.utils.ThreadNamedFactory;

/**
 * DataReceiver
 * 
 */
public class DataReceiver {

  private static DataReceiver _DataReceiver = null;
  List<SCFChannel> sockets = new ArrayList<SCFChannel>();
  private final static Object LockHelper = new Object();

  private DataReceiver() {
    Worker worker = new Worker(sockets);
    Thread thread = new Thread(worker);
    thread.setDaemon(true);
    thread.start();
  }

  public static DataReceiver instance() throws ClosedChannelException, IOException {
    if (_DataReceiver == null) {
      synchronized (LockHelper) {
        if (_DataReceiver == null) {
          _DataReceiver = new DataReceiver();
        }
      }
    }
    return _DataReceiver;
  }

  public synchronized void RegSocketChannel(final SCFChannel socket) throws ClosedChannelException, IOException {
    sockets.add(socket);
  }

  public synchronized void UnRegSocketChannel(SCFChannel socket) {
    sockets.remove(socket);
    Handler.removeInstance(socket);
  }
}

class Worker implements Runnable {

  private final static int T_COUNT = Runtime.getRuntime().availableProcessors();// CPU核数
  private ExecutorService pool = Executors.newFixedThreadPool(T_COUNT, new ThreadNamedFactory("Async DataReceiver Thread"));
  private static final Logger logger = LoggerFactory.getLogger(Worker.class);
  List<SCFChannel> sockets = null;

  public Worker(List<SCFChannel> sockets) {
    this.sockets = sockets;
  }

  @Override
  public void run() {
    while (true) {
      try {
        for (final SCFChannel socket : sockets) {
          try {
            pool.execute(Handler.getInstance(socket));
          } catch (Throwable ex) {
            logger.error("", ex);
          }
        }
        Thread.sleep(1);
      } catch (Throwable ex) {
        logger.error("", ex);
      }
    }
  }
}

class Handler implements Runnable {

  private static ConcurrentHashMap<SCFChannel, Handler> mapInstance = new ConcurrentHashMap<SCFChannel, Handler>();
  private static final Logger logger = LoggerFactory.getLogger(Handler.class);
  private SCFChannel socket = null;
  private final static Object lockHelper = new Object();

  protected static Handler getInstance(SCFChannel socket) {
    Handler handler = mapInstance.get(socket);
    if (handler == null) {
      synchronized (lockHelper) {
        if (handler == null) {
          handler = new Handler(socket);
          mapInstance.put(socket, handler);
        }
      }
    }
    return handler;

  }

  private Handler(SCFChannel socket) {
    this.socket = socket;
  }

  protected static void removeInstance(SCFChannel socket) {
    synchronized (lockHelper) {
      mapInstance.remove(socket);
    }
  }

  @Override
  public void run() {
    try {
      socket.frameHandle();
    } catch (Throwable ex) {
      logger.error("socket frameHandle error " + ex);
      if (!socket.connecting()) {
        socket.dispose(true);
      }
    }
  }
}
