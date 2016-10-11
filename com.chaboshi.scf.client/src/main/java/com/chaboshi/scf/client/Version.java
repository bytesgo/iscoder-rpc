/*
 * Copyright 2010 www.58.com, Inc.
 * @author Service Platform Architecture Team mail: spat@58.com web: http://www.58.com
 */
package com.chaboshi.scf.client;

/**
 * Provides the version information of SCF.
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public final class Version {
  /** The version identifier. */
  public static final String ID = SCFConst.VERSION_FLAG;

  /** Prints out the version identifier to stdout. */
  public static void main(String[] args) {
    System.out.println(ID);
  }

  private Version() {
    super();
  }
}
