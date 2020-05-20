package com.github.leeyazhou.scf.protocol.sdp;

import com.github.leeyazhou.scf.core.annotation.SCFMember;
import com.github.leeyazhou.scf.core.annotation.SCFSerializable;

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
