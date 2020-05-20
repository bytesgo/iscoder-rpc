package com.github.leeyazhou.scf.client.loadbalance;

import com.github.leeyazhou.scf.client.channel.ChannelFactory;

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
      ChannelFactory sp = server.getScoketpool();
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
