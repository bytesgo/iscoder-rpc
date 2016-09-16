package com.chaboshi.scf.server.core.proxy;

import com.chaboshi.scf.server.contract.context.ExecFilterType;
import com.chaboshi.scf.server.contract.context.Global;
import com.chaboshi.scf.server.contract.context.SCFContext;
import com.chaboshi.scf.server.contract.filter.IFilter;
import com.chaboshi.scf.server.contract.log.ILog;
import com.chaboshi.scf.server.contract.log.LogFactory;

/**
 * sync service invoke handle
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 * 
 *         <a href="http://blog.58.com/spat/">blog</a>
 *         <a href="http://www.58.com">website</a>
 * 
 */
public class SyncInvokerHandle extends InvokerBase {

  private static ILog logger = LogFactory.getLogger(SyncInvokerHandle.class);

  /**
   * create protocol and invoke service proxy
   */
  @Override
  public void invoke(SCFContext context) throws Exception {
    try {
      for (IFilter f : Global.getSingleton().getGlobalRequestFilterList()) {// request
                                                                            // 请求全局过滤
        if (context.getExecFilter() == ExecFilterType.All || context.getExecFilter() == ExecFilterType.RequestOnly) {
          f.filter(context);
        }
      }

      if (context.isDoInvoke()) {// 调用真实服务
        doInvoke(context);
      }

      logger.debug("begin response filter");
      // response filter
      for (IFilter f : Global.getSingleton().getGlobalResponseFilterList()) {// Response
                                                                             // 响应全局过滤
        if (context.getExecFilter() == ExecFilterType.All || context.getExecFilter() == ExecFilterType.ResponseOnly) {
          f.filter(context);
        }
      }
      context.getServerHandler().writeResponse(context);
    } catch (Exception ex) {
      context.setError(ex);
      context.getServerHandler().writeResponse(context);
      logger.error("in async messageReceived", ex);
      // MonitorErrorLog.sendError("in async messageReceived");//消息发送到监测者
    }
  }
}