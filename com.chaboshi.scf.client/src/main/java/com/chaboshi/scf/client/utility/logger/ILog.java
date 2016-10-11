/*
 * Copyright 2010 www.58.com, Inc.
 * @author Service Platform Architecture Team mail: spat@58.com web: http://www.58.com
 */
package com.chaboshi.scf.client.utility.logger;

/**
 * ILog
 *
 * @author Service Platform Architecture Team (spat@58.com)
 */
public interface ILog {

  /**
   * Logging a fine message
   * 
   * @param message the message to log
   */
  void fine(String message);

  /**
   * Logging a config message
   * 
   * @param message the message to log
   */
  void config(String message);

  /**
   * Logging an info message
   * 
   * @param message the message to log
   */
  void info(String message);

  /**
   * Logging a warning message
   * 
   * @param message the message to log
   */
  void warning(String message);

  /**
   * Logging a debug message
   * 
   * @param message the message to log
   */
  void debug(String message);

  /**
   * Logging a debug message with the throwable message
   * 
   * @param message the message to log
   * @param t the exception
   */
  void debug(String message, Throwable t);

  /**
   * Logging an info message with the throwable message
   * 
   * @param message the message to log
   * @param t the exception
   */
  void info(String message, Throwable t);

  /**
   * Logging a warning message
   * 
   * @param message the message to log
   */
  void warn(String message);

  /**
   * Logging a warning message with the throwable message
   * 
   * @param message the message to log
   * @param t the exception
   */
  void warn(String message, Throwable t);

  /**
   * Logging an error message
   * 
   * @param message the message to log
   */
  void error(String message);

  /**
   * Logging an error message with the throwable message
   * 
   * @param message the message to log
   * @param t the exception
   */
  void error(String message, Throwable t);

  /**
   * Logging an error
   * 
   * @param e
   */
  void error(Throwable e);

}