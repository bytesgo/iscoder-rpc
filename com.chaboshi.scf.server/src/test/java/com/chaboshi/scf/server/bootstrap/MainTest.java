package com.chaboshi.scf.server.bootstrap;

import org.junit.Test;

import com.chaboshi.scf.server.bootstrap.Main;

public class MainTest {

  @Test
  public void testMain() throws Exception {
    Main.main(new String[] { "demo" });
  }

}