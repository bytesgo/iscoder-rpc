package com.github.leeyazhou.scf.protocol.sdp;

import java.util.List;

import com.github.leeyazhou.scf.core.annotation.SCFMember;
import com.github.leeyazhou.scf.core.annotation.SCFSerializable;
import com.github.leeyazhou.scf.core.entity.KeyValuePair;

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
