package com.github.leeyazhou.scf.core.exception;

public class ProtocolException extends SCFException {
  private static final long serialVersionUID = 1L;

  public ProtocolException() {
    super("服务器端协议出错!");
  }

  public ProtocolException(String message) {
    super(message);
    this.setErrCode(ReturnType.PROTOCOL);
  }
}
