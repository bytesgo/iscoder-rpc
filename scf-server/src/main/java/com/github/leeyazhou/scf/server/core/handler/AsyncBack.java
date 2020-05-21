package com.github.leeyazhou.scf.server.core.handler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.leeyazhou.scf.core.exception.TimeoutException;
import com.github.leeyazhou.scf.core.utils.spat.async.AsyncInvoker;
import com.github.leeyazhou.scf.core.utils.spat.async.IAsyncHandler;
import com.github.leeyazhou.scf.protocol.sdp.ResponseProtocol;
import com.github.leeyazhou.scf.protocol.sfp.Protocol;
import com.github.leeyazhou.scf.server.contract.context.ExecFilterType;
import com.github.leeyazhou.scf.server.contract.context.Global;
import com.github.leeyazhou.scf.server.contract.context.SCFContext;
import com.github.leeyazhou.scf.server.contract.context.SCFResponse;
import com.github.leeyazhou.scf.server.contract.context.SecureContext;
import com.github.leeyazhou.scf.server.contract.context.ServerType;
import com.github.leeyazhou.scf.server.contract.http.HttpThreadLocal;
import com.github.leeyazhou.scf.server.filter.IFilter;
import com.github.leeyazhou.scf.server.performance.monitorweb.AbandonCount;
import com.github.leeyazhou.scf.server.util.ExceptionUtil;

public class AsyncBack {

  private static Logger logger = LoggerFactory.getLogger(AsyncBack.class);
  private static AsyncBack asyn = null;
  private static int taskTimeOut = 1000;
  private static HttpThreadLocal httpThreadLocal;
  public static Map<String, Integer> asynMap = new ConcurrentHashMap<String, Integer>();
  public static final int THREAD_COUNT = Runtime.getRuntime().availableProcessors();
  private static AsyncInvoker asyncInvoker = AsyncInvoker.getInstance(THREAD_COUNT, false, "Back Async Worker");
  public static Map<Integer, SCFContext> contextMap = new ConcurrentHashMap<Integer, SCFContext>();
  public static final CallBackUtil callBackUtil = new CallBackUtil();

  static {
    try {
      httpThreadLocal = HttpThreadLocal.getInstance();
      String sTaskTimeOut = Global.getSingleton().getServiceConfig().getString("back.task.timeout");
      if (sTaskTimeOut != null && !"".equals(sTaskTimeOut)) {
        taskTimeOut = Integer.parseInt(sTaskTimeOut);
      }
      logger.info("back async worker count:" + THREAD_COUNT);
    } catch (Exception e) {
      logger.error("init AsyncInvokerHandle error", e);
    }
  }

  private AsyncBack() {

  }

  public static AsyncBack getAsynBack() {
    return asyn != null ? asyn : new AsyncBack();
  }

  public static void send(final int key, final Object obj) {

    final SCFContext context = contextMap.get(key);
    if (null == context) {
      return;
    }
    synchronized (context) {
      if (null == context || context.isDel()) {
        return;
      }
      context.setDel(true);
    }

    asyncInvoker.run(taskTimeOut, new IAsyncHandler() {
      @Override
      public Object run() throws Throwable {
        if (obj instanceof Exception) {
          exceptionCaught((Throwable) obj);
          return null;
        }
        Protocol protocol = context.getScfRequest().getProtocol();
        SCFResponse response = new SCFResponse(obj, null);

        protocol.setSdpEntity(new ResponseProtocol(response.getReturnValue(), null));

        for (IFilter f : Global.getSingleton().getGlobalResponseFilterList()) {
          if (context.getExecFilter() == ExecFilterType.All || context.getExecFilter() == ExecFilterType.ResponseOnly) {
            f.filter(context);
          }
        }
        return context;
      }

      @Override
      public void messageReceived(Object obj) {
        if (obj != null) {
          SCFContext ctx = (SCFContext) obj;
          if (ctx.isAsyn()) {
            ctx.getServerHandler().writeResponse(ctx);
            // contextMap.remove(key);//使用完删除context
          } else {
            logger.error("The Method is Synchronized!");
          }
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
          protocol.setSdpEntity(ExceptionUtil.createError(e));
          context.getScfResponse().setResponseBuffer(protocol.toBytes(Global.getSingleton().getGlobalSecureIsRights(), desKeyByte));
        } catch (Exception ex) {
          context.getScfResponse().setResponseBuffer(new byte[] { 0 });
          logger.error("AsyncInvokerHandle invoke-exceptionCaught error", ex);
        } finally {
          context.getServerHandler().writeResponse(context);
          logger.error("AsyncInvokerHandle invoke error", e);
        }
      }
    });
  }

}
