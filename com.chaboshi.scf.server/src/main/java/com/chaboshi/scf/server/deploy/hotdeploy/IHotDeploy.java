/*
 * Copyright 2010 58.com, Inc.
 */

package com.chaboshi.scf.server.deploy.hotdeploy;

import com.chaboshi.scf.server.core.handler.InvokerHandler;

/**
 * a interface for description hot deploy class
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 * 
 *         <a href="http://blog.58.com/spat/">blog</a> <a href="http://www.58.com">website</a>
 * 
 */
public interface IHotDeploy {

  public void setSyncInvokerHandle(InvokerHandler handle);

  public void setAsyncInvokerHandle(InvokerHandler handle);

}