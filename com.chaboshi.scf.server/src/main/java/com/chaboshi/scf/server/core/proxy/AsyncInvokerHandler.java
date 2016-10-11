package com.chaboshi.scf.server.core.proxy;

import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chaboshi.scf.protocol.sfp.v1.Protocol;
import com.chaboshi.scf.server.contract.context.ExecFilterType;
import com.chaboshi.scf.server.contract.context.Global;
import com.chaboshi.scf.server.contract.context.SCFContext;
import com.chaboshi.scf.server.contract.context.SCFResponse;
import com.chaboshi.scf.server.contract.context.SecureContext;
import com.chaboshi.scf.server.contract.context.ServerType;
import com.chaboshi.scf.server.contract.filter.IFilter;
import com.chaboshi.scf.server.contract.http.HttpThreadLocal;
import com.chaboshi.scf.server.performance.monitorweb.AbandonCount;
import com.chaboshi.scf.server.util.ExceptionHelper;
import com.chaboshi.spat.utility.async.AsyncInvoker;
import com.chaboshi.spat.utility.async.IAsyncHandler;

/**
 * async service invoke handle
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 * 
 *         <a href="http://blog.58.com/spat/">blog</a> <a href="http://www.58.com">website</a>
 * 
 */
public class AsyncInvokerHandler extends InvokerBase {
  private static Logger logger = LoggerFactory.getLogger(AsyncInvokerHandler.class);
  /**
   * 异步执行器
   */
  private AsyncInvoker asyncInvoker;
  private HttpThreadLocal httpThreadLocal;
  private int taskTimeOut = 1000;

  public AsyncInvokerHandler() {
    try {
      httpThreadLocal = HttpThreadLocal.getInstance();
      int workerCount = Global.getSingleton().getServiceConfig().getInt("scf.async.worker.count");
      if (workerCount > 0) {
        asyncInvoker = AsyncInvoker.getInstance(workerCount, false, "Scf Async worker");
      } else {
        asyncInvoker = AsyncInvoker.getInstance();
      }
      String sTaskTimeOut = Global.getSingleton().getServiceConfig().getString("scf.server.tcp.task.timeout");
      if (sTaskTimeOut != null && !"".equals(sTaskTimeOut)) {
        taskTimeOut = Integer.parseInt(sTaskTimeOut);
      }
      logger.info("async worker count:" + workerCount);
    } catch (Exception e) {
      logger.error("init AsyncInvokerHandle error", e);
    }
  }

  @Override
  public void invoke(final SCFContext context) throws Exception {
    logger.debug("-------------------begin async invoke-------------------");

    asyncInvoker.run(taskTimeOut, new IAsyncHandler() {
      @Override
      public Object run() throws Throwable {
        logger.debug("begin request filter");
        // request filter

        for (IFilter f : Global.getSingleton().getGlobalRequestFilterList()) {
          if (context.getExecFilter() == ExecFilterType.All || context.getExecFilter() == ExecFilterType.RequestOnly) {
            f.filter(context);
          }
        }

        if (context.isDoInvoke()) {
          if (context.getServerType() == ServerType.HTTP) {
            httpThreadLocal.set(context.getHttpContext());
          }
          doInvoke(context);
        }
        if (context.isAsyn()) {
          return context;
        }
        logger.debug("begin response filter");
        // response filter
        for (IFilter f : Global.getSingleton().getGlobalResponseFilterList()) {
          if (context.getExecFilter() == ExecFilterType.All || context.getExecFilter() == ExecFilterType.ResponseOnly) {
            f.filter(context);
          }
        }
        return context;
      }

      @Override
      public void messageReceived(Object obj) {
        try {
          if (context.getServerType() == ServerType.HTTP) {
            httpThreadLocal.remove();
          }
          if (obj != null) {
            SCFContext ctx = (SCFContext) obj;
            if (!ctx.isAsyn()) {
              ctx.getServerHandler().writeResponse(ctx);
            }
          } else {
            logger.error("context is null!");
          }
        } finally {
          SCFContext.removeThreadLocal();
        }
      }

      @Override
      public void exceptionCaught(Throwable e) {
        try {
          if (context.getServerType() == ServerType.HTTP) {
            httpThreadLocal.remove();
          }

          if (context.getScfResponse() == null) {
            SCFResponse respone = new SCFResponse();
            context.setScfResponse(respone);
          }

          // 任务超时计数
          if (e instanceof TimeoutException) {
            try {
              AbandonCount.messageRecv();
            } catch (Exception ex) {
              ex.printStackTrace();
            }
          }

          byte[] desKeyByte = null;
          String desKeyStr = null;
          boolean bool = false;

          Global global = Global.getSingleton();
          if (global != null) {
            // 判断当前服务启用权限认证
            if (global.getGlobalSecureIsRights()) {
              SecureContext securecontext = global.getGlobalSecureContext(context.getChannel().getNettyChannel());
              bool = securecontext.isRights();
              if (bool) {
                desKeyStr = securecontext.getDesKey();
              }
            }
          }

          if (desKeyStr != null) {
            desKeyByte = desKeyStr.getBytes("utf-8");
          }

          Protocol protocol = context.getScfRequest().getProtocol();
          if (protocol == null) {
            protocol = Protocol.fromBytes(context.getScfRequest().getRequestBuffer(), global.getGlobalSecureIsRights(), desKeyByte);
            context.getScfRequest().setProtocol(protocol);
          }
          protocol.setSdpEntity(ExceptionHelper.createError(e));
          context.getScfResponse().setResponseBuffer(protocol.toBytes(Global.getSingleton().getGlobalSecureIsRights(), desKeyByte));
        } catch (Exception ex) {
          context.getScfResponse().setResponseBuffer(new byte[] { 0 });
          logger.error("AsyncInvokerHandle invoke-exceptionCaught error", ex);
        } finally {
          SCFContext.removeThreadLocal();
          logger.error("AsynBack.contextMap.remove " + context.getSessionID());
          AsynBack.contextMap.remove(context.getSessionID());
        }

        context.getServerHandler().writeResponse(context);

        logger.error("AsyncInvokerHandle invoke error", e);
      }
    });
  }
}