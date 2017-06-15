/*
 * Copyright 2010 www.58.com, Inc.
 * @author Service Platform Architecture Team mail: spat@58.com web: http://www.58.com
 */
package com.chaboshi.scf.client.entity;

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
