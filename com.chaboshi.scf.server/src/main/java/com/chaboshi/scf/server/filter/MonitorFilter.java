package com.chaboshi.scf.server.filter;

import com.chaboshi.scf.server.contract.context.SCFContext;
import com.chaboshi.scf.server.contract.context.StopWatch;
import com.chaboshi.scf.server.contract.filter.IFilter;
import com.chaboshi.scf.server.contract.log.ILog;
import com.chaboshi.scf.server.contract.log.LogFactory;
import com.chaboshi.scf.server.performance.monitorweb.MonitorCount;

public class MonitorFilter implements IFilter {

  private static ILog logger = LogFactory.getLogger(MonitorFilter.class);

  @Override
  public int getPriority() {
    return 0;
  }

  @Override
  public void filter(SCFContext context) throws Exception {
    try {
      if (null != context) {
        StopWatch sw = context.getStopWatch();
        if (null != sw) {
          // 将sw发送到Monitor
          MonitorCount.messageRecv(sw);
        }
      }
    } catch (Exception mex) {
      logger.info("MonitorCount error" + mex);
    }

  }

}
