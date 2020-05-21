/**
 * 
 */
package com.github.leeyazhou.scf.server.bootstrap;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.github.leeyazhou.scf.core.IInit;
import com.github.leeyazhou.scf.core.loader.DynamicClassLoader;
import com.github.leeyazhou.scf.server.contract.context.Global;
import com.github.leeyazhou.scf.server.contract.context.IProxyFactory;
import com.github.leeyazhou.scf.server.contract.context.ServiceConfig;
import com.github.leeyazhou.scf.server.core.communication.Server;
import com.github.leeyazhou.scf.server.deploy.hotdeploy.ProxyFactoryLoader;
import com.github.leeyazhou.scf.server.filter.IFilter;

/**
 * @author leeyazhou
 *
 */
public class Bootstrap {
  private static final Logger logger = LoggerFactory.getLogger(Bootstrap.class);
  private final DynamicClassLoader classLoader;
  private final ServiceConfig serviceConfig;

  public Bootstrap(ServiceConfig serviceConfig, DynamicClassLoader classLoader) {
    this.serviceConfig = serviceConfig;
    this.classLoader = classLoader;
  }

  public void startup() {
    logger.info("-------------------------end-------------------------\n");

    // load proxy factory
    logger.info("--------------------loading proxys-------------------");
    IProxyFactory proxyFactory = ProxyFactoryLoader.loadProxyFactory(classLoader);
    Global.getSingleton().setProxyFactory(proxyFactory);
    logger.info("-------------------------end-------------------------\n");

    // load init beans
    loadInitBeans(classLoader, serviceConfig);
    addFilter(classLoader);
    loadServers(classLoader);

    try {
      registerExcetEven();
    } catch (Exception e) {
      logger.error("registerExcetEven error", e);
      System.exit(0);
    }

    logger.info("+++++++++++++++++++++ server start success!!! +++++++++++++++++++++\n");
  }


  /**
   * 
   * @param classLoader
   * @param sc
   * @throws Exception
   */
  private void loadInitBeans(DynamicClassLoader classLoader, ServiceConfig sc) {
    logger.info("-----------------loading init beans------------------");
    List<String> initList = sc.getList("scf.init", ",");
    if (initList != null) {
      for (String initBeans : initList) {
        try {
          logger.info("load: " + initBeans);
          IInit initBean = (IInit) classLoader.loadClass(initBeans).newInstance();
          Global.getSingleton().addInit(initBean);
          initBean.init();
        } catch (Exception e) {
          logger.error("init " + initBeans + " error!!!", e);
        }
      }
    }
    logger.info("-------------------------end-------------------------\n");
  }

  /**
   * 加载授权文件方法
   * 
   * @param sc
   * @param key
   * @param serverName
   * @throws Exception
   */
  // private static void loadSecureKey(ServiceConfig sc, String path) throws
  // Exception{
  // File[] file = new File(path).listFiles();
  // for(File f : file){
  // String fName = f.getName();
  // if(!f.exists() || fName.indexOf("secure") < 0 ||
  // !"xml".equalsIgnoreCase(fName.substring(fName.lastIndexOf(".")+1))){
  // continue;
  // }
  // sc.getSecureConfig(f.getPath());
  // }
  // }

  /**
   * 
   * @param classLoader
   * @param sc
   * @param key
   * @throws Exception
   */
  private List<IFilter> loadFilters(DynamicClassLoader classLoader, ServiceConfig sc, String key) {
    List<String> filterList = sc.getList(key, ",");
    List<IFilter> instanceList = new ArrayList<IFilter>();
    if (filterList != null) {
      for (String filterName : filterList) {
        try {
          logger.debug("load: " + filterName);
          IFilter filter = (IFilter) classLoader.loadClass(filterName.trim()).newInstance();
          instanceList.add(filter);
        } catch (Exception e) {
          logger.error("load " + filterName + " error!!!", e);
        }
      }
    }
    return instanceList;
  }

  private void addFilter(DynamicClassLoader classLoader) {
    // load global request-filters
    logger.info("-----------loading global request filters------------");
    List<IFilter> requestFilters = loadFilters(classLoader, serviceConfig, "scf.filter.global.request");
    for (IFilter filter : requestFilters) {
      Global.getSingleton().addGlobalRequestFilter(filter);
    }
    logger.info("-------------------------end-------------------------\n");

    // load global response-filters
    logger.info("-----------loading global response filters-----------");
    List<IFilter> responseFilters = loadFilters(classLoader, serviceConfig, "scf.filter.global.response");
    for (IFilter filter : responseFilters) {
      Global.getSingleton().addGlobalResponseFilter(filter);
    }
    logger.info("-------------------------end-------------------------\n");

    // load connection filters
    logger.info("-----------loading connection filters-----------");
    List<IFilter> connFilters = loadFilters(classLoader, serviceConfig, "scf.filter.connection");
    for (IFilter filter : connFilters) {
      Global.getSingleton().addConnectionFilter(filter);
    }
    logger.info("-------------------------end-------------------------\n");
  }

  /**
   * 
   * @param classLoader
   * @param sc
   * @throws Exception
   */
  private void loadServers(DynamicClassLoader classLoader) {
    logger.info("------------------ starting servers -----------------");
    List<String> servers = serviceConfig.getList("scf.servers", ",");
    for (String server : servers) {
      try {
        if (serviceConfig.getBoolean(server + ".enable")) {
          logger.info(server + " is starting...");
          Server serverImpl =
              (Server) classLoader.loadClass(serviceConfig.getString(server + ".implement")).newInstance();
          Global.getSingleton().addServer(serverImpl);
          serverImpl.start();
          logger.info(server + "started success!!!\n");
        }
      } catch (Exception err) {
        logger.error(server + "error : ", err);
      }
    }
    logger.info("-------------------------end-------------------------\n");
  }

  /**
   * when shutdown server destroyed all socket connection
   */
  private void registerExcetEven() {
    Runtime.getRuntime().addShutdownHook(new Thread() {
      public void run() {
        for (Server server : Global.getSingleton().getServerList()) {
          try {
            server.stop();
          } catch (Exception e) {
            logger.error("stop server error", e);
          }
        }

        try {
          super.finalize();
        } catch (Throwable e) {
          logger.error("super.finalize() error when stop server", e);
        }
      }
    });
  }
}
