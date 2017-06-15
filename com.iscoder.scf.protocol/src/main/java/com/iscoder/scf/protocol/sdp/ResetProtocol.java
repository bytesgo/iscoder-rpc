package com.iscoder.scf.protocol.sdp;

import com.iscoder.scf.common.annotation.SCFMember;
import com.iscoder.scf.common.annotation.SCFSerializable;

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
