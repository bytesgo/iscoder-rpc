/*
 * Copyright 2010 www.58.com, Inc.
 * @author Service Platform Architecture Team mail: spat@58.com web: http://www.58.com
 */
package com.chaboshi.scf.client;

/**
 * SCFConst
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class SCFConst {

  public static String CONFIG_PATH = SCFInit.DEFAULT_CONFIG_PATH;
  /** 配置文件路径 */
  public static final long MAX_SESSIONID = 1024 * 1024 * 1024;
  public static final int DEFAULT_MAX_THREAD_COUNT = 2000;
  public static final int DEFAULT_MAX_CURRENT_USER_COUNT = 2000;
  public static final int DEFAULT_MAX_PAKAGE_SIZE = 1024 * 1024;
  /** 1m */
  public static final int DEFAULT_BUFFER_SIZE = 10 * 1024;
  /** 10KB */
  public static final int DEFAULT_DEAD_TIMEOUT = 60000;
  /** 60s */
  public static final boolean DEFAULT_PROTECTED = true;
  public static final String VERSION_FLAG = "SCF Client V2.0.1:";
}
