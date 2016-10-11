/*
 * Copyright 2010 58.com, Inc.
 */

package com.chaboshi.scf.server.contract.server;

/**
 * a interface for description start/stop socket server
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 * 
 *         <a href="http://blog.58.com/spat/">blog</a> <a href="http://www.58.com">website</a>
 * 
 */
public interface Server {

  public void start() throws Exception;

  public void stop() throws Exception;
}