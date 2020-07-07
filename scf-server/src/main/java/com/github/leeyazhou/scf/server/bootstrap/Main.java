package com.github.leeyazhou.scf.server.bootstrap;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.github.leeyazhou.scf.core.loader.DynamicClassLoader;
import com.github.leeyazhou.scf.core.loader.SCFLoader;
import com.github.leeyazhou.scf.core.util.StringUtil;
import com.github.leeyazhou.scf.server.contract.context.Global;
import com.github.leeyazhou.scf.server.contract.context.ServiceConfig;
import com.github.leeyazhou.scf.server.deploy.filemonitor.FileMonitor;
import com.github.leeyazhou.scf.server.deploy.filemonitor.HotDeployListener;
import com.github.leeyazhou.scf.server.deploy.filemonitor.NotifyCount;

/**
 * serive frame entry main para: serviceName
 */
public class Main {

  private static final Logger logger = LoggerFactory.getLogger(Main.class);
  private static ServiceConfig serviceConfig;
  private static final String serviceNameKey = "scf.service.name";

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

    final String scfHome = System.getProperty("scf.home", null);
    final String rootPath = scfHome + "/";
    String serviceName = "no service name please set it";
    Map<String, String> argsMap = argsToMap(args);
    if (argsMap.containsKey(serviceNameKey)) {
      serviceName = argsMap.get(serviceNameKey);
    }
    Global.getSingleton().setRootPath(rootPath);
    String serviceFolderPath = scfHome + "/services/" + serviceName;
    String scfConfigDefaultPath = scfHome + "/conf/scf_config.xml";
    String scfConfigPath = serviceFolderPath + "/conf/scf_config.xml";

    logger.info("+++++++++++++++++++++ staring +++++++++++++++++++++\n");

    logger.info("scf.home: " + scfHome);
    logger.info("rootPath: " + rootPath);
    logger.info("service scf_config.xml: " + scfConfigPath);
    logger.info("default scf_config.xml: " + scfConfigDefaultPath);

    serviceConfig = ServiceConfig.getServiceConfig(scfConfigDefaultPath, scfConfigPath);

    for (Map.Entry<String, String> entry : argsMap.entrySet()) {
      logger.debug(entry.getKey() + ": " + entry.getValue());
      serviceConfig.set(entry.getKey(), entry.getValue());
    }
    if (StringUtil.isEmpty(serviceConfig.getServiceName())) {
      logger.info("scf.service.name:" + serviceName);
      serviceConfig.setServiceName(serviceName);
    }
    Global.getSingleton().setServiceConfig(serviceConfig);

    // init class loader
    logger.info("-----------------loading global jars------------------");
    final SCFLoader loader = new SCFLoader();
    final DynamicClassLoader classLoader = new DynamicClassLoader(loader);

    loader.addURLFolder(scfHome + "/libs");

    classLoader.addFolder(rootPath + "/services/" + serviceConfig.getServiceName() + "/libs");

    Class<?> bootstrapClazz = loader.loadClass("com.github.leeyazhou.scf.server.bootstrap.Bootstrap");
    Method startupMethod = bootstrapClazz.getDeclaredMethod("startup");
    Constructor<?> constructor = bootstrapClazz.getConstructor(ServiceConfig.class, DynamicClassLoader.class);
    startupMethod.invoke(constructor.newInstance(serviceConfig, classLoader));

    addFileMonitor(rootPath);
    // while (true) {
    // Thread.sleep(1 * 60 * 60 * 1000);
    // }
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

    FileMonitor.getInstance().addMonitorFile(rootPath + "service/" + serviceConfig.getServiceName() + "/");
    FileMonitor.getInstance().setInterval(5000);
    FileMonitor.getInstance().setNotifyCount(NotifyCount.Once);
    FileMonitor.getInstance().addListener(new HotDeployListener());
    FileMonitor.getInstance().start();

    logger.info("-------------------------end-------------------------\n");
  }

  private static Map<String, String> argsToMap(String[] args) {
    Map<String, String> argsMap = new HashMap<String, String>();

    for (int i = 0; i < args.length; i++) {
      if (args[i].startsWith("-D")) {
        args[i] = args[i].replaceFirst("-D", "");

        String[] aryArg = args[i].split("=");
        if (aryArg.length == 2) {
          argsMap.put(aryArg[0], aryArg[1]);
        }
      }
    }
    return argsMap;
  }
}
