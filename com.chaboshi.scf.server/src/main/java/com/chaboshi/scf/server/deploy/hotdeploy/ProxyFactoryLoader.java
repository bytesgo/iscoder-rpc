package com.chaboshi.scf.server.deploy.hotdeploy;

import com.chaboshi.scf.server.contract.context.Global;
import com.chaboshi.scf.server.contract.context.IProxyFactory;
import com.chaboshi.scf.server.deploy.bytecode.CreateManager;

/**
 * A class for dynamic load ProxyHandle
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 * 
 *         <a href="http://blog.58.com/spat/">blog</a>
 *         <a href="http://www.58.com">website</a>
 * 
 */
public class ProxyFactoryLoader {

  /**
   * 
   * @param serviceConfig
   * @return
   * @throws Exception
   */
  public static IProxyFactory loadProxyFactory(DynamicClassLoader classLoader) throws Exception {

    CreateManager cm = new CreateManager();
    return cm.careteProxy(
        Global.getSingleton().getRootPath() + "service/deploy/" + Global.getSingleton().getServiceConfig().getString("scf.service.name"),
        classLoader);
  }
}