/*
 * Copyright 2010 58.com, Inc. SPAT team blog: http://blog.58.com/spat/ website: http://www.58.com
 */
package com.chaboshi.scf.protocol.entity;

/**
 * CompressType
 *
 * @author Service Platform Architecture Team (spat@58.com)
 */
public enum CompressType {

  /**
   * 不压缩(无意义编号为0)
   */
  UnCompress(0),

  /**
   * 7zip
   */
  SevenZip(1),

  /**
   * DES加密
   */
  DES(2);

  private final int num;

  public int getNum() {
    return num;
  }

  private CompressType(int num) {
    this.num = num;
  }

  public static CompressType getCompressType(int num) throws Exception {
    for (CompressType type : CompressType.values()) {
      if (type.getNum() == num) {
        return type;
      }
    }
    throw new Exception("末知的压缩格式");
  }
}
