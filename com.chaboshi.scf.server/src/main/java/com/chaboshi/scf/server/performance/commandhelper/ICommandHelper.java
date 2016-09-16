package com.chaboshi.scf.server.performance.commandhelper;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.MessageEvent;

import com.chaboshi.scf.server.contract.context.SCFContext;
import com.chaboshi.scf.server.performance.Command;

public interface ICommandHelper {
  public Command createCommand(String commandStr);

  public void execCommand(Command command, MessageEvent event) throws Exception;

  public void messageReceived(SCFContext context);

  public void removeChannel(Command command, Channel channel);

  public int getChannelCount();
}