package com.github.leeyazhou.scf.server.performance.commandhelper;

import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.MessageEvent;

import com.github.leeyazhou.scf.server.contract.context.SCFContext;
import com.github.leeyazhou.scf.server.performance.Command;
import com.github.leeyazhou.scf.server.performance.CommandType;

public class Control extends CommandHelperBase {

  @Override
  public Command createCommand(String commandStr) {
    if (commandStr != null && !commandStr.equalsIgnoreCase("")) {
      if (commandStr.equalsIgnoreCase("control")) {
        Command entity = new Command();
        entity.setCommandType(CommandType.Control);
        return entity;
      }
    }
    return null;
  }

  @Override
  public void execCommand(Command command, MessageEvent event) throws Exception {
    if (command.getCommandType() == CommandType.Control) {
      String msg = "error: at present not allow\r\n";
      byte[] responseByte = msg.getBytes("utf-8");
      event.getChannel().write(ChannelBuffers.copiedBuffer(responseByte));
    }
  }

  @Override
  public void messageReceived(SCFContext context) {
    // do nothing
  }

  @Override
  public void removeChannel(Command command, Channel channel) {
    // do nothing
  }

  @Override
  public int getChannelCount() {
    return 0;
  }
}