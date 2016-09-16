package com.chaboshi.scf.server.performance.monitorweb;

import java.util.concurrent.atomic.AtomicInteger;

import com.chaboshi.scf.server.contract.log.ILog;
import com.chaboshi.scf.server.contract.log.LogFactory;

/**
 * AbandonCount 任务超时计数
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class AbandonCount {
  static ILog logger = LogFactory.getLogger(AbandonCount.class);
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
