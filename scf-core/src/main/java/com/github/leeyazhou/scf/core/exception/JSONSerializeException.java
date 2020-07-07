package com.github.leeyazhou.scf.core.exception;

public class JSONSerializeException extends SCFException {
  private static final long serialVersionUID = 1L;

  public JSONSerializeException() {
    super("服务器端数据JSON序列化错误!");
  }

  public JSONSerializeException(String message) {
    super(message);
    this.setErrCode(ReturnType.JSON_SERIALIZE_EXCEPTION);
  }
}
