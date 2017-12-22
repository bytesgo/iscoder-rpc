package com.iscoder.scf.server.performance;

import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.MessageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iscoder.scf.server.IFilter;
import com.iscoder.scf.server.contract.context.Global;
import com.iscoder.scf.server.contract.context.SCFContext;
import com.iscoder.scf.server.filter.MonitorRequestFilter;
import com.iscoder.scf.server.filter.MonitorResponseFilter;

public class MonitorCenter {
  private static final Logger logger = LoggerFactory.getLogger(MonitorCenter.class);
  /**
   * thread pool
   */
  private static ExecutorService executor = Executors.newCachedThreadPool();

  /**
   * MonitorRequestFilter
   */
  private static IFilter monitorRequestFilter = new MonitorRequestFilter();

  /**
   * MonitorResponseFilter
   */
  private static IFilter monitorResponseFilter = new MonitorResponseFilter();

  private static Command command = null;

  /**
   * monitor event receive
   * 
   * @param e
   * @throws Exception
   * 
   */
  public static void messageReceived(MessageEvent e) throws Exception {
    ByteBuffer buffer = ((ChannelBuffer) e.getMessage()).toByteBuffer();
    byte[] reciveByte = buffer.array();
    String msg = new String(reciveByte, "utf-8");
    if (!msg.equals("|") && !msg.equals("-")) {
      command = Command.create(msg);
    }
    logger.info("command:" + msg + "--commandType:" + command.getCommandType());
    command.exec(e);

    removeChannel(e.getChannel());
  }

  /**
   * add monitor filter
   */
  public synchronized static void addFilter() {
    if (!Global.getSingleton().getGlobalRequestFilterList().contains(monitorRequestFilter)) {
      logger.info("add monitorRequestFilter");
      Global.getSingleton().addGlobalRequestFilter(monitorRequestFilter);
    }
    if (!Global.getSingleton().getGlobalResponseFilterList().contains(monitorResponseFilter)) {
      logger.info("add monitorResponseFilter");
      Global.getSingleton().addGlobalResponseFilter(monitorResponseFilter);
    }
  }

  /**
   * remove monitor filter
   */
  public synchronized static void removeFilter() {
    Global.getSingleton().removeGlobalRequestFilter(monitorRequestFilter);
    Global.getSingleton().removeGlobalResponseFilter(monitorResponseFilter);
    logger.info("remove monitorRequestFilter");
    logger.info("remove monitorResponseFilter");
  }

  /**
   * add monitor task
   * 
   * @param context
   */
  public static void addMonitorTask(final SCFContext context) {
    executor.execute(new Runnable() {
      public void run() {
        if (command != null) {
          command.messageReceived(context);
        }
      }
    });
  }

  public static void removeChannel(Channel channel) {
    if (command != null) {
      command.removeChannel(channel);

      if (command.getChannelCount() <= 0) {
        removeFilter();
      }
    }
  }
}