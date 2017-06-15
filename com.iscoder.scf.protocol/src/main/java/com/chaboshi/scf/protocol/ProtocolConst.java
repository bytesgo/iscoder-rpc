package com.chaboshi.scf.protocol;

/**
 * ProtocolConst
 */
public class ProtocolConst {

  /**
   *
   */
  public static final byte[] P_START_TAG = new byte[] { 18, 17, 13, 10, 9 };

  /**
   *
   */
  public static final byte[] P_END_TAG = new byte[] { 9, 10, 13, 17, 18 };

  /**
   * 获得协义的版本号
   * 
   * @param buffer
   * @return
   */
  public static int getVersion(byte[] buffer) {
    return buffer[0];
  }

  /**
   * 解析协义
   * 
   * @param buffer
   * @return
   * @throws Exception
   */
  public static Object fromBytes(byte[] buffer) throws Exception {
    if (buffer != null && buffer.length > 0) {
      int version = buffer[0];
      if (version == com.chaboshi.scf.protocol.sfp.Protocol.VERSION) {
        return com.chaboshi.scf.protocol.sfp.Protocol.fromBytes(buffer);
      }
    }

    throw new Exception("不完整的二进制流");
  }

  /**
   * 
   * @param buf
   * @return
   */
  public static boolean checkHeadDelimiter(byte[] buf) {
    if (buf.length == ProtocolConst.P_START_TAG.length) {
      for (int i = 0; i < buf.length; i++) {
        if (buf[i] != ProtocolConst.P_START_TAG[i]) {
          return false;
        }
      }
      return true;
    } else {
      return false;
    }
  }
}