package com.iscoder.scf.common.entity;

/**
 * 
 */

public class Out<T> {
  private T outPara;

  public void setOutPara(T t) {
    this.outPara = t;
  }

  public T getOutPara() {
    return outPara;
  }

  /*
   */
  @Deprecated
  public Out() {

  }

  public Out(T t) {
    setOutPara(t);
  }
}