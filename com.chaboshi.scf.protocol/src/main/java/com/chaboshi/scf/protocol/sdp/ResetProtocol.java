package com.chaboshi.scf.protocol.sdp;

import com.chaboshi.scf.serializer.component.annotation.SCFMember;
import com.chaboshi.scf.serializer.component.annotation.SCFSerializable;

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
