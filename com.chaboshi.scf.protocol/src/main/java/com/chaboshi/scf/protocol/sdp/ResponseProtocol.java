/*
 * Copyright 2010 58.com, Inc. SPAT team blog: http://blog.58.com/spat/ website: http://www.58.com
 */
package com.chaboshi.scf.protocol.sdp;

import com.chaboshi.common.annotation.SCFMember;
import com.chaboshi.common.annotation.SCFSerializable;

/**
 * ResponseProtocol
 *
 * @author Service Platform Architecture Team (spat@58.com)
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
