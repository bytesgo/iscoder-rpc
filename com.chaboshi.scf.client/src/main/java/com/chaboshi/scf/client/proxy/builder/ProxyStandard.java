/*
 * Copyright 2010 www.58.com, Inc.
 * @author Service Platform Architecture Team mail: spat@58.com web: http://www.58.com
 */
package com.chaboshi.scf.client.proxy.builder;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ProxyStandard
 *
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class ProxyStandard implements InvocationHandler, Serializable, IProxyStandard {

  private static final long serialVersionUID = -8742928000690128026L;
  private Class<?> interfaceClass;
  private MethodCaller methodCaller;
  private String lookup;
  private static final Logger logger = LoggerFactory.getLogger(ProxyStandard.class);

  /**
   * @param interfaceClass 接口类
   * @param serviceName 服务名
   * @param lookup 接口实现类
   */
  public ProxyStandard(Class<?> interfaceClass, String serviceName, String lookup) {
    this.lookup = lookup;
    this.interfaceClass = interfaceClass;
    this.methodCaller = new MethodCaller(serviceName, lookup);
  }

  /**
   * args 参数 method 方法
   */
  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    long start = System.currentTimeMillis();
    Object obj = null;
    try {
      obj = methodCaller.doMethodCall(args, method);
    } catch (Exception e) {
      e.printStackTrace();
    }
    long end = System.currentTimeMillis();
    long total = end - start;
    if (total > 200) {
      logger.warn("interface:" + interfaceClass.getName() + ";class:" + lookup + ";method:" + method.getName() + ";invoke time :" + total);
    }
    return obj;
  }
}
