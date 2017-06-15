/*
 * Copyright 2010 58.com, Inc.
 */

package com.chaboshi.scf.server.contract.context;

/**
 * a interface for description ProxyFactory every service contain only one ProxyFactory for create ProxyStub
 * 
 */
public interface IProxyFactory {
  public IProxyStub getProxy(String lookup);// throws ProtocolException;
}