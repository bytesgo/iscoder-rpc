package com.chaboshi.scf.server.core.communication.telnet;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.ChannelGroupFuture;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chaboshi.scf.server.contract.context.Global;
import com.chaboshi.scf.server.core.Server;

/**
 * start netty server
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 * 
 *         <a href="http://blog.58.com/spat/">blog</a> <a href="http://www.58.com">website</a>
 * 
 */
public class TelnetServer implements Server {

  private static Logger logger = LoggerFactory.getLogger(TelnetServer.class);

  public TelnetServer() {
  }

  /**
   * netty ServerBootstrap
   */
  private static final ServerBootstrap bootstrap = new ServerBootstrap();

  /**
   * record all channel
   */
  public static final ChannelGroup allChannels = new DefaultChannelGroup("58ControlServer");

  // /**
  // * request filter
  // */
  // public static List<IFilter> requestFilter = new ArrayList<IFilter>();
  //
  // /**
  // * response filter
  // */
  // public static List<IFilter> responseFilter = new ArrayList<IFilter>();

  /**
   * start netty server
   */
  @Override
  public void start() throws Exception {

    String telnetIP = Global.getSingleton().getServiceConfig().getString("scf.server.telnet.listenIP");
    int telnetPort = Global.getSingleton().getServiceConfig().getInt("scf.server.telnet.listenPort");
    if (telnetIP == null || telnetIP.equalsIgnoreCase("") || telnetIP.equalsIgnoreCase("0.0.0.0")) {
      telnetIP = Global.getSingleton().getServiceConfig().getString("scf.server.tcp.listenIP");
    }
    if (telnetPort == 0) {
      int port = Global.getSingleton().getServiceConfig().getInt("scf.server.tcp.listenPort");
      telnetPort = Reverse(Reverse(port) + 1);
    }
    logger.info("----------------telnet server config------------------");
    logger.info("-- telnet server listen ip: " + telnetIP);
    logger.info("-- telnet server port: " + telnetPort);
    logger.info("------------------------------------------------------");

    bootstrap.setFactory(new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));

    TelnetHandler handler = new TelnetHandler();

    bootstrap.setPipelineFactory(
        new TelnetPipelineFactory(handler, Global.getSingleton().getServiceConfig().getInt("scf.server.telnet.frameMaxLength")));
    bootstrap.setOption("child.tcpNoDelay", true);
    bootstrap.setOption("child.receiveBufferSize", Global.getSingleton().getServiceConfig().getInt("scf.server.telnet.receiveBufferSize"));
    bootstrap.setOption("child.sendBufferSize", Global.getSingleton().getServiceConfig().getInt("scf.server.telnet.sendBufferSize"));

    try {
      InetSocketAddress socketAddress = null;
      socketAddress = new InetSocketAddress(telnetIP, telnetPort);
      Channel channel = bootstrap.bind(socketAddress);
      allChannels.add(channel);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * stop netty server
   */
  @Override
  public void stop() throws Exception {
    logger.info("----------------------------------------------------");
    logger.info("-- telnet Server closing...");
    logger.info("-- channels count : " + allChannels.size());
    ChannelGroupFuture future = allChannels.close(); // close all channel
    future.awaitUninterruptibly(); // wait for channel close

    bootstrap.getFactory().releaseExternalResources(); // release external
                                                       // resources

    logger.info("-- close success !");
    logger.info("----------------------------------------------------");
  }

  // @Override
  // public synchronized void addRequestFilter(IFilter filter) {
  // if(!requestFilter.contains(filter)) {
  // requestFilter.add(filter);
  // }
  // }
  //
  //
  // @Override
  // public synchronized void addResponseFilter(IFilter filter) {
  // if(!responseFilter.contains(filter)) {
  // responseFilter.add(filter);
  // }
  // }
  public static int Reverse(int num) {
    int re = 0;
    for (; num != 0; num /= 10) {
      re = re * 10 + num % 10;
    }
    return re;
  }
}