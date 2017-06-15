package com.chaboshi.common.exception;

/**
 * EmptyQueueException
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
