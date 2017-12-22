package com.iscoder.scf.common.exception;

public class ServiceException extends RemoteException {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public ServiceException() {
    super("服务器端服务出错!");
  }

  public ServiceException(String message) {
    super(message);
    this.setErrCode(ReturnType.SERVICE_EXCEPTION);
  }
}
