package com.github.leeyazhou.scf.core.exception;

public class NotFoundServiceException extends SCFException {
  private static final long serialVersionUID = 1L;

  public NotFoundServiceException() {
    super("服务器端找不到指定的服务!");
  }

  public NotFoundServiceException(String message) {
    super(message);
    this.setErrCode(ReturnType.NOT_FOUND_SERVICE_EXCEPTION);
  }
}
