package com.github.leeyazhou.scf.server.proxycreater.wsdl;

import java.util.List;

import com.github.leeyazhou.scf.core.entity.Out;

public class TestMethoInfo {

  public int add(int a, int b) {
    return a + b;
  }

  public void empty() {

  }

  public List<String> fun1(List<String> list) {
    return null;
  }

  public String[] fun2(String[] ary) {
    return null;
  }

  public TestMethoInfo fun3(TestMethoInfo tm) {
    return null;
  }

  public Out<String> fun4(Out<String> outPara) {
    return null;
  }

}