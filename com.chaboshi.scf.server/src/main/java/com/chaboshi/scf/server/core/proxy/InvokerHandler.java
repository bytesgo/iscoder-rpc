/*
 * Copyright 2010 58.com, Inc.
 *
 *
 */

package com.chaboshi.scf.server.core.proxy;

import com.chaboshi.scf.server.contract.context.SCFContext;

/**
 * a interface for description InvokerHandle such as: AsyncInvokerHandle,
 * AsyncInvokerHandle
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 * 
 *         <a href="http://blog.58.com/spat/">blog</a>
 *         <a href="http://www.58.com">website</a>
 * 
 */
public interface InvokerHandler {

  public void invoke(SCFContext context) throws Exception;

}