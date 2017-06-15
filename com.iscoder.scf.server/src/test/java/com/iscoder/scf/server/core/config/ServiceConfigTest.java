package com.iscoder.scf.server.core.config;

import org.junit.Assert;
import org.junit.Test;

import com.iscoder.scf.server.RootPath;
import com.iscoder.scf.server.contract.context.ServiceConfig;

public class ServiceConfigTest {

  @Test
  public void testCreateInstance() throws Exception {

    ServiceConfig sc = ServiceConfig.getServiceConfig(RootPath.projectRootPath + "config/scf_config.xml");

    Assert.assertEquals("", sc.getServiceName());
    Assert.assertEquals("utf-8", sc.getString("scf.encoding"));
    Assert.assertEquals("com.iscoder.scf.server.core.init.SerializerInit", sc.getString("scf.init"));

    // socket
    Assert.assertEquals("0.0.0.0", sc.getString("scf.server.tcp.listenIP"));
    Assert.assertEquals(9090, sc.getInt("scf.server.tcp.listenPort"));
    Assert.assertEquals(60, sc.getInt("scf.server.tcp.workerCount"));
    Assert.assertEquals("com.iscoder.scf.server.core.communication.tcp.SocketServer", sc.getString("scf.server.tcp.implement"));
    Assert.assertEquals(true, sc.getBoolean("scf.server.tcp.enable"));
    Assert.assertEquals(1, sc.getList("scf.server.tcp.filter.request", ",").size());
    Assert.assertEquals("com.iscoder.scf.server.core.filter.ProtocolParseFilter",
        sc.getList("scf.server.tcp.filter.request", ",").get(0));
    Assert.assertEquals(1, sc.getList("scf.server.tcp.filter.response", ",").size());
    Assert.assertEquals("com.iscoder.scf.server.core.filter.ProtocolCreateFilter",
        sc.getList("scf.server.tcp.filter.response", ",").get(0));
    Assert.assertEquals(65536, sc.getInt("scf.server.tcp.receiveBufferSize"));
    Assert.assertEquals(65536, sc.getInt("scf.server.tcp.sendBufferSize"));
    Assert.assertEquals(524288, sc.getInt("scf.server.tcp.frameMaxLength"));

    // http
    Assert.assertEquals("0.0.0.0", sc.getString("scf.server.http.listenIP"));
    Assert.assertEquals(8080, sc.getInt("scf.server.http.listenPort"));
    Assert.assertEquals(200, sc.getInt("scf.server.http.workerCount"));
    Assert.assertEquals("com.iscoder.scf.server.core.communication.http.HttpServer", sc.getString("scf.server.http.implement"));
    Assert.assertEquals(false, sc.getBoolean("scf.server.http.enable"));
    Assert.assertEquals(null, sc.getList("scf.server.http.filter.request", ","));
    Assert.assertEquals(null, sc.getList("scf.server.http.filter.response", ","));
    Assert.assertEquals(65536, sc.getInt("scf.server.http.receiveBufferSize"));
    Assert.assertEquals(65536, sc.getInt("scf.server.http.sendBufferSize"));
    Assert.assertEquals(524288, sc.getInt("scf.server.http.frameMaxLength"));

    // telnet
    Assert.assertEquals("0.0.0.0", sc.getString("scf.server.telnet.listenIP"));
    Assert.assertEquals(7070, sc.getInt("scf.server.telnet.listenPort"));
    Assert.assertEquals(1, sc.getInt("scf.server.telnet.workerCount"));
    Assert.assertEquals("com.iscoder.scf.server.core.communication.telnet.TelnetServer", sc.getString("scf.server.telnet.implement"));
    Assert.assertEquals(true, sc.getBoolean("scf.server.telnet.enable"));
    Assert.assertEquals(null, sc.getList("scf.server.telnet.filter.request", ","));
    Assert.assertEquals(null, sc.getList("scf.server.telnet.filter.response", ","));
    Assert.assertEquals(65536, sc.getInt("scf.server.telnet.receiveBufferSize"));
    Assert.assertEquals(65536, sc.getInt("scf.server.telnet.sendBufferSize"));
    Assert.assertEquals(524288, sc.getInt("scf.server.telnet.frameMaxLength"));
  }

  @Test
  public void testCreateInstanceII() throws Exception {

    ServiceConfig sc = ServiceConfig.getServiceConfig(RootPath.projectRootPath + "config/scf_config.xml",
        RootPath.projectRootPath + "config/demo_config.xml");

    Assert.assertEquals(null, sc.getString("scf.service.name.abc"));
    Assert.assertEquals("demo", sc.getServiceName());
    Assert.assertEquals("utf-8", sc.getString("scf.encoding"));
    Assert.assertEquals("com.iscoder.scf.server.core.init.SerializerInit,com.iscoder.zhaopin.scf.pusher.zpt.components.Global",
        sc.getString("scf.init"));

    // socket
    Assert.assertEquals("192.168.1.123", sc.getString("scf.server.tcp.listenIP"));
    Assert.assertEquals(10000, sc.getInt("scf.server.tcp.listenPort"));
    Assert.assertEquals(60, sc.getInt("scf.server.tcp.workerCount"));
    Assert.assertEquals("com.iscoder.scf.server.core.communication.tcp.SocketServer", sc.getString("scf.server.tcp.implement"));
    Assert.assertEquals(true, sc.getBoolean("scf.server.tcp.enable"));
    Assert.assertEquals(1, sc.getList("scf.server.tcp.filter.request", ",").size());
    Assert.assertEquals("com.iscoder.scf.server.core.filter.ProtocolParseFilter",
        sc.getList("scf.server.tcp.filter.request", ",").get(0));
    Assert.assertEquals(1, sc.getList("scf.server.tcp.filter.response", ",").size());
    Assert.assertEquals("com.iscoder.scf.server.core.filter.ProtocolCreateFilter",
        sc.getList("scf.server.tcp.filter.response", ",").get(0));
    Assert.assertEquals(65536, sc.getInt("scf.server.tcp.receiveBufferSize"));
    Assert.assertEquals(65536, sc.getInt("scf.server.tcp.sendBufferSize"));
    Assert.assertEquals(524288, sc.getInt("scf.server.tcp.frameMaxLength"));

    // http
    Assert.assertEquals("0.0.0.0", sc.getString("scf.server.http.listenIP"));
    Assert.assertEquals(8080, sc.getInt("scf.server.http.listenPort"));
    Assert.assertEquals(200, sc.getInt("scf.server.http.workerCount"));
    Assert.assertEquals("com.iscoder.scf.server.core.communication.http.HttpServer", sc.getString("scf.server.http.implement"));
    Assert.assertEquals(true, sc.getBoolean("scf.server.http.enable"));
    Assert.assertEquals(null, sc.getList("scf.server.http.filter.request", ","));
    Assert.assertEquals(null, sc.getList("scf.server.http.filter.response", ","));
    Assert.assertEquals(65536, sc.getInt("scf.server.http.receiveBufferSize"));
    Assert.assertEquals(65536, sc.getInt("scf.server.http.sendBufferSize"));
    Assert.assertEquals(524288, sc.getInt("scf.server.http.frameMaxLength"));

    // telnet
    Assert.assertEquals("0.0.0.0", sc.getString("scf.server.telnet.listenIP"));
    Assert.assertEquals(7070, sc.getInt("scf.server.telnet.listenPort"));
    Assert.assertEquals(1, sc.getInt("scf.server.telnet.workerCount"));
    Assert.assertEquals("com.iscoder.scf.server.core.communication.telnet.TelnetServer", sc.getString("scf.server.telnet.implement"));
    Assert.assertEquals(true, sc.getBoolean("scf.server.telnet.enable"));
    Assert.assertEquals(null, sc.getList("scf.server.telnet.filter.request", ","));
    Assert.assertEquals(null, sc.getList("scf.server.telnet.filter.response", ","));
    Assert.assertEquals(65536, sc.getInt("scf.server.telnet.receiveBufferSize"));
    Assert.assertEquals(65536, sc.getInt("scf.server.telnet.sendBufferSize"));
    Assert.assertEquals(524288, sc.getInt("scf.server.telnet.frameMaxLength"));
  }

}