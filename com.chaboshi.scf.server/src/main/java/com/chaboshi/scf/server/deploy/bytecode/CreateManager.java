package com.chaboshi.scf.server.deploy.bytecode;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chaboshi.scf.server.contract.context.IProxyFactory;
import com.chaboshi.scf.server.contract.context.IProxyStub;
import com.chaboshi.scf.server.deploy.hotdeploy.DynamicClassLoader;

/**
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 * 
 *         <a href="http://blog.58.com/spat/">blog</a> <a href="http://www.58.com">website</a>
 * 
 */
public class CreateManager {

  private static Logger logger = LoggerFactory.getLogger(CreateManager.class);

  public IProxyFactory careteProxy(String serviceRootPath, DynamicClassLoader classLoader) throws Exception {

    String configPath = serviceRootPath + "/" + Constant.SERVICE_CONTRACT;
    File file = new File(configPath);
    ContractInfo serviceContract = null;

    // if config file exists load contract info from config

    if (file != null && file.exists()) {
      serviceContract = ContractConfig.loadContractInfo(configPath, classLoader);
    } else {
      serviceContract = ScanClass.getContractInfo(serviceRootPath + "/", classLoader);
    }
    long time = System.currentTimeMillis();
    List<ClassFile> localProxyList = new ProxyClassCreater().createProxy(classLoader, serviceContract, time);
    logger.info("proxy class buffer creater finish!!!");
    ClassFile cfProxyFactory = new ProxyFactoryCreater().createProxy(classLoader, serviceContract, time);
    logger.info("proxy factory buffer creater finish!!!");

    List<IProxyStub> localProxyAry = new ArrayList<IProxyStub>();
    for (ClassFile cf : localProxyList) {
      Class<?> cls = classLoader.findClass(cf.getClsName(), cf.getClsByte(), null);
      logger.info("dynamic load class:" + cls.getName());
      localProxyAry.add((IProxyStub) cls.newInstance());
    }

    Class<?> proxyFactoryCls = classLoader.findClass(cfProxyFactory.getClsName(), cfProxyFactory.getClsByte(), null);
    Constructor<?> constructor = proxyFactoryCls.getConstructor(List.class);
    IProxyFactory pfInstance = (IProxyFactory) constructor.newInstance(localProxyAry);
    logger.info("crate ProxyFactory instance!!!");
    return pfInstance;
  }
}