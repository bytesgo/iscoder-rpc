package com.chaboshi.scf.protocol.sdp;

import com.chaboshi.common.annotation.SCFMember;
import com.chaboshi.common.annotation.SCFSerializable;

@SCFSerializable(name = "ResetProtocol")
public class ResetProtocol {
  @SCFMember
  private String msg;

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }
}
