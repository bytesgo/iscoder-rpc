/*
 * Copyright 2010 www.58.com, Inc.
 * 
 * @author Service Platform Architecture Team
 * mail: spat@58.com
 * web: http://www.58.com
 */
package com.chaboshi.scf.client.utility.logger;

/**
 * LogFactory
 *
 * @author Service Platform Architecture Team (spat@58.com)
 */
public final class LogFactory {

  /**
   * Get an instance of a logger object.
   *
   * @param cls the Class to log from
   * @return Logger the logger instance
   */
  public static ILog getLogger(Class<?> cls) {
    return new FileLog(cls);
  }
}
