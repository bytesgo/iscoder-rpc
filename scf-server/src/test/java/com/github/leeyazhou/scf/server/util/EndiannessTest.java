package com.github.leeyazhou.scf.server.util;

import org.junit.Assert;
import org.junit.Test;

import com.github.leeyazhou.scf.server.util.EndiannessUtil;

public class EndiannessTest {

  @Test
  public void testBytesToIntLittleEndian() {
    byte[] buf = new byte[] { 10, 0, 0, 0 };
    int n = EndiannessUtil.bytesToIntLittleEndian(buf);
    Assert.assertEquals(10, n);

    byte[] buf2 = new byte[] { -97, 0, 0, 10 };
    int n2 = EndiannessUtil.bytesToIntLittleEndian(buf2);
    Assert.assertEquals(167772319, n2);
  }

  @Test
  public void testIntToBytesLittleEndian() {
    byte[] buf = EndiannessUtil.intToBytesLittleEndian(1);
    Assert.assertEquals(1, buf[0]);
    Assert.assertEquals(0, buf[1]);
    Assert.assertEquals(0, buf[2]);
    Assert.assertEquals(0, buf[3]);

    byte[] buf2 = EndiannessUtil.intToBytesLittleEndian(1891);
    Assert.assertEquals(99, buf2[0]);
    Assert.assertEquals(7, buf2[1]);
    Assert.assertEquals(0, buf2[2]);
    Assert.assertEquals(0, buf2[3]);

    Assert.assertEquals(1891, EndiannessUtil.bytesToIntLittleEndian(buf2));
  }

  @Test
  public void testBytesToIntBigEndian() {
    byte[] buf2 = new byte[] { -97, 0, 0, 10 };
    int n2 = EndiannessUtil.bytesToIntBigEndian(buf2);
    Assert.assertEquals(-1627389942, n2);
  }

  @Test
  public void testIntToBytesBigEndian() {

    byte[] buf = EndiannessUtil.intToBytesBigEndian(10);
    Assert.assertEquals(0, buf[0]);
    Assert.assertEquals(0, buf[1]);
    Assert.assertEquals(0, buf[2]);
    Assert.assertEquals(10, buf[3]);

    byte[] buf2 = EndiannessUtil.intToBytesBigEndian(1891);
    Assert.assertEquals(0, buf2[0]);
    Assert.assertEquals(0, buf2[1]);
    Assert.assertEquals(7, buf2[2]);
    Assert.assertEquals(99, buf2[3]);

    Assert.assertEquals(1891, EndiannessUtil.bytesToIntBigEndian(buf2));
  }
}