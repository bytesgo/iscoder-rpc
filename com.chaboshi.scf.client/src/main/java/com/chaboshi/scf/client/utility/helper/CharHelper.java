/*
 * Copyright 2010 www.58.com, Inc.
 * @author Service Platform Architecture Team mail: spat@58.com web: http://www.58.com
 */
package com.chaboshi.scf.client.utility.helper;

/**
 * CharHelper
 *
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class CharHelper {

  public static String subString(String source, int startIndex, int count) {
    if (source == null || source.equals("")) {
      return null;
    }
    if (source.length() - startIndex > count) {
      count = source.length() - startIndex;
    }
    if (startIndex <= 0) {
      startIndex = 0;
    }
    return source.substring(startIndex, count);
  }
}
