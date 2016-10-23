package com.chaboshi.scf.server.deploy.filemonitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chaboshi.scf.server.contract.context.Global;
import com.chaboshi.scf.server.contract.context.IProxyFactory;
import com.chaboshi.scf.server.deploy.hotdeploy.DynamicClassLoader;
import com.chaboshi.scf.server.deploy.hotdeploy.ProxyFactoryLoader;

/**
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 * 
 *         <a href="http://blog.58.com/spat/">blog</a> <a href="http://www.58.com">website</a>
 * 
 */
public class HotDeployListener implements IListener {

  private static Logger logger = LoggerFactory.getLogger(HotDeployListener.class);

  public void fileChanged(FileInfo fInfo) {
    logger.info("service file is change!!! ");
    try {
      logger.info("begin hot deploy scf...");

      DynamicClassLoader classLoader = new DynamicClassLoader();
      classLoader
          .addFolder(
              Global.getSingleton().getRootPath() + "service/deploy/"
                  + Global.getSingleton().getServiceConfig().getServiceName() + "/",
              Global.getSingleton().getRootPath() + "service/lib/", Global.getSingleton().getRootPath() + "lib");

      IProxyFactory proxyFactory = ProxyFactoryLoader.loadProxyFactory(classLoader);
      if (proxyFactory != null) {
        Global.getSingleton().setProxyFactory(proxyFactory);
        logger.info("change context class loader");
        Thread.currentThread().setContextClassLoader(proxyFactory.getClass().getClassLoader());
        logger.info("init serializer type map");
        // TypeHelper.InitTypeMap();
        logger.info("notice gc");
        System.gc();
        logger.info("hot deploy service success!!!");

      } else {
        logger.error("IInvokerHandle is null when hotDeploy!!!");
      }

      logger.info("finish hot deploy!!!");
    } catch (Exception e) {
      logger.error("create proxy error", e);
    }
  }
}