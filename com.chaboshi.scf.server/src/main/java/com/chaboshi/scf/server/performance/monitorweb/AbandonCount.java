package com.chaboshi.scf.server.performance.monitorweb;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * AbandonCount 任务超时计数
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class AbandonCount {
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
