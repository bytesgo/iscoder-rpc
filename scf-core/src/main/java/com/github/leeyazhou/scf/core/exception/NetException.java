package com.github.leeyazhou.scf.core.exception;

public class NetException extends SCFException {
  private static final long serialVersionUID = 1L;

  public NetException() {
    super("服务器端网络错误!");
  }

  public NetException(String message) {
    super(message);
    this.setErrCode(ReturnType.NET);
  }
}
