/*
 * Copyright 2010 58.com, Inc.
 */

package com.chaboshi.scf.server.contract.context;

import com.chaboshi.scf.server.util.ServiceFrameException;

/**
 * a interface for description ProxyStub
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 * 
 *         <a href="http://blog.58.com/spat/">blog</a> <a href="http://www.58.com">website</a>
 * 
 */
public interface IProxyStub {

  public SCFResponse invoke(SCFContext context) throws ServiceFrameException;
}
