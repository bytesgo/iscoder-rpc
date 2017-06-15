/*
 * Copyright 2010 58.com, Inc. SPAT team blog: http://blog.58.com/spat/ website: http://www.58.com
 */
package com.chaboshi.scf.protocol.entity;

/**
 * SerializeType
 *
 * @author Service Platform Architecture Team (spat@58.com)
 */
public enum SerializeType {

  JSON(1),

  JAVABinary(2),

  XML(3),

  SCFBinary(4),

  PB(5),

  KRYO(6);

  private final int num;

  public int getNum() {
    return num;
  }

  private SerializeType(int num) {
    this.num = num;
  }

  public static SerializeType getSerializeType(int num) {
    for (SerializeType type : SerializeType.values()) {
      if (type.getNum() == num) {
        return type;
      }
    }
    return null;
  }
}
