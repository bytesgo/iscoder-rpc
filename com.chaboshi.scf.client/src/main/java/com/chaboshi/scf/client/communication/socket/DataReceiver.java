/*
 * Copyright 2010 www.58.com, Inc.
 * 
 * @author Service Platform Architecture Team
 * mail: spat@58.com
 * web: http://www.58.com
 */
package com.chaboshi.scf.client.communication.socket;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.chaboshi.scf.client.utility.logger.ILog;
import com.chaboshi.scf.client.utility.logger.LogFactory;

/**
 * DataReceiver
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
class DataReceiver {

  private static DataReceiver _DataReceiver = null;
  List<CSocket> sockets = new ArrayList<CSocket>();
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

  public synchronized void RegSocketChannel(final CSocket socket) throws ClosedChannelException, IOException {
    sockets.add(socket);
  }

  public synchronized void UnRegSocketChannel(CSocket socket) {
    sockets.remove(socket);
    Handle.RemoveInstance(socket);
  }
}

class Worker implements Runnable {

  private final static int T_COUNT = Runtime.getRuntime().availableProcessors();// CPU核数
  private ExecutorService pool = Executors.newFixedThreadPool(T_COUNT, new ThreadRenameFactory("Async DataReceiver Thread"));
  private static ILog logger = LogFactory.getLogger(Worker.class);
  List<CSocket> sockets = null;

  public Worker(List<CSocket> sockets) {
    this.sockets = sockets;
  }

  @Override
  public void run() {
    while (true) {
      try {
        for (final CSocket socket : sockets) {
          try {
            pool.execute(Handle.getInstance(socket));
          } catch (Throwable ex) {
            logger.error(ex);
          }
        }
        Thread.sleep(1);
      } catch (Throwable ex) {
        logger.error(ex);
      }
    }
  }
}

class Handle implements Runnable {

  private static ConcurrentHashMap<CSocket, Handle> mapInstance = new ConcurrentHashMap<CSocket, Handle>();
  private static ILog logger = LogFactory.getLogger(Handle.class);
  private CSocket socket = null;
  private final static Object lockHelper = new Object();

  protected static Handle getInstance(CSocket socket) {
    Handle handle = mapInstance.get(socket);
    if (handle == null) {
      synchronized (lockHelper) {
        if (handle == null) {
          handle = new Handle(socket);
          mapInstance.put(socket, handle);
        }
      }
    }
    return handle;

  }

  private Handle(CSocket socket) {
    this.socket = socket;
  }

  protected static void RemoveInstance(CSocket socket) {
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
