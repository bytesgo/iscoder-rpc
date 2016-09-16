/*
 * Copyright 2010 www.58.com, Inc.
 * 
 * @author Service Platform Architecture Team
 * mail: spat@58.com
 * web: http://www.58.com
 */
package com.chaboshi.scf.client.proxy.builder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.net.MalformedURLException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ProxyFactory
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 * 
 * 
 */
public class ProxyFactory {

  private static ConcurrentHashMap<String, Object> cache = new ConcurrentHashMap<String, Object>(); // 同步map

  /**
   * Factory for Proxy - creation.
   * 
   * @param type the class of type
   * @param strUrl request URL
   * @return
   * @throws MalformedURLException
   */
  // ProxyFactory.create(INewsService.class, url); url =
  // "tcp://demo/NewsService";
  @SuppressWarnings("unchecked")
  public static <T> T create(Class<T> type, String strUrl) {// <T> T返回任意类型的数据？
    // 返回代理的实例 泛型
    String key = strUrl.toLowerCase();
    Object proxy = null;
    if (cache.containsKey(key)) {
      proxy = cache.get(key);
    }
    if (proxy == null) {
      proxy = createStandardProxy(strUrl, type);
      if (proxy != null) {
        cache.put(key, proxy);
      }
    }

    return (T) proxy;
  }

  /**
   * 
   * @param strUrl tcp://
   ***/
  /***
   * @param type 接口类
   * @return
   */
  // url = "tcp://demo/NewService";
  private static Object createStandardProxy(String strUrl, Class<?> type) {
    String serviceName = "";
    String lookup = "";// 接口实现类
    strUrl = strUrl.replace("tcp://", "");
    String[] splits = strUrl.split("/");
    if (splits.length == 2) {
      serviceName = splits[0]; // =demo
      lookup = splits[1]; // =NewService
    }
    InvocationHandler handler = new ProxyStandard(type, serviceName, lookup);
    return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[] { type }, handler);
  }
}