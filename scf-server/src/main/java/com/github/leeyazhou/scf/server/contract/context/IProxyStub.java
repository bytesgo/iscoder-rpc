package com.github.leeyazhou.scf.server.contract.context;

import com.github.leeyazhou.scf.server.exception.RPCException;

/**
 * a interface for description ProxyStub
 * 
 * 
 */
public interface IProxyStub {

  public SCFResponse invoke(SCFContext context) throws RPCException;
}
