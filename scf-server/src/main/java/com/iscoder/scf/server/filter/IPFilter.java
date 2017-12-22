package com.iscoder.scf.server.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iscoder.scf.server.IFilter;
import com.iscoder.scf.server.contract.context.SCFContext;
import com.iscoder.scf.server.util.IPTableUtil;

public class IPFilter implements IFilter {

  private static final Logger logger = LoggerFactory.getLogger(IPFilter.class);

  @Override
  public void filter(SCFContext context) throws Exception {
    if (IPTableUtil.isAllow(context.getChannel().getRemoteIP())) {
      logger.info("new channel conected:" + context.getChannel().getRemoteIP());
    } else {
      logger.error("forbid ip not allow connect:" + context.getChannel().getRemoteIP());
      context.getChannel().close();
    }
  }

  @Override
  public int getPriority() {
    return 100;
  }

}