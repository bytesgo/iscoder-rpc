package com.chaboshi.scf.server.util;

import org.junit.Assert;
import org.junit.Test;

import com.chaboshi.scf.server.contract.context.Global;
import com.chaboshi.scf.server.contract.context.ServiceConfig;

public class IPTableTest {

  static {
    try {
      Global.getSingleton().setServiceConfig(ServiceConfig.getServiceConfig("D:/workspace/java/bj58.scf.server/config/scf_config.xml"));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testIsAllowI() {
    Global.getSingleton().getServiceConfig().set("scf.iptable.allow.iplist", "192.168.9.*,192.168.11.*");
    Global.getSingleton().getServiceConfig().set("scf.iptable.forbid.iplist", "192.168.9.199,192.168.9.241");
    IPTable.init();

    Assert.assertEquals(false, IPTable.isAllow("192.168.111.9"));
    Assert.assertEquals(true, IPTable.isAllow("192.168.9.9"));
    Assert.assertEquals(false, IPTable.isAllow("192.168.110.123"));
    Assert.assertEquals(false, IPTable.isAllow("192.168.9.199"));
    Assert.assertEquals(false, IPTable.isAllow("192.168.9.241"));
    Assert.assertEquals(true, IPTable.isAllow("192.168.11.9"));
    Assert.assertEquals(true, IPTable.isAllow("192.168.11.129"));
  }

  @Test
  public void testIsAllowII() {
    Global.getSingleton().getServiceConfig().set("scf.iptable.allow.iplist", "");
    Global.getSingleton().getServiceConfig().set("scf.iptable.forbid.iplist", "192.168.9.199,192.168.9.241");
    IPTable.init();

    Assert.assertEquals(true, IPTable.isAllow("192.168.111.9"));
    Assert.assertEquals(true, IPTable.isAllow("192.168.9.9"));
    Assert.assertEquals(true, IPTable.isAllow("192.168.110.123"));
    Assert.assertEquals(false, IPTable.isAllow("192.168.9.199"));
    Assert.assertEquals(false, IPTable.isAllow("192.168.9.241"));
    Assert.assertEquals(true, IPTable.isAllow("192.168.11.9"));
    Assert.assertEquals(true, IPTable.isAllow("192.168.11.129"));
  }

  @Test
  public void testIsAllowIII() {
    Global.getSingleton().getServiceConfig().set("scf.iptable.allow.iplist", "192.168.9.*,192.168.11.*");
    Global.getSingleton().getServiceConfig().set("scf.iptable.forbid.iplist", "");
    IPTable.init();

    Assert.assertEquals(false, IPTable.isAllow("192.168.111.9"));
    Assert.assertEquals(true, IPTable.isAllow("192.168.9.9"));
    Assert.assertEquals(false, IPTable.isAllow("192.168.110.123"));
    Assert.assertEquals(true, IPTable.isAllow("192.168.9.199"));
    Assert.assertEquals(true, IPTable.isAllow("192.168.9.241"));
    Assert.assertEquals(true, IPTable.isAllow("192.168.11.9"));
    Assert.assertEquals(true, IPTable.isAllow("192.168.11.129"));
  }

  @Test
  public void testIsAllowIV() {
    Global.getSingleton().getServiceConfig().set("scf.iptable.allow.iplist", "");
    Global.getSingleton().getServiceConfig().set("scf.iptable.forbid.iplist", "");
    IPTable.init();

    Assert.assertEquals(true, IPTable.isAllow("192.168.111.9"));
    Assert.assertEquals(true, IPTable.isAllow("192.168.9.9"));
    Assert.assertEquals(true, IPTable.isAllow("192.168.110.123"));
    Assert.assertEquals(true, IPTable.isAllow("192.168.9.199"));
    Assert.assertEquals(true, IPTable.isAllow("192.168.9.241"));
    Assert.assertEquals(true, IPTable.isAllow("192.168.11.9"));
    Assert.assertEquals(true, IPTable.isAllow("192.168.11.129"));
  }
}