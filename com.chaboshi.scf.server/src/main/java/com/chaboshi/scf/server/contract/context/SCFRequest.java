/*
 * Copyright 2010 58.com, Inc.
 */

package com.chaboshi.scf.server.contract.context;

import com.chaboshi.scf.protocol.sfp.v1.Protocol;

/**
 * SCF request entity
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 * 
 *         <a href="http://blog.58.com/spat/">blog</a> <a href="http://www.58.com">website</a>
 * 
 */
public class SCFRequest {

  private Protocol protocol;

  private byte[] requestBuffer;

  public SCFRequest() {

  }

  public SCFRequest(Protocol protocol, byte[] buf) {
    super();
    this.protocol = protocol;
    this.requestBuffer = buf;
  }

  public Protocol getProtocol() {
    return protocol;
  }

  public void setProtocol(Protocol protocol) {
    this.protocol = protocol;
  }

  public void setRequestBuffer(byte[] requestBuffer) {
    this.requestBuffer = requestBuffer;
  }

  public byte[] getRequestBuffer() {
    return requestBuffer;
  }
}