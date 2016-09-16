package com.chaboshi.scf.server.performance.commandhelper;

import com.chaboshi.scf.server.contract.log.ILog;
import com.chaboshi.scf.server.contract.log.LogFactory;

public abstract class CommandHelperBase implements ICommandHelper {

  protected static ILog logger = LogFactory.getLogger(CommandHelperBase.class);

}
