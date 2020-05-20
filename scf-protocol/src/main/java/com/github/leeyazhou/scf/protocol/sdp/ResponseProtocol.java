package com.github.leeyazhou.scf.protocol.sdp;

import com.github.leeyazhou.scf.core.annotation.SCFMember;
import com.github.leeyazhou.scf.core.annotation.SCFSerializable;

/**
 * ResponseProtocol
 *
 */
@SCFSerializable(name = "ResponseProtocol")
public class ResponseProtocol {

  @SCFMember
  private Object result;
  @SCFMember
  private Object[] outpara;

  public ResponseProtocol() {

  }

  public ResponseProtocol(Object result, Object[] outpara) {
    super();
    this.result = result;
    this.outpara = outpara;
  }

  public Object[] getOutpara() {
    return outpara;
  }

  public void setOutpara(Object[] outpara) {
    this.outpara = outpara;
  }

  public Object getResult() {
    return result;
  }

  public void setResult(Object result) {
    this.result = result;
  }
}
