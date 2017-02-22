package com.chaboshi.scf.server.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chaboshi.scf.server.IFilter;
import com.chaboshi.scf.server.contract.context.SCFContext;
import com.chaboshi.scf.server.performance.MonitorCenter;

/**
 * A filter for add monitor task to MonitorCenter
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 * 
 *         <a href="http://blog.58.com/spat/">blog</a> <a href="http://www.58.com">website</a>
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