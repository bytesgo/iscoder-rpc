package com.chaboshi.scf.server.performance.commandhelper;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.MessageEvent;

import com.chaboshi.scf.server.contract.context.SCFContext;
import com.chaboshi.scf.server.performance.Command;
import com.chaboshi.scf.server.performance.CommandType;

public class CRLF extends CommandHelperBase {

  @Override
  public Command createCommand(String commandStr) {
    if (commandStr == null || commandStr.equalsIgnoreCase("")) {
      Command entity = new Command();
      entity.setCommandType(CommandType.CRLF);
      return entity;
    }
    return null;
  }

  @Override
  public void execCommand(Command command, MessageEvent event) throws Exception {
    if (command.getCommandType() == CommandType.CRLF) {
      logger.debug("cr & lf");
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
