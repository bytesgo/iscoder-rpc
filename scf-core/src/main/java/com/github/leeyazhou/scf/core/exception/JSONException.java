package com.github.leeyazhou.scf.core.exception;

public class JSONException extends SCFException {
  private static final long serialVersionUID = 1L;

  public JSONException() {
    super("服务器端JSON错误!!");
  }

  public JSONException(String message) {
    super(message);
    this.setErrCode(ReturnType.JSON_EXCEPTION);
  }
}
