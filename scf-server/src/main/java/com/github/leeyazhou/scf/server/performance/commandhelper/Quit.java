package com.github.leeyazhou.scf.server.performance.commandhelper;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.MessageEvent;

import com.github.leeyazhou.scf.server.contract.context.SCFContext;
import com.github.leeyazhou.scf.server.performance.Command;
import com.github.leeyazhou.scf.server.performance.CommandType;

public class Quit extends CommandHelperBase {

  @Override
  public Command createCommand(String commandStr) {
    if (commandStr != null && !commandStr.equalsIgnoreCase("")) {
      if (commandStr.equalsIgnoreCase("quit")) {
        Command entity = new Command();
        entity.setCommandType(CommandType.Quit);
        return entity;
      }
    }
    return null;
  }

  @Override
  public void execCommand(Command command, MessageEvent event) throws Exception {
    if (command.getCommandType() == CommandType.Quit) {
      logger.info("quit monitor");
      event.getChannel().close();
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