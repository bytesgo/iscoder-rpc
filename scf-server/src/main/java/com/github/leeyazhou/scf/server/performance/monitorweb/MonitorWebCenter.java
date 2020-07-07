package com.github.leeyazhou.scf.server.performance.monitorweb;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.leeyazhou.scf.server.contract.context.Global;
import com.github.leeyazhou.scf.server.core.communication.Server;
import com.github.leeyazhou.scf.server.performance.exception.SerializeException;

public class MonitorWebCenter implements Server {
  private static final Logger logger = LoggerFactory.getLogger(MonitorWebCenter.class);
  static MonitorUDPClient udp;

  @Override
  public void start() throws Exception {
    logger.info("----------------monitor server start------------------");
    logger.info("-- monitor server send ip: " + Global.getSingleton().getServiceConfig().getString("scf.server.monitor.sendIP"));
    logger.info("-- monitor server port: " + Global.getSingleton().getServiceConfig().getInt("scf.server.monitor.sendPort"));
    logger.info("------------------------------------------------------");
    udp = MonitorUDPClient.getInstrance(Global.getSingleton().getServiceConfig().getString("scf.server.monitor.sendIP"),
        Global.getSingleton().getServiceConfig().getInt("scf.server.monitor.sendPort"), "utf-8");
    Thread thread = new Thread(new Runnable() {
      public void run() {
        try {

          MonitorWebCenter.control(udp);

        } catch (Exception e) {
          logger.error("init monitor server error");
          e.printStackTrace();
        }
      }
    });
    thread.setName("SCF Monitor UDP send Thread");
    thread.start();
  }

  @Override
  public void stop() throws Exception {
    udp.close();
  }

  public static void control(MonitorUDPClient udp) throws Exception {

    MonitorCount mc = new MonitorCount();
    MonitorProtocol countP = new MonitorProtocol(MonitorType.count, (short) 0);
    MonitorProtocol abandonP = new MonitorProtocol(MonitorType.abandon, (short) 0);
    MonitorProtocol frameExP = new MonitorProtocol(MonitorType.frameEx, (short) 0);
    String serviceName = Global.getSingleton().getServiceConfig().getServiceName();
    MonitorJVM mjvm = new MonitorJVM(udp, serviceName);
    int sendtime = Global.getSingleton().getServiceConfig().getInt("scf.server.monitor.timeLag");
    String sendStr;
    while (true) {
      try {
        Thread.sleep(sendtime < 3000 ? 0 : (sendtime - 3000));
        // 并发
        mc.initMCount();
        Thread.sleep(1000);
        sendStr = getSendStr(serviceName, "count", mc.getCount());
        if (sendStr != null) {
          udp.send(countP.dataCreate(sendStr.getBytes()));
        }

        getMaxCount(MonitorCount.getFromIP(), serviceName);

        // 框架异常
        FrameExCount.initCount(0);
        Thread.sleep(1000);
        sendStr = getSendStr(serviceName, "frameex", FrameExCount.getCount());
        if (sendStr != null) {
          udp.send(frameExP.dataCreate(sendStr.getBytes()));
        }

        // 抛弃
        AbandonCount.initCount(0);
        Thread.sleep(1000);
        sendStr = getSendStr(serviceName, "abandon", AbandonCount.getCount());
        if (sendStr != null) {
          udp.send(abandonP.dataCreate(sendStr.getBytes()));
        }

        // jvm
        mjvm.jvmGc();
        mjvm.jvmGCUtil();
        mjvm.jvmThreadCount();
        mjvm.jvmMemory();
        mjvm.jvmHeapMemory();
        mjvm.jvmNoHeapMemory();
        mjvm.jvmLoad();
      } catch (Exception ex) {
        logger.error("control method error", ex);
      }
    }
  }

  /**
   * 具体ip访问次数
   * 
   */
  public static void getMaxCount(Map<String, Integer> map, String serviceName) {
    MonitorProtocol protocol = new MonitorProtocol(MonitorType.count, (short) 0);
    Iterator<Entry<String, Integer>> it = map.entrySet().iterator();
    while (it.hasNext()) {
      Entry<String, Integer> entry = it.next();
      String ip = entry.getKey();
      Integer count = entry.getValue();
      StringBuffer sb = new StringBuffer();
      sb.append("ip:\t");
      sb.append((String) ip);
      sb.append("\t");
      sb.append(count);
      sb.append("\t");
      sb.append(serviceName);
      try {
        udp.send(protocol.dataCreate(sb.toString().getBytes()));
      } catch (IOException e) {
        e.printStackTrace();
      } catch (SerializeException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * 构造发送字符串
   * 
   */
  public static String getSendStr(String serviceName, String sendType, int count) {
    StringBuffer sb = new StringBuffer();
    sb.append(sendType);
    sb.append("\t");
    sb.append(count);
    sb.append("\t");
    sb.append(serviceName);
    return sb.toString();
  }

}