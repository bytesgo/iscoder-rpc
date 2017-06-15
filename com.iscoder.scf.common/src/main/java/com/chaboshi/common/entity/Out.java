package com.chaboshi.common.entity;

/**
 * 否决的，请使用 com.bj58.spat.scf.protocol.sdp.Out a class for packing out/ref parameter
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
   * 否决的，请使用 com.bj58.spat.scf.protocol.sdp.Out
   */
  @Deprecated
  public Out() {

  }

  /*
   * 否决的，请使用 com.bj58.spat.scf.protocol.sdp.Out
   */
  public Out(T t) {
    setOutPara(t);
  }
}