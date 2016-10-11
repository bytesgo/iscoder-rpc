/*
 * Copyright 2010 www.58.com, Inc.
 * @author Service Platform Architecture Team mail: spat@58.com web: http://www.58.com
 */
package com.chaboshi.scf.client.loadbalance;

import com.chaboshi.scf.client.communication.socket.SocketPool;

/**
 * TimerJob
 *
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class TimerJob implements Runnable {

  private Server server = null;

  public TimerJob(Server server) {
    super();
    this.server = server;
  }

  @Override
  public void run() {
    /**
     * 如果当前连接处于重启状态则注销当前服务所有socket
     */
    try {
      SocketPool sp = server.getScoketpool();
      try {
        sp.destroy();
      } catch (Throwable e) {
        System.out.println("destroy socket fail!");
        e.printStackTrace();
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    } catch (Throwable e) {
      e.printStackTrace();
    }
  }

}
