package com.chaboshi.scf.protocol.sdp;

import com.chaboshi.common.annotation.SCFMember;
import com.chaboshi.common.annotation.SCFSerializable;

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
