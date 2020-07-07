package com.github.leeyazhou.scf.server.core.communication.tcp;

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
import com.github.leeyazhou.scf.server.contract.context.Global;
import com.github.leeyazhou.scf.server.core.communication.Server;
import com.github.leeyazhou.scf.server.core.handler.Handler;

/**
 * start netty server
 * 
 */
public class TcpServer implements Server {
  private static final Logger logger = LoggerFactory.getLogger(TcpServer.class);

  public TcpServer() {}

  /**
   * netty ServerBootstrap
   */
  private final ServerBootstrap bootstrap = new ServerBootstrap();

  /**
   * record all channel
   */
  static final ChannelGroup allChannels = new DefaultChannelGroup("SCF-SockerServer");

  /**
   * invoker handle
   */
  static Handler handler = null;

  /**
   * start netty server
   */
  @Override
  public void start() throws Exception {
    logger.info("loading invoker...");
    String invoker = Global.getSingleton().getServiceConfig().getString("scf.proxy.invoker.implement");
    handler = (Handler) Class.forName(invoker).newInstance();
    logger.info("initing server...");
    initSocketServer();
  }

  /**
   * stop netty server
   */
  @Override
  public void stop() throws Exception {
    logger.info("socket server closing...");
    logger.info("channels count : " + allChannels.size());

    ChannelGroupFuture future = allChannels.close(); // close all channel
    logger.info("closing all channels...");
    future.awaitUninterruptibly(); // wait for channel close
    logger.info("closed all channels...");
    bootstrap.getFactory().releaseExternalResources(); // release external
                                                       // resources
    logger.info("released external resources");
    logger.info("close success !");
  }

  /**
   * 初始化socket server
   * 
   * @throws Exception
   */
  private void initSocketServer() throws Exception {
    final boolean tcpNoDelay = true;
    final String ip = Global.getSingleton().getServiceConfig().getString("scf.server.tcp.listenIP");
    final int port = Global.getSingleton().getServiceConfig().getInt("scf.server.tcp.listenPort");
    final int workerCount = Global.getSingleton().getServiceConfig().getInt("scf.server.tcp.workerCount");
    final int frameMaxLength = Global.getSingleton().getServiceConfig().getInt("scf.server.tcp.frameMaxLength");
    final int receiveBufferSize = Global.getSingleton().getServiceConfig().getInt("scf.server.tcp.receiveBufferSize");
    final int sendBufferSize = Global.getSingleton().getServiceConfig().getInt("scf.server.tcp.sendBufferSize");
    logger.info("listen ip: " + ip);
    logger.info("port: " + port);
    logger.info("tcpNoDelay: " + tcpNoDelay);
    logger.info("receiveBufferSize: " + receiveBufferSize);
    logger.info("sendBufferSize: " + sendBufferSize);
    logger.info("frameMaxLength: " + frameMaxLength);
    logger.info("worker thread count: " + workerCount);

    logger.info(Global.getSingleton().getServiceConfig().getServiceName() + " SocketServer starting...");

    bootstrap.setFactory(new NioServerSocketChannelFactory(Executors.newCachedThreadPool(),
        Executors.newCachedThreadPool(), workerCount));

    final TcpHandler handler = new TcpHandler();

    bootstrap.setPipelineFactory(new TcpPipelineFactory(handler, frameMaxLength));
    bootstrap.setOption("child.tcpNoDelay", tcpNoDelay);
    bootstrap.setOption("child.receiveBufferSize", receiveBufferSize);
    bootstrap.setOption("child.sendBufferSize", sendBufferSize);

    try {
      InetSocketAddress socketAddress = new InetSocketAddress(ip, port);
      Channel channel = bootstrap.bind(socketAddress);
      allChannels.add(channel);
    } catch (Exception e) {
      logger.error("init socket server error", e);
      System.exit(1);
    }
  }
}
