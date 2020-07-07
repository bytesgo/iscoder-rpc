package com.github.leeyazhou.scf.client.proxy.builder;

import java.lang.reflect.Proxy;
import java.net.MalformedURLException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ProxyFactory
 * 
 * 
 */
public class ProxyFactory {

  private static ConcurrentHashMap<String, Object> proxyCache = new ConcurrentHashMap<String, Object>(); // 同步map

  /**
   * Factory for Proxy - creation.<br/>
   * 
   * example : <br/>
   * ProxyFactory.create(INewsService.class, "tcp://demo/NewsService");
   * 
   * @param type the class of type
   * @param strUrl request URL
   * @return
   * @throws MalformedURLException
   */
  @SuppressWarnings("unchecked")
  public static <T> T create(Class<T> type, String strUrl) {
    // 返回代理的实例 泛型
    String key = strUrl.toLowerCase();
    Object proxy = null;
    if (proxyCache.containsKey(key)) {
      proxy = proxyCache.get(key);
    }
    if (proxy == null) {
      proxy = createStandardProxy(strUrl, type);
      if (proxy != null) {
        proxyCache.put(key, proxy);
      }
    }
    return (T) proxy;
  }

  /***
   * url = "tcp://demo/NewService";
   * 
   * @param type 接口类
   * @return
   */
  private static Object createStandardProxy(String strUrl, Class<?> type) {
    String serviceName = "";
    String lookup = "";// 接口实现类
    strUrl = strUrl.replace("tcp://", "");
    String[] splits = strUrl.split("/");
    if (splits.length == 2) {
      serviceName = splits[0]; // =demo
      lookup = splits[1]; // =NewService
    }
    ProxyStandard handler = new ProxyStandard(type, serviceName, lookup);
    return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[] { type }, handler);
  }
}