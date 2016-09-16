/*
 * Copyright 2010 58.com, Inc.
 *
 *
 */

package com.chaboshi.scf.server.contract.entity;

/**
 * 否决的，请使用 com.bj58.spat.scf.protocol.sdp.Out a class for packing out/ref
 * parameter
 * 
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 * 
 *         <a href="http://blog.58.com/spat/">blog</a>
 *         <a href="http://www.58.com">website</a>
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