/*
 * Copyright 2010 58.com, Inc. SPAT team blog: http://blog.58.com/spat/ website: http://www.58.com
 */

package com.chaboshi.scf.protocol.exception;

/**
 * EmptyQueueException
 *
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class EmptyQueueException extends RuntimeException {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public EmptyQueueException(String err) {
    super(err);
  }

}
