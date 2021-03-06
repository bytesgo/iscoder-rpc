package com.github.leeyazhou.scf.server.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.leeyazhou.scf.server.contract.context.SCFContext;
import com.github.leeyazhou.scf.server.contract.context.StopWatch;
import com.github.leeyazhou.scf.server.performance.monitorweb.MonitorCount;

public class MonitorFilter implements IFilter {

  private static final Logger logger = LoggerFactory.getLogger(MonitorFilter.class);

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
