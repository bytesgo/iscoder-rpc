package com.github.leeyazhou.scf.server.monitor;

import com.github.leeyazhou.scf.server.bootstrap.Main;

public class StartTest {
  public static void main(String args[]) throws Exception {
    String[] str = new String[] { "-Dscf.service.name=demo" };
    Main.main(str);
  }
}
