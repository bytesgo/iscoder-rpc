package com.github.leeyazhou.scf.demo.impl;

import com.github.leeyazhou.scf.server.core.communication.Server;

public class Application {

  public static void main(String[] args) throws Exception {
    String userDir = Server.class.getResource("/").getPath();
    System.setProperty("scf.home", userDir);
    com.github.leeyazhou.scf.server.bootstrap.Main
        .main(new String[] {"-dscf.service.name=showcase", "-Dscf.service.name=showcase"});
  
  }
}
