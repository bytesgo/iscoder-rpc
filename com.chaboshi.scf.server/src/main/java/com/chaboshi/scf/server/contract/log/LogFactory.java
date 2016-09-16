/*
 * Copyright 2010 58.com, Inc.
 *
 *
 */

package com.chaboshi.scf.server.contract.log;

/**
 * A class to get an instance for a logger object
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 * 
 *         <a href="http://blog.58.com/spat/">blog</a>
 *         <a href="http://www.58.com">website</a>
 * 
 */
public final class LogFactory {

  /**
   * Get an instance of a logger object.
   * 
   * @param cls the Class to log from
   * @return Logger the logger instance
   */
  public static ILog getLogger(Class<?> cls) {
    return new Log4jLogger(cls);
  }
}