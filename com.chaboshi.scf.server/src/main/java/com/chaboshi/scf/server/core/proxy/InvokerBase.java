package com.chaboshi.scf.server.core.proxy;

import com.chaboshi.scf.protocol.sdp.RequestProtocol;
import com.chaboshi.scf.protocol.sdp.ResponseProtocol;
import com.chaboshi.scf.protocol.sfp.v1.Protocol;
import com.chaboshi.scf.protocol.utility.KeyValuePair;
import com.chaboshi.scf.server.contract.context.Global;
import com.chaboshi.scf.server.contract.context.IProxyStub;
import com.chaboshi.scf.server.contract.context.SCFContext;
import com.chaboshi.scf.server.contract.context.SCFResponse;
import com.chaboshi.scf.server.contract.context.StopWatch;
import com.chaboshi.scf.server.contract.log.ILog;
import com.chaboshi.scf.server.contract.log.LogFactory;
import com.chaboshi.scf.server.performance.monitorweb.FrameExCount;
import com.chaboshi.scf.server.util.ErrorState;
import com.chaboshi.scf.server.util.ExceptionHelper;
import com.chaboshi.scf.server.util.ServiceFrameException;
import com.chaboshi.scf.server.util.SystemUtils;

public abstract class InvokerBase implements IInvokerHandle {

  /**
   * log
   */
  private final static ILog logger = LogFactory.getLogger(InvokerBase.class);

  /**
   * 调用真实服务
   * 
   * @param context
   */
  void doInvoke(SCFContext context) {
    logger.debug("------------------------------ begin request-----------------------------");

    StringBuffer sbInvokerMsg = new StringBuffer();
    StringBuffer sbIsAsynMsg = new StringBuffer();
    StopWatch sw = context.getStopWatch();
    Object response = null;
    Protocol protocol = null;

    try {
      protocol = context.getScfRequest().getProtocol();
      RequestProtocol request = (RequestProtocol) protocol.getSdpEntity();
      logger.debug(request.toString());
      sbInvokerMsg.append("protocol version:");
      sbInvokerMsg.append(protocol.getVersion());
      sbInvokerMsg.append("\nfromIP:");
      sbInvokerMsg.append(context.getChannel().getRemoteIP());
      sbInvokerMsg.append("\nlookUP:");
      sbInvokerMsg.append(request.getLookup());
      sbIsAsynMsg.append(request.getLookup());
      sbInvokerMsg.append("\nmethodName:");
      sbInvokerMsg.append(request.getMethodName());
      sbIsAsynMsg.append(request.getMethodName());
      sbInvokerMsg.append("\nparams:");

      if (request.getParaKVList() != null) {
        for (KeyValuePair kv : request.getParaKVList()) {
          if (kv != null) {
            sbInvokerMsg.append("\n--key:");
            sbInvokerMsg.append(kv.getKey());
            sbIsAsynMsg.append(kv.getKey());
            sbInvokerMsg.append("\n--value:");
            sbInvokerMsg.append(kv.getValue());
          } else {
            logger.error("KeyValuePair is null  Lookup:" + request.getLookup() + "--MethodName:" + request.getMethodName());
          }
        }
      }

      logger.debug(sbInvokerMsg.toString());
      logger.debug(sbIsAsynMsg.toString());
      logger.debug("begin get proxy factory");

      // get local proxy
      IProxyStub localProxy = Global.getSingleton().getProxyFactory().getProxy(request.getLookup());
      logger.debug("proxyFactory.getProxy finish");
      if (localProxy == null) {
        ServiceFrameException sfe = new ServiceFrameException(
            "method:ProxyHandle.invoke--msg:" + request.getLookup() + "." + request.getMethodName() + " not fond",
            context.getChannel().getRemoteIP(), context.getChannel().getLocalIP(), request, ErrorState.NotFoundServiceException, null);
        response = ExceptionHelper.createError(sfe);
        logger.error("localProxy is null", sfe);
      } else {
        logger.debug("begin localProxy.invoke");
        String swInvoderKey = "InvokeRealService_" + request.getLookup() + "." + request.getMethodName();
        sw.startNew(swInvoderKey, sbInvokerMsg.toString());
        sw.setFromIP(context.getChannel().getRemoteIP());
        sw.setLocalIP(context.getChannel().getLocalIP());

        if (AsynBack.asynMap.containsKey(sbIsAsynMsg.toString())) {
          int sessionid = SystemUtils.createSessionId();
          // int sessionid =
          // context.getScfRequest().getProtocol().getSessionID();
          SCFContext.setThreadLocal(context);
          context.setAsyn(true);
          AsynBack.contextMap.put(sessionid, context);
          AsynBack.callBackUtil.offer(new WData(sessionid, System.currentTimeMillis()));
        }

        // invoker real service
        SCFResponse scfResponse = localProxy.invoke(context);

        if (context.isAsyn()) {
          return;
        }

        sw.stop(swInvoderKey);

        logger.debug("end localProxy.invoke");
        context.setScfResponse(scfResponse);
        response = createResponse(scfResponse);
        logger.debug("localProxy.invoke finish");
      }
    } catch (ServiceFrameException sfe) {
      logger.error("ServiceFrameException when invoke service fromIP:" + context.getChannel().getRemoteIP() + "  toIP:"
          + context.getChannel().getLocalIP(), sfe);
      response = ExceptionHelper.createError(sfe);
      context.setError(sfe);
      FrameExCount.messageRecv();
      SCFContext.removeThreadLocal();
    } catch (Throwable e) {
      logger.error(
          "Exception when invoke service fromIP:" + context.getChannel().getRemoteIP() + "  toIP:" + context.getChannel().getLocalIP(), e);
      response = ExceptionHelper.createError(e);
      context.setError(e);
      SCFContext.removeThreadLocal();
    }

    protocol.setSdpEntity(response);
    logger.debug("---------------------------------- end --------------------------------");
  }

  /**
   * create response message body
   * 
   * @param scfResponse
   * @return
   */
  ResponseProtocol createResponse(SCFResponse scfResponse) {
    if (scfResponse.getOutParaList() != null && scfResponse.getOutParaList().size() > 0) {
      int outParaSize = scfResponse.getOutParaList().size();
      Object[] objArray = new Object[outParaSize];
      for (int i = 0; i < outParaSize; i++) {
        objArray[i] = scfResponse.getOutParaList().get(i).getOutPara();
      }
      return new ResponseProtocol(scfResponse.getReturnValue(), objArray);
    } else {
      return new ResponseProtocol(scfResponse.getReturnValue(), null);
    }
  }
}