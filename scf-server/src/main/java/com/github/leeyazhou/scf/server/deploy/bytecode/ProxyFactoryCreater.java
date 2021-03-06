package com.github.leeyazhou.scf.server.deploy.bytecode;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.github.leeyazhou.scf.core.loader.DynamicClassLoader;
import com.github.leeyazhou.scf.server.contract.context.Global;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;

/**
 * 
 * 
 */
public class ProxyFactoryCreater {

  private static Logger logger = LoggerFactory.getLogger(ProxyFactoryCreater.class);

  @SuppressWarnings("rawtypes")
  public ClassFile createProxy(DynamicClassLoader classLoader, ContractInfo serviceContract, long time) {
    try {
      String pfClsName = "ProxyFactory" + time;
      logger.info("begin create ProxyFactory:" + pfClsName);
      ClassPool pool = ClassPool.getDefault();
      Set<String> jarList = classLoader.getJarList();
      for (String jar : jarList) {
        pool.appendClassPath(jar);
      }

      CtClass ctProxyClass = pool.makeClass(pfClsName, null);

      CtClass proxyFactory = pool.getCtClass(Constant.IPROXYFACTORY_CLASS_NAME);
      ctProxyClass.addInterface(proxyFactory);

      // createProxy
      StringBuilder sbBody = new StringBuilder();
      sbBody.append("public " + Constant.IPROXYSTUB_CLASS_NAME + " getProxy(String lookup) {");
      StringBuilder sbConstructor = new StringBuilder();
      sbConstructor.append("{");
      int proxyCount = 0;
      for (ContractInfo.SessionBean sessionBean : serviceContract.getSessionBeanList()) {
        Iterator it = sessionBean.getInstanceMap().entrySet().iterator();
        while (it.hasNext()) {
          Map.Entry entry = (Map.Entry) it.next();
          String lookup = entry.getKey().toString();

          sbBody.append("if(lookup.equalsIgnoreCase(\"" + lookup + "\")){");
          sbBody.append("return proxy");
          sbBody.append(lookup);
          sbBody.append(Global.getSingleton().getServiceConfig().getServiceName());
          sbBody.append(";}");

          sbConstructor.append("proxy");
          sbConstructor.append(lookup);
          sbConstructor.append(Global.getSingleton().getServiceConfig().getServiceName());
          sbConstructor.append("=(");
          sbConstructor.append(Constant.IPROXYSTUB_CLASS_NAME);
          sbConstructor.append(")$1.get(");
          sbConstructor.append(proxyCount);
          sbConstructor.append(");");

          CtField proxyField = CtField.make("private " + Constant.IPROXYSTUB_CLASS_NAME + " proxy" + lookup
              + Global.getSingleton().getServiceConfig().getServiceName() + " = null;", ctProxyClass);

          ctProxyClass.addField(proxyField);

          proxyCount++;
        }
      }
      sbBody.append("return null;}}");
      sbConstructor.append("}");

      CtMethod methodItem = CtMethod.make(sbBody.toString(), ctProxyClass);
      ctProxyClass.addMethod(methodItem);

      CtConstructor cc = new CtConstructor(new CtClass[] {pool.get("java.util.List")}, ctProxyClass);
      cc.setBody(sbConstructor.toString());
      ctProxyClass.addConstructor(cc);

      logger.debug("ProxyFactory source code:" + sbBody.toString());

      logger.info("create ProxyFactory success!!!");

      return new ClassFile(pfClsName, ctProxyClass.toBytecode());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
