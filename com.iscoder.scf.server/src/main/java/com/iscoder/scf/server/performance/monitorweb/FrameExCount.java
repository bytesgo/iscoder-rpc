package com.iscoder.scf.server.performance.monitorweb;

import java.util.concurrent.atomic.AtomicInteger;

public class FrameExCount {
  private static AtomicInteger count = new AtomicInteger(0);

  public static void messageRecv() {
    count.getAndIncrement();
  }

  public static int getCount() {
    return count.get();
  }

  public static void initCount(int i) {
    count.set(i);
  }
}
