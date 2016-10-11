/*
 * Copyright 2010 www.58.com, Inc.
 * @author Service Platform Architecture Team mail: spat@58.com web: http://www.58.com
 */
package com.chaboshi.scf.client.utility.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chaboshi.scf.client.SCFConst;

/**
 * FileLog
 *
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class FileLog implements ILog {

  private Logger logger = null;

  public FileLog(Class<?> cls) {
    logger = LoggerFactory.getLogger(cls);
  }

  @Override
  public void fine(String message) {
    logger.info(SCFConst.VERSION_FLAG + message);
  }

  @Override
  public void config(String message) {
    logger.info(SCFConst.VERSION_FLAG + message);
  }

  @Override
  public void info(String message) {
    logger.info(SCFConst.VERSION_FLAG + message);
  }

  @Override
  public void warning(String message) {
    logger.warn(SCFConst.VERSION_FLAG + message);
  }

  @Override
  public void debug(String message) {
    logger.debug(SCFConst.VERSION_FLAG + message);
  }

  @Override
  public void debug(String message, Throwable t) {
    logger.debug(SCFConst.VERSION_FLAG + message, t);
  }

  @Override
  public void warn(String message) {
    logger.warn(SCFConst.VERSION_FLAG + message);
  }

  @Override
  public void error(String message) {
    logger.error(SCFConst.VERSION_FLAG + message);
  }

  @Override
  public void error(String message, Throwable t) {
    logger.error(SCFConst.VERSION_FLAG + message, t);
  }

  @Override
  public void error(Throwable e) {
    logger.error("", e);
  }

  @Override
  public void info(String message, Throwable t) {
    logger.info(SCFConst.VERSION_FLAG + message, t);
  }

  @Override
  public void warn(String message, Throwable t) {
    logger.warn(SCFConst.VERSION_FLAG + message, t);
  }
}
