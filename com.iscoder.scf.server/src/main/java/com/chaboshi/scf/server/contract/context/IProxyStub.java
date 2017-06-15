/*
 * Copyright 2010 58.com, Inc.
 */

package com.chaboshi.scf.server.contract.context;

import com.chaboshi.scf.server.exception.ServiceFrameException;

/**
 * a interface for description ProxyStub
 * 
 * 
 */
public interface IProxyStub {

  public SCFResponse invoke(SCFContext context) throws ServiceFrameException;
}
