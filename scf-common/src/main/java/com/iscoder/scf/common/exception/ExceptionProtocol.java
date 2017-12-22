package com.iscoder.scf.common.exception;

import com.iscoder.scf.common.annotation.SCFMember;
import com.iscoder.scf.common.annotation.SCFSerializable;

@SCFSerializable(name = "ExceptionProtocol")
public class ExceptionProtocol {

  @SCFMember
  private int errorCode;
  @SCFMember
  private String toIP;
  @SCFMember
  private String fromIP;
  @SCFMember
  private String errorMsg;

  public int getErrorCode() {
    return errorCode;
  }

  public void setErrorCode(int errorCode) {
    this.errorCode = errorCode;
  }

  public String getErrorMsg() {
    return errorMsg;
  }

  public void setErrorMsg(String errorMsg) {
    this.errorMsg = errorMsg;
  }

  public String getFromIP() {
    return fromIP;
  }

  public void setFromIP(String fromIP) {
    this.fromIP = fromIP;
  }

  public String getToIP() {
    return toIP;
  }

  public void setToIP(String toIP) {
    this.toIP = toIP;
  }
}
