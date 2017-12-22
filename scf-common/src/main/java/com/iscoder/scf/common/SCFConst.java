package com.iscoder.scf.common;

/**
 * SCFConst
 */
public final class SCFConst {

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
