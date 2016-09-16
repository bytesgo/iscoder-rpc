/*
 * Copyright 2010 www.58.com, Inc.
 * 
 * @author Service Platform Architecture Team
 * mail: spat@58.com
 * web: http://www.58.com
 */
package com.chaboshi.scf.client;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.chaboshi.scf.client.proxy.ServiceProxy;

/**
 * SCFInit
 *
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class SCFInit {

  /**
   * 有的老系统没有调用SCFInit的init方法, 因此在SCFConst中增加对SCFInit类的引用,从而保证静态构造函数会被执行
   */
  protected static String DEFAULT_CONFIG_PATH = null;

  static {
    DEFAULT_CONFIG_PATH = System.getProperty("scf.client.config.path");
    if (DEFAULT_CONFIG_PATH == null) {
      DEFAULT_CONFIG_PATH = System.getProperty("scf.config.path");
    }
    if (DEFAULT_CONFIG_PATH == null) {
      DEFAULT_CONFIG_PATH = getJarPath(SCFConst.class) + "/scf.config";
    }

    registerExcetEven();
  }

  @Deprecated
  public static void init(String configPath, String[] jarPaths) {
    SCFConst.CONFIG_PATH = DEFAULT_CONFIG_PATH = configPath;
    // Serializer.SetJarPath(jarPaths);
  }

  public static void init(String configPath) {
    SCFConst.CONFIG_PATH = DEFAULT_CONFIG_PATH = configPath;
  }

  private static String getJarPath(Class<?> type) {
    String path = type.getProtectionDomain().getCodeSource().getLocation().getPath();
    path = path.replaceFirst("file:/", "");
    path = path.replaceAll("!/", "");
    path = path.replaceAll("\\\\", "/");
    path = path.substring(0, path.lastIndexOf("/"));
    if (path.substring(0, 1).equalsIgnoreCase("/")) {
      String osName = System.getProperty("os.name").toLowerCase();
      if (osName.indexOf("window") >= 0) {
        path = path.substring(1);
      }
    }
    try {
      return URLDecoder.decode(path, "UTF-8");
    } catch (UnsupportedEncodingException ex) {
      Logger.getLogger(SCFConst.class.getName()).log(Level.SEVERE, null, ex);
      return path;
    }
  }

  /**
   * when shutdown server destroyed all socket connection
   */
  private static void registerExcetEven() {
    Runtime.getRuntime().addShutdownHook(new Thread() {
      public void run() {
        ServiceProxy.destroyAll();
      }
    });
  }
}