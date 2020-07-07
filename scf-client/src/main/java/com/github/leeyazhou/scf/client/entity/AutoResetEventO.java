package com.github.leeyazhou.scf.client.entity;

public class AutoResetEventO {

  public synchronized void set() {
    notify();
  }

  public synchronized boolean waitOne(long time) throws InterruptedException {
    long t = System.currentTimeMillis();
    wait(time);
    if (System.currentTimeMillis() - t < time) {
      return true;
    }
    return false;
  }
}
