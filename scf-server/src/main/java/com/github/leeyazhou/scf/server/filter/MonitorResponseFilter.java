package com.github.leeyazhou.scf.server.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.leeyazhou.scf.server.contract.context.SCFContext;
import com.github.leeyazhou.scf.server.performance.MonitorCenter;

/**
 * A filter for add monitor task to MonitorCenter
 * 
 */
public class MonitorResponseFilter implements IFilter {

  private static final Logger logger = LoggerFactory.getLogger(MonitorResponseFilter.class);

  @Override
  public void filter(SCFContext context) throws Exception {
    logger.debug("MonitorResponseFilter addMonitorTask");
    MonitorCenter.addMonitorTask(context);
  }

  @Override
  public int getPriority() {
    return 0;
  }

}