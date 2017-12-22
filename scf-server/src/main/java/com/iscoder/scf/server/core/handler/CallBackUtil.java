package com.iscoder.scf.server.core.handler;

import java.util.concurrent.TimeUnit;

import com.iscoder.scf.common.utils.SystemUtil;
import com.iscoder.scf.common.utils.spat.jsr.LinkedTransferQueue;
import com.iscoder.scf.common.utils.spat.jsr.TransferQueue;
import com.iscoder.scf.server.contract.context.Global;

public class CallBackUtil {

  private static final int COUNT = SystemUtil.getHalfCpuProcessorCount();
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
            if (AsyncBack.contextMap.get(wd.getSessionID()).isDel()) {
              AsyncBack.contextMap.remove(wd.getSessionID());// 使用完删除context
              continue;
            }
            if ((System.currentTimeMillis() - wd.getTime()) > taskTimeOut) {
              AsyncBack.send(wd.getSessionID(), new Exception("wait other server recive timeout.wait time is " + taskTimeOut));
            } else {
              offer(wd);
              Thread.sleep(1);
            }
          }

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
