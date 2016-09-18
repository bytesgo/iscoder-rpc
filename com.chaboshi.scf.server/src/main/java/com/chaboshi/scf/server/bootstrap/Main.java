package com.chaboshi.scf.server.bootstrap;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Appender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;

import com.chaboshi.scf.server.contract.context.Global;
import com.chaboshi.scf.server.contract.context.IProxyFactory;
import com.chaboshi.scf.server.contract.context.ServiceConfig;
import com.chaboshi.scf.server.contract.filter.IFilter;
import com.chaboshi.scf.server.contract.init.IInit;
import com.chaboshi.scf.server.contract.log.ILog;
import com.chaboshi.scf.server.contract.log.Log4jConfig;
import com.chaboshi.scf.server.contract.log.LogFactory;
import com.chaboshi.scf.server.contract.log.SystemPrintStream;
import com.chaboshi.scf.server.contract.server.Server;
import com.chaboshi.scf.server.deploy.filemonitor.FileMonitor;
import com.chaboshi.scf.server.deploy.filemonitor.HotDeployListener;
import com.chaboshi.scf.server.deploy.filemonitor.NotifyCount;
import com.chaboshi.scf.server.deploy.hotdeploy.DynamicClassLoader;
import com.chaboshi.scf.server.deploy.hotdeploy.GlobalClassLoader;
import com.chaboshi.scf.server.deploy.hotdeploy.ProxyFactoryLoader;

/**
 * serive frame entry main para: serviceName
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 * 
 *         <a href="http://blog.58.com/spat/">blog</a>
 *         <a href="http://www.58.com">website</a>
 * 
 */
public class Main {

  private static ILog logger = null;

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

    String userDir = System.getProperty("user.dir");
    String rootPath = userDir + "/../";
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
    String log4jConfigDefaultPath = rootPath + "conf/scf_log4j.xml";
    String log4jConfigPath = serviceFolderPath + "/scf_log4j.xml";

    // load log4j
    loadLog4jConfig(log4jConfigPath, log4jConfigDefaultPath, serviceName);
    logger = LogFactory.getLogger(Main.class);

    logger.info("+++++++++++++++++++++ staring +++++++++++++++++++++\n");

    logger.info("user.dir: " + userDir);
    logger.info("rootPath: " + rootPath);
    logger.info("service scf_config.xml: " + scfConfigPath);
    logger.info("default scf_config.xml: " + scfConfigDefaultPath);
    logger.info("service scf_log4j.xml: " + log4jConfigPath);
    logger.info("default scf_log4j.xml: " + log4jConfigDefaultPath);

    // load service config
    logger.info("load service config...");
    ServiceConfig serviceConfig = loadServiceConfig(scfConfigDefaultPath, scfConfigPath);
    Set<String> keySet = argsMap.keySet();
    for (String key : keySet) {
      logger.info(key + ": " + argsMap.get(key));
      serviceConfig.set(key, argsMap.get(key));
    }
    if (serviceConfig.getServiceName() == null || serviceConfig.getServiceName().equalsIgnoreCase("")) {
      logger.info("scf.service.name:" + serviceName);
      serviceConfig.set("scf.service.name", serviceName);
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

      // load init beans
      logger.info("-----------------loading init beans------------------");
      loadInitBeans(classLoader, serviceConfig);
      logger.info("-------------------------end-------------------------\n");
    }

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

    // load servers
    logger.info("------------------ starting servers -----------------");
    loadServers(classLoader, serviceConfig);
    logger.info("-------------------------end-------------------------\n");

    // add current service file to monitor
    if (serviceConfig.getBoolean("scf.hotdeploy")) {
      logger.info("------------------init file monitor-----------------");
      addFileMonitor(rootPath, serviceConfig.getServiceName());
      logger.info("-------------------------end-------------------------\n");
    }

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
   * load service config
   * 
   * @param cps
   * @return
   * @throws Exception
   */
  private static ServiceConfig loadServiceConfig(String... cps) throws Exception {
    ServiceConfig sc = ServiceConfig.getServiceConfig(cps);
    if (sc == null) {
      logger.error("ServiceConfig sc is null");
    }

    return sc;
  }

  /**
   * 
   * @param configPath
   * @param logFilePath
   * @throws Exception
   */
  private static void loadLog4jConfig(String configPath, String defaultPath, String serviceName) throws Exception {
    File fLog4jConfig = new File(configPath);
    if (fLog4jConfig.exists()) {
      Log4jConfig.configure(configPath);
      SystemPrintStream.redirectToLog4j();
    } else {
      Log4jConfig.configure(defaultPath);
      String logPath = null;
      Appender appender = Logger.getRootLogger().getAppender("activexAppender");
      if (appender instanceof FileAppender) {
        FileAppender fappender = (FileAppender) appender;
        if (!fappender.getFile().contains(serviceName)) {
          logPath = fappender.getFile();
          fappender.setFile(
              fappender.getFile().substring(0, fappender.getFile().lastIndexOf("/")) + "/" + serviceName + "/" + serviceName + ".log");
          fappender.activateOptions();
        }
      }
      SystemPrintStream.redirectToLog4j();
      try {
        if (logPath != null) {
          File file = new File(logPath);
          file.delete();
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    // redirect System.out & System.err into log file

  }

  /**
   * 
   * @param classLoader
   * @param sc
   * @throws Exception
   */
  private static void loadInitBeans(DynamicClassLoader classLoader, ServiceConfig sc) throws Exception {
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
          logger.info("load: " + filterName);
          IFilter filter = (IFilter) classLoader.loadClass(filterName).newInstance();
          instanceList.add(filter);
        } catch (Exception e) {
          logger.error("load " + filterName + " error!!!", e);
        }
      }
    }

    return instanceList;
  }

  /**
   * 
   * @param classLoader
   * @param sc
   * @throws Exception
   */
  private static void loadServers(DynamicClassLoader classLoader, ServiceConfig sc) throws Exception {
    List<String> servers = sc.getList("scf.servers", ",");
    if (servers != null) {
      for (String server : servers) {
        try {
          if (sc.getBoolean(server + ".enable")) {
            logger.info(server + " is starting...");
            Server serverImpl = (Server) classLoader.loadClass(sc.getString(server + ".implement")).newInstance();
            Global.getSingleton().addServer(serverImpl);
            serverImpl.start();
            logger.info(server + "started success!!!\n");
          }
        } catch (Exception ex) {

        }
      }
    }
  }

  /**
   * add current service file to file monitor
   * 
   * @param config
   * @throws Exception
   */
  private static void addFileMonitor(String rootPath, String serviceName) throws Exception {
    FileMonitor.getInstance().addMonitorFile(rootPath + "service/deploy/" + serviceName + "/");

    FileMonitor.getInstance().setInterval(5000);
    FileMonitor.getInstance().setNotifyCount(NotifyCount.Once);
    FileMonitor.getInstance().addListener(new HotDeployListener());
    FileMonitor.getInstance().start();
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
