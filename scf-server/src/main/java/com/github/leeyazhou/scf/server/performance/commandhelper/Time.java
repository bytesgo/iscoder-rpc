package com.github.leeyazhou.scf.server.performance.commandhelper;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.MessageEvent;

import com.github.leeyazhou.scf.server.contract.context.SCFContext;
import com.github.leeyazhou.scf.server.contract.context.StopWatch;
import com.github.leeyazhou.scf.server.performance.Command;
import com.github.leeyazhou.scf.server.performance.CommandType;
import com.github.leeyazhou.scf.server.performance.MonitorCenter;
import com.github.leeyazhou.scf.server.performance.MonitorChannel;
import com.github.leeyazhou.scf.server.performance.ShowColumn;

public class Time extends CommandHelperBase {

  private static List<MonitorChannel> taskList = new ArrayList<MonitorChannel>();

  @Override
  public Command createCommand(String commandStr) { // commandStr="time | grep
                                                    // getList"
    if (commandStr != null && !commandStr.equalsIgnoreCase("")) {
      String[] args = commandStr.split("\\|");
      if (args[0].trim().equalsIgnoreCase("time")) {
        List<String> grepList = new ArrayList<String>();
        List<ShowColumn> scList = new ArrayList<ShowColumn>();

        Command entity = new Command();
        scList.add(ShowColumn.All);
        entity.setCommandType(CommandType.Time);

        for (int i = 1; i < args.length; i++) {
          if (args[i].trim().startsWith("grep")) {
            grepList.add(args[i].trim().replaceFirst("grep ", "").trim());
          } else if (args[i].trim().startsWith("group")) {
            entity.setGroup(Integer.parseInt(args[i].trim().replaceFirst("group ", "").trim()));
          } else if (args[i].trim().startsWith("column")) {
            scList.clear();
            String cs = args[i].trim().replaceFirst("column -", "");
            if (cs.indexOf("a") >= 0) {
              if (!scList.contains(ShowColumn.All)) {
                scList.add(ShowColumn.All);
              }
            } else {
              String[] csAry = cs.split("");
              for (String item : csAry) {
                if (item.equalsIgnoreCase("t")) {
                  if (!scList.contains(ShowColumn.Time)) {
                    scList.add(ShowColumn.Time);
                  }
                } else if (item.equalsIgnoreCase("k")) {
                  if (!scList.contains(ShowColumn.Key)) {
                    scList.add(ShowColumn.Key);
                  }
                } else if (item.equalsIgnoreCase("d")) {
                  if (!scList.contains(ShowColumn.Description)) {
                    scList.add(ShowColumn.Description);
                  }
                }
              }
            }
          }
        }
        entity.setGrep(grepList);
        entity.setColumnList(scList);
        return entity;
      }
    }
    return null;
  }

  @SuppressWarnings("rawtypes")
  @Override
  public void messageReceived(SCFContext context) {
    if (taskList.size() <= 0) {
      return;
    }

    StopWatch sw = context.getStopWatch();
    if (sw != null) {
      Map<String, StopWatch.PerformanceCounter> mapCounter = sw.getMapCounter();
      Iterator itSW = mapCounter.entrySet().iterator();
      while (itSW.hasNext()) {
        Map.Entry entrySW = (Map.Entry) itSW.next();
        StopWatch.PerformanceCounter pc = (StopWatch.PerformanceCounter) entrySW.getValue();

        StringBuilder sbAllMsg = new StringBuilder();
        sbAllMsg.append("time:");
        sbAllMsg.append(pc.getEndTime() - pc.getStartTime());
        sbAllMsg.append("ms--key:");
        sbAllMsg.append(pc.getKey());
        sbAllMsg.append("--description:");
        sbAllMsg.append(pc.getDescription());

        try {
          String allMsg = sbAllMsg.toString();
          for (MonitorChannel mc : taskList) {
            /**
             * 修改grep
             * 
             * @author fanwb
             * @Data 2012-07-26
             */
            boolean match = true;
            for (String key : mc.getCommand().getGrep()) {
              if (allMsg.indexOf(key) == -1) {
                match = false;
              }

            }
            if (match) {
              sendMsg(mc, pc, sbAllMsg);
            }
          }
        } catch (UnsupportedEncodingException e) {
          logger.error("send monitor data", e);
        }
      }
    }
  }

  @Override
  public void execCommand(Command command, MessageEvent event) throws Exception {
    if (command.getCommandType() == CommandType.Time) {
      MonitorCenter.addFilter(); // add filter
      logger.info("add time monitor channel:" + event.getChannel().getRemoteAddress());
      for (int i = 0; i < taskList.size(); i++) {
        if (taskList.get(i).getChannel().equals(event.getChannel()) || !taskList.get(i).getChannel().isOpen()) {
          taskList.remove(i);
        }
      }
      taskList.add(new MonitorChannel(command, event.getChannel(), event.getChannel().getRemoteAddress()));
    }
  }

  @Override
  public void removeChannel(Command command, Channel channel) {
    if (command.getCommandType() != CommandType.Time) {
      for (int i = 0; i < taskList.size(); i++) {
        if (taskList.get(i).getChannel().equals(channel) || !taskList.get(i).getChannel().isOpen()) {
          taskList.remove(i);
        }
      }
    }
  }

  @Override
  public int getChannelCount() {
    for (int i = 0; i < taskList.size(); i++) {
      if (!taskList.get(i).getChannel().isOpen()) {
        taskList.remove(i);
      }
    }
    return taskList.size();
  }

  /**
   * 
   * @param mc
   * @param pc
   * @param sbAllMsg
   * @throws UnsupportedEncodingException
   */
  private void sendMsg(MonitorChannel mc, StopWatch.PerformanceCounter pc, StringBuilder sbAllMsg) throws UnsupportedEncodingException {
    if (mc.getCommand().getGroup() > 0) {
      if (mc.getConvergeCount() < mc.getCommand().getGroup()) {
        mc.setConvergeCount(mc.getConvergeCount() + 1);
        mc.setConvergeTime(mc.getConvergeTime() + (pc.getEndTime() - pc.getStartTime()));
      } else {
        StringBuilder sbSendMsg = new StringBuilder();
        sbSendMsg.append("group ");
        sbSendMsg.append(mc.getCommand().getGroup());
        sbSendMsg.append(" all time:");
        sbSendMsg.append(mc.getConvergeTime());
        sbSendMsg.append("--average time:");
        sbSendMsg.append(mc.getConvergeTime() / mc.getConvergeCount());
        sbSendMsg.append("\n\r");

        byte[] responseByte = sbSendMsg.toString().getBytes("utf-8");
        mc.getChannel().write(ChannelBuffers.copiedBuffer(responseByte));

        mc.setConvergeCount(0);
        mc.setConvergeTime(0L);
      }
    } else {
      StringBuilder sbSendMsg = new StringBuilder();
      for (ShowColumn sc : mc.getCommand().getColumnList()) {
        if (sc == ShowColumn.All) {
          sbSendMsg = sbAllMsg;
          break;
        } else if (sc == ShowColumn.Time) {
          sbSendMsg.append("time:");
          sbSendMsg.append(pc.getEndTime() - pc.getStartTime());
        } else if (sc == ShowColumn.Key) {
          sbSendMsg.append("ms--key:");
          sbSendMsg.append(pc.getKey());
        } else if (sc == ShowColumn.Description) {
          sbSendMsg.append("--description:");
          sbSendMsg.append(pc.getDescription());
        }
      }
      sbSendMsg.append("\n\r");
      byte[] responseByte = sbSendMsg.toString().getBytes("utf-8");
      mc.getChannel().write(ChannelBuffers.copiedBuffer(responseByte));
    }
  }
}