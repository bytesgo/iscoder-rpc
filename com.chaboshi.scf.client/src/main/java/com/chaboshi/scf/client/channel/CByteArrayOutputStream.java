/*
 * Copyright 2010 www.58.com, Inc.
 * @author Service Platform Architecture Team mail: spat@58.com web: http://www.58.com
 */
package com.chaboshi.scf.client.channel;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;

/**
 * CByteArrayOutputStream
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class CByteArrayOutputStream extends ByteArrayOutputStream {

  public byte[] toByteArray(int index, int len) {
    return Arrays.copyOfRange(buf, index, Math.min(index + len, size()));
  }
}
