package com.iscoder.scf.server.core.convert;

import org.junit.Assert;
import org.junit.Test;

public class CustomBinaryConvertTest {

  @Test
  public void testConvertToTObjectClassOfQ() throws Exception {
    Object l = new long[] { 1L, 2L, 3L };
    Convert convert = new SCFConvert();
    long[] ary = (long[]) convert.convertToT(l, long[].class);
    Assert.assertEquals(1, ary[0]);
    Assert.assertEquals(2, ary[1]);
    Assert.assertEquals(3, ary[2]);

    Object LL = new Long[] { 1L, 2L };
    Long[] sl = (Long[]) LL;
    System.out.println(sl);
  }

}