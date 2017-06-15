package com.chaboshi.common.exception;

/**
 * 服务重启异常
 * 
 */
public class RebootException extends RemoteException {
  private static final long serialVersionUID = 1L;

  public RebootException() {
    super("服务正在重启!");
  }

  public RebootException(String message) {
    super(message);
    this.setErrCode(ReturnType.REBOOT_EXCEPTION);
  }
}
