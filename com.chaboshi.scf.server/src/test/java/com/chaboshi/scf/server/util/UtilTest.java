package com.chaboshi.scf.server.util;

import org.junit.Test;

public class UtilTest {

  @Test
  public void testGetPN() {
    org.junit.Assert.assertEquals("List<Entity>", Util.getSimpleParaName("List<com.abc.Entity>"));
    org.junit.Assert.assertEquals("List<Entity>", Util.getSimpleParaName("List<Entity>"));
    org.junit.Assert.assertEquals("Info", Util.getSimpleParaName("com.bj58.Info"));
    org.junit.Assert.assertEquals("Map<Info,String>", Util.getSimpleParaName("java.util.Map<com.bj58.Info, String>"));
    org.junit.Assert.assertEquals("Integer", Util.getSimpleParaName("Integer"));
  }
}
