package com.chaboshi.scf.server.core.proxy;

import java.util.concurrent.TimeUnit;

import com.chaboshi.scf.server.contract.context.Global;
import com.chaboshi.scf.server.util.SystemUtils;
import com.chaboshi.spat.utility.jsr166.LinkedTransferQueue;
import com.chaboshi.spat.utility.jsr166.TransferQueue;

public class CallBackUtil {

  private static final int COUNT = SystemUtils.getHalfCpuProcessorCount();
  private final TransferQueue<WData> checkQueue = new LinkedTransferQueue<WData>();
  int taskTimeOut = 1000;
  private Thread[] workers;

  public CallBackUtil() {
    String sTaskTimeOut = Global.getSingleton().getServiceConfig().getString("back.task.timeout");
    if (sTaskTimeOut != null && !"".equals(sTaskTimeOut)) {
      taskTimeOut = Integer.parseInt(sTaskTimeOut);
      taskTimeOut = ((taskTimeOut * 3) / 2) + 1;
    }
    // Thread t = new Thread(new CallBackHandle());
    // t.setName("CallBackHandle Thread");
    // t.start();

    workers = new Thread[COUNT];
    for (int i = 0; i < COUNT; i++) {
      workers[i] = new Thread(new CallBackHandle());
      workers[i].setName("CallBackHandle thread[" + i + "]");
      workers[i].setDaemon(true);
      workers[i].start();
    }
  }

  public void offer(WData wd) {
    checkQueue.offer(wd);
  }

  class CallBackHandle implements Runnable {

    @Override
    public void run() {
      for (;;) {
        try {
          WData wd = checkQueue.poll(1500, TimeUnit.MILLISECONDS);
          if (wd != null) {
            if (AsynBack.contextMap.get(wd.getSessionID()).isDel()) {
              AsynBack.contextMap.remove(wd.getSessionID());// 使用完删除context
              continue;
            }
            if ((System.currentTimeMillis() - wd.getTime()) > taskTimeOut) {
              AsynBack.send(wd.getSessionID(), new Exception("wait other server recive timeout.wait time is " + taskTimeOut));
            } else {
              offer(wd);
              Thread.sleep(1);
            }
          }

          // if(System.currentTimeMillis() - time > 30000){
          // logger.error("map count is "+AsynBack.contextMap.size() +"
          // taskTimeOut is "+taskTimeOut+" checkQueue size is
          // "+checkQueue.size());
          // time = System.currentTimeMillis();
          // }

        } catch (InterruptedException e) {
          try {
            Thread.sleep(10);
          } catch (InterruptedException e1) {
            e1.printStackTrace();
          }
          e.printStackTrace();
        } catch (Exception ex) {
          try {
            Thread.sleep(10);
          } catch (InterruptedException ie) {
            ie.printStackTrace();
          }
          ex.printStackTrace();
        }
      }
    }
  }
}
