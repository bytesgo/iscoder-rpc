package com.chaboshi.scf.client.communication.socket;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chaboshi.scf.client.utility.jsr.LinkedTransferQueue;
import com.chaboshi.scf.client.utility.jsr.TransferQueue;
import com.chaboshi.scf.protocol.exception.TimeoutException;

public class NIOHandler {
  private static final Log logger = LogFactory.getLog(NIOHandler.class);
  private final static TransferQueue<WindowData> writeQueue = new LinkedTransferQueue<WindowData>();
  private final static TransferQueue<TimeOut> timeOutQueue = new LinkedTransferQueue<TimeOut>();
  private static NIOHandler handler = new NIOHandler();

  private NIOHandler() {

  }

  public void start() {
    SendWorker sendWorker = new SendWorker();
    Thread thread = new Thread(sendWorker);
    thread.setName("client async sendThread ");
    thread.setDaemon(true);
    thread.start();

    /**
     * 计算超时时间
     */
    Thread toThread = new Thread(new Runnable() {

      @Override
      public void run() {
        for (;;) {
          try {
            TimeOut to = timeOutQueue.poll(1000, TimeUnit.MILLISECONDS);
            if (to != null) {
              if (to.getTime() - to.getWd().getTimestamp() > to.getcSocket().getTimeOut()) {
                // 超时了~
                String exceptionMsg = "ServiceName:[" + to.getcSocket().getServiceName() + "],ServiceIP:[" + to.getcSocket().getServiceIP()
                    + "],Receive data timeout or error!timeout:" + (to.getTime() - to.getWd().getTimestamp());
                to.getWd().getReceiveHandler().callBack(new TimeoutException(exceptionMsg));
              }
            }
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }
    });

    toThread.setName("time out");
    toThread.setDaemon(true);
    toThread.start();
  }

  public static synchronized NIOHandler getInstance() {
    return handler;
  }

  public void offerWriteData(WindowData wd) {
    if (writeQueue.size() > 50000) {
      logger.warn("writeQueue size > 50000");
      return;
    }
    writeQueue.offer(wd);
  }

  public void offerTimeOut(TimeOut to) {
    if (timeOutQueue.size() > 50000) {
      logger.warn("timeOutQueue size > 50000");
      return;
    }
    timeOutQueue.offer(to);
  }

  class SendWorker implements Runnable {
    @Override
    public void run() {
      for (;;) {
        WindowData wd = null;
        try {
          wd = (WindowData) NIOHandler.writeQueue.poll(1000, TimeUnit.MILLISECONDS);
          if (wd != null) {
            switch (wd.getFlag()) {
            case 1:
              wd.getCsocket().send(wd.getSendData());
              break;
            default:
              break;
            }
          }
        } catch (InterruptedException e) {
          e.printStackTrace();
        } catch (IOException e) {
          e.printStackTrace();
        } catch (Throwable e) {
          e.printStackTrace();
        }
      }
    }
  }

}
