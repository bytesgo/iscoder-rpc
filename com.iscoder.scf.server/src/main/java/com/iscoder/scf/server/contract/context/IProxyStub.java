package com.iscoder.scf.server.contract.context;

import com.iscoder.scf.server.exception.ServiceFrameException;

/**
 * a interface for description ProxyStub
 * 
 * 
 */
public interface IProxyStub {

  public SCFResponse invoke(SCFContext context) throws ServiceFrameException;
}
