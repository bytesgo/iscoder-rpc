package com.chaboshi.scf.server.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chaboshi.scf.server.IFilter;
import com.chaboshi.scf.server.contract.context.SCFContext;

/**
 * A filter for set SCFContext monitor true
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 * 
 *         <a href="http://blog.58.com/spat/">blog</a> <a href="http://www.58.com">website</a>
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