/*
 * Copyright 2010 58.com, Inc.
 */

package com.chaboshi.scf.server.contract.context;

/**
 * a interface for description ProxyFactory every service contain only one ProxyFactory for create ProxyStub
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 * 
 *         <a href="http://blog.58.com/spat/">blog</a> <a href="http://www.58.com">website</a>
 * 
 */
public interface IProxyFactory {
  public IProxyStub getProxy(String lookup);// throws ProtocolException;
}