package com.chaboshi.scf.server.monitor;

import com.chaboshi.scf.server.bootstrap.Main;

public class StartTest {
  public static void main(String args[]) throws Exception {
    String[] str = new String[] { "-Dscf.service.name=demo" };
    Main.main(str);
  }
}
