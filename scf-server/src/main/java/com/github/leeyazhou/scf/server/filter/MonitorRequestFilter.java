package com.github.leeyazhou.scf.server.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.leeyazhou.scf.server.contract.context.SCFContext;

/**
 * A filter for set SCFContext monitor true
 * 
 */
public class MonitorRequestFilter implements IFilter {

  private static final Logger logger = LoggerFactory.getLogger(MonitorRequestFilter.class);

  @Override
  public void filter(SCFContext context) throws Exception {
    logger.debug("MonitorRequestFilter set monitor true");
    context.setMonitor(true);
  }

  @Override
  public int getPriority() {
    return 0;
  }

}