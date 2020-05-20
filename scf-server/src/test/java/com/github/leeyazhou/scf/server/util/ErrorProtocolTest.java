package com.github.leeyazhou.scf.server.util;

import static org.junit.Assert.fail;

import org.junit.Test;

import com.github.leeyazhou.scf.server.util.ExceptionUtil;

public class ErrorProtocolTest {

  // static {
  // try {
  // ServiceConfig.createInstance("D:/serviceframe_v2_II/bin/imc_config.xml");
  // } catch (Exception e) {
  // e.printStackTrace();
  // }
  // }

  @Test
  public void testCreateErrorServiceFrameException() {
    fail("Not yet implemented");
  }

  @Test
  public void testCreateErrorErrorStateStringString() {
    fail("Not yet implemented");
  }

  @Test
  public void testCreateErrorErrorStateStringStringException() {
    fail("Not yet implemented");
  }

  @Test
  public void testCreateErrorException() {
    fail("Not yet implemented");
  }

  @Test
  public void testCreateErrorProtocol() {
    byte[] buf = ExceptionUtil.createErrorProtocol();
    System.out.println("length:" + buf.length);
    for (byte b : buf) {
      System.out.println(b);
    }
  }
}
