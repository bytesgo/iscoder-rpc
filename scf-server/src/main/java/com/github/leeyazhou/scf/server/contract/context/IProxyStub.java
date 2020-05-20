package com.github.leeyazhou.scf.server.contract.context;

import com.github.leeyazhou.scf.server.exception.ServiceFrameException;

/**
 * a interface for description ProxyStub
 * 
 * 
 */
public interface IProxyStub {

  public SCFResponse invoke(SCFContext context) throws ServiceFrameException;
}
