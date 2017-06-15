package com.iscoder.scf.protocol.sdp;

import java.util.List;

import com.iscoder.scf.common.annotation.SCFMember;
import com.iscoder.scf.common.annotation.SCFSerializable;
import com.iscoder.scf.common.entity.KeyValuePair;

/**
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
