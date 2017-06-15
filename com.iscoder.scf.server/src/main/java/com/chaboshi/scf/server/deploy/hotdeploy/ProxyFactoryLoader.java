package com.chaboshi.scf.server.deploy.hotdeploy;

import com.chaboshi.scf.server.contract.context.Global;
import com.chaboshi.scf.server.contract.context.IProxyFactory;
import com.chaboshi.scf.server.deploy.bytecode.CreateManager;

/**
 * A class for dynamic load ProxyHandle
 * 
 */
public class ProxyFactoryLoader {

  private static final CreateManager cm = new CreateManager();

  /**
   * 
   * @param serviceConfig
   * @return
   * @throws Exception
   */
  public static IProxyFactory loadProxyFactory(DynamicClassLoader classLoader) throws Exception {
    return cm.careteProxy(
        Global.getSingleton().getRootPath() + "service/deploy/" + Global.getSingleton().getServiceConfig().getServiceName(), classLoader);
  }
}