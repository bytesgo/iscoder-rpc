/*
 * Copyright 2010 58.com, Inc. SPAT team blog: http://blog.58.com/spat/ website: http://www.58.com
 */
package com.chaboshi.scf.protocol.sdp;

import java.util.List;

import com.chaboshi.scf.protocol.utility.KeyValuePair;
import com.chaboshi.scf.serializer.component.annotation.SCFMember;
import com.chaboshi.scf.serializer.component.annotation.SCFSerializable;

/**
 * RequestProtocol
 *
 * @author Service Platform Architecture Team (spat@58.com)
 */
@SCFSerializable(name = "RequestProtocol")
public class RequestProtocol {

  @SCFMember
  private String lookup;
  @SCFMember
  private String methodName;
  @SCFMember
  private List<KeyValuePair> paraKVList;

  public RequestProtocol() {
  }

  public RequestProtocol(String lookup, String methodName, List<KeyValuePair> paraKVList) {
    this.lookup = lookup;
    this.methodName = methodName;
    this.paraKVList = paraKVList;
  }

  public String getLookup() {
    return lookup;
  }

  public void setLookup(String lookup) {
    this.lookup = lookup;
  }

  public String getMethodName() {
    return methodName;
  }

  public void setMethodName(String methodName) {
    this.methodName = methodName;
  }

  public List<KeyValuePair> getParaKVList() {
    return paraKVList;
  }

  public void setParaKVList(List<KeyValuePair> paraKVList) {
    this.paraKVList = paraKVList;
  }

  @Override
  public String toString() {
    return "RequestProtocol [lookup=" + lookup + ", methodName=" + methodName + ", paraKVList=" + paraKVList + "]";
  }

}
