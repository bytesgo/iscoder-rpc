/*
 * Copyright 2010 www.58.com, Inc.
 * @author Service Platform Architecture Team mail: spat@58.com web: http://www.58.com
 */
package com.chaboshi.scf.client.util;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class AutoResetEvent {
  CountDownLatch cdl;

  public AutoResetEvent() {
    cdl = new CountDownLatch(1);
  }

  public AutoResetEvent(int waitCount) {
    cdl = new CountDownLatch(waitCount);
  }

  public void set() {
    cdl.countDown();
  }

  public boolean waitOne(long time) throws InterruptedException {
    return cdl.await(time, TimeUnit.MILLISECONDS);
  }
}
