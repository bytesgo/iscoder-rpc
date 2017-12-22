package com.iscoder.scf.protocol.entity;

/**
 * SerializeType
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
