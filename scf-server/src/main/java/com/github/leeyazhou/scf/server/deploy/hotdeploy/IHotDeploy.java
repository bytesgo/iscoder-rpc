package com.github.leeyazhou.scf.server.deploy.hotdeploy;

import com.github.leeyazhou.scf.server.core.handler.Handler;

/**
 * a interface for description hot deploy class
 * 
 */
public interface IHotDeploy {

  public void setSyncInvokerHandle(Handler handle);

  public void setAsyncInvokerHandle(Handler handle);

}