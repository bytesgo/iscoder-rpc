package com.github.leeyazhou.scf.server.deploy.hotdeploy;

import com.github.leeyazhou.scf.server.core.handler.Handler;

/**
 * a interface for description hot deploy class
 * 
 */
public interface IHotDeploy {

  void setSyncInvokerHandle(Handler handle);

  void setAsyncInvokerHandle(Handler handle);

}
