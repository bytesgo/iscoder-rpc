package com.github.leeyazhou.scf.server.bootstrap;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.leeyazhou.scf.server.IFilter;
import com.github.leeyazhou.scf.server.IInit;
import com.github.leeyazhou.scf.server.contract.context.Global;
import com.github.leeyazhou.scf.server.contract.context.IProxyFactory;
import com.github.leeyazhou.scf.server.contract.context.ServiceConfig;
import com.github.leeyazhou.scf.server.core.communication.Server;
import com.github.leeyazhou.scf.server.deploy.filemonitor.FileMonitor;
import com.github.leeyazhou.scf.server.deploy.filemonitor.HotDeployListener;
import com.github.leeyazhou.scf.server.deploy.filemonitor.NotifyCount;
import com.github.leeyazhou.scf.server.deploy.hotdeploy.DynamicClassLoader;
import com.github.leeyazhou.scf.server.deploy.hotdeploy.GlobalClassLoader;
import com.github.leeyazhou.scf.server.deploy.hotdeploy.ProxyFactoryLoader;

/**
 * serive frame entry main para: serviceName
 */
public class Main {

  private static final Logger logger = LoggerFactory.getLogger(Main.class);
  private static ServiceConfig serviceConfig;

  /**
   * start server
   * 
   * @param args : service name
   * @throws Exception
   */

  public static void main(String[] args) throws Exception {
    if (args.length < 1) {
      throw new IllegalArgumentException("usage: -Dscf.service.name=<service-name> [<other-scf-config>]");
    }

    String userDir = System.getProperty("user.dir", null);
    String rootPath = userDir + "/";
    String serviceName = "no service name please set it";
    Map<String, String> argsMap = new HashMap<String, String>();
    Global.getSingleton().setRootPath(rootPath);

    for (int i = 0; i < args.length; i++) {
      if (args[i].startsWith("-D")) {
        String[] aryArg = args[i].split("=");
        if (aryArg.length == 2) {
          if (aryArg[0].equalsIgnoreCase("-Dscf.service.name")) {
            serviceName = aryArg[1];
          }
          argsMap.put(aryArg[0].replaceFirst("-D", ""), aryArg[1]);
        }
      }
    }

    String serviceFolderPath = rootPath + "service/deploy/" + serviceName;
    String scfConfigDefaultPath = rootPath + "conf/scf_config.xml";
    String scfConfigPath = serviceFolderPath + "/scf_config.xml";

    logger.info("+++++++++++++++++++++ staring +++++++++++++++++++++\n");

    logger.info("user.dir: " + userDir);
    logger.info("rootPath: " + rootPath);
    logger.info("service scf_config.xml: " + scfConfigPath);
    logger.info("default scf_config.xml: " + scfConfigDefaultPath);

    serviceConfig = ServiceConfig.getServiceConfig(scfConfigDefaultPath, scfConfigPath);

    Set<String> keySet = argsMap.keySet();
    for (String key : keySet) {
      logger.debug(key + ": " + argsMap.get(key));
      serviceConfig.set(key, argsMap.get(key));
    }
    if (serviceConfig.getServiceName() == null || serviceConfig.getServiceName().length() == 0) {
      logger.info("scf.service.name:" + serviceName);
      serviceConfig.setServiceName(serviceName);
    }
    Global.getSingleton().setServiceConfig(serviceConfig);

    // init class loader
    logger.info("-----------------loading global jars------------------");
    DynamicClassLoader classLoader = new DynamicClassLoader();
    String[] jarPath = new String[3];
    jarPath[0] = rootPath + "service/deploy/" + serviceConfig.getServiceName() + "/";
    jarPath[1] = rootPath + "service/lib/";
    jarPath[2] = rootPath + "lib";
    classLoader.addFolder(jarPath);
    GlobalClassLoader.addSystemClassPathFolder(jarPath);
    jarPath = null;
    logger.info("-------------------------end-------------------------\n");

    if (new File(serviceFolderPath).isDirectory() || !serviceName.equalsIgnoreCase("error_service_name_is_null")) {
      // load proxy factory
      logger.info("--------------------loading proxys-------------------");
      IProxyFactory proxyFactory = ProxyFactoryLoader.loadProxyFactory(classLoader);
      Global.getSingleton().setProxyFactory(proxyFactory);
      logger.info("-------------------------end-------------------------\n");
    }

    // load init beans
    loadInitBeans(classLoader, serviceConfig);
    addFilter(classLoader);
    loadServers(classLoader);
    addFileMonitor(rootPath);

    try {
      registerExcetEven();
    } catch (Exception e) {
      logger.error("registerExcetEven error", e);
      System.exit(0);
    }

    logger.info("+++++++++++++++++++++ server start success!!! +++++++++++++++++++++\n");
    while (true) {
      Thread.sleep(1 * 60 * 60 * 1000);
    }
  }

  /**
   * 
   * @param classLoader
   * @param sc
   * @throws Exception
   */
  private static void loadInitBeans(DynamicClassLoader classLoader, ServiceConfig sc) throws Exception {
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
  private static List<IFilter> loadFilters(DynamicClassLoader classLoader, ServiceConfig sc, String key) throws Exception {
    List<String> filterList = sc.getList(key, ",");
    List<IFilter> instanceList = new ArrayList<IFilter>();
    if (filterList != null) {
      for (String filterName : filterList) {
        try {
          logger.debug("load: " + filterName);
          IFilter filter = (IFilter) classLoader.loadClass(filterName).newInstance();
          instanceList.add(filter);
        } catch (Exception e) {
          logger.error("load " + filterName + " error!!!", e);
        }
      }
    }
    return instanceList;
  }

  private static void addFilter(DynamicClassLoader classLoader) throws Exception {
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
  private static void loadServers(DynamicClassLoader classLoader) throws Exception {
    logger.info("------------------ starting servers -----------------");
    List<String> servers = serviceConfig.getList("scf.servers", ",");
    for (String server : servers) {
      try {
        if (serviceConfig.getBoolean(server + ".enable")) {
          logger.info(server + " is starting...");
          Server serverImpl = (Server) classLoader.loadClass(serviceConfig.getString(server + ".implement")).newInstance();
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
   * add current service file to file monitor
   * 
   * @param config
   * @throws Exception
   */
  private static void addFileMonitor(String rootPath) throws Exception {
    if (!serviceConfig.isHotDeploy()) {
      return;
    }
    logger.info("------------------init file monitor-----------------");

    FileMonitor.getInstance().addMonitorFile(rootPath + "service/deploy/" + serviceConfig.getServiceName() + "/");
    FileMonitor.getInstance().setInterval(5000);
    FileMonitor.getInstance().setNotifyCount(NotifyCount.Once);
    FileMonitor.getInstance().addListener(new HotDeployListener());
    FileMonitor.getInstance().start();

    logger.info("-------------------------end-------------------------\n");
  }

  /**
   * when shutdown server destroyed all socket connection
   */
  private static void registerExcetEven() {
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
