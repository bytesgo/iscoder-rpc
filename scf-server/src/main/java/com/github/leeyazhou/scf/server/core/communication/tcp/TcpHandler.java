package com.github.leeyazhou.scf.server.core.communication.tcp;

import java.nio.ByteBuffer;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandler.Sharable;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.leeyazhou.scf.protocol.ProtocolConst;
import com.github.leeyazhou.scf.server.IFilter;
import com.github.leeyazhou.scf.server.contract.context.Global;
import com.github.leeyazhou.scf.server.contract.context.SCFChannel;
import com.github.leeyazhou.scf.server.contract.context.SCFContext;
import com.github.leeyazhou.scf.server.contract.context.SecureContext;
import com.github.leeyazhou.scf.server.contract.context.ServerType;
import com.github.leeyazhou.scf.server.core.communication.ServerHandler;
import com.github.leeyazhou.scf.server.util.ExceptionUtil;

/**
 * netty event handler
 * 
 * 
 */
@Sharable
public class TcpHandler extends SimpleChannelUpstreamHandler implements ServerHandler {

  private static Logger logger = LoggerFactory.getLogger(TcpHandler.class);

  @Override
  public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
    try {
      logger.debug("message receive");
      ByteBuffer buffer = ((ChannelBuffer) e.getMessage()).toByteBuffer();
      byte[] reciveByte = buffer.array();
      logger.debug("reciveByte.length:" + reciveByte.length);
      // check head delimiter
      byte[] headDelimiter = new byte[ProtocolConst.P_START_TAG.length];
      System.arraycopy(reciveByte, 0, headDelimiter, 0, ProtocolConst.P_START_TAG.length);

      if (ProtocolConst.checkHeadDelimiter(headDelimiter)) {
        byte[] requestBuffer = new byte[reciveByte.length - ProtocolConst.P_START_TAG.length];
        System.arraycopy(reciveByte, ProtocolConst.P_START_TAG.length, requestBuffer, 0,
            (reciveByte.length - ProtocolConst.P_START_TAG.length));

        SCFContext scfContext = new SCFContext(requestBuffer, new SCFChannel(e.getChannel()), ServerType.TCP, this);

        TcpServer.handler.invoke(scfContext);
      } else {
        byte[] response = ExceptionUtil.createErrorProtocol();
        e.getChannel().write(ChannelBuffers.copiedBuffer(response));
        logger.error("protocol error: protocol head not match");
      }
    } catch (Throwable ex) {
      byte[] response = ExceptionUtil.createErrorProtocol();
      e.getChannel().write(response);
      logger.error("SocketHandler invoke error", ex);

    }
  }

  @Override
  public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e) throws Exception {
    if (e instanceof ChannelStateEvent) {
      logger.debug(e.toString());
    }
    super.handleUpstream(ctx, e);
  }

  @Override
  public void channelOpen(ChannelHandlerContext ctx, ChannelStateEvent e) {
    TcpServer.allChannels.add(e.getChannel());
    logger.info("new channel open:" + e.getChannel().getRemoteAddress().toString());
    /**
     * 如果当前服务启动权限认证,则增加当前连接对应的SecureContext
     * 
     * @author HaoXB
     */
    if (Global.getSingleton().getGlobalSecureIsRights()) {// Channel
      Global.getSingleton().addChannelMap(e.getChannel(), new SecureContext());
    }

  }

  @Override
  public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
    for (IFilter filter : Global.getSingleton().getConnectionFilterList()) {
      filter.filter(new SCFContext(new SCFChannel(e.getChannel())));
    }
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
    logger.error("unexpected exception from downstream remoteAddress(" + e.getChannel().getRemoteAddress().toString() + ")", e.getCause());

    // e.getChannel().close();
  }

  @Override
  public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) {
    logger.info("channel is closed:" + e.getChannel().getRemoteAddress().toString());
    e.getChannel().close();

    /**
     * 如果当前服务启动权限认证,则删除当前连接对应的SecureContext
     * 
     * @author HaoXB
     */
    if (Global.getSingleton().getGlobalSecureIsRights()) {
      Global.getSingleton().removeChannelMap(e.getChannel());
    }
  }

  @Override
  public void writeResponse(SCFContext context) {
    if (context != null && context.getScfResponse() != null) {
      context.getChannel().write(context.getScfResponse().getResponseBuffer());
    } else {
      context.getChannel().write(new byte[] { 0 });
      logger.error("context is null or response is null in writeResponse");
    }
  }
}