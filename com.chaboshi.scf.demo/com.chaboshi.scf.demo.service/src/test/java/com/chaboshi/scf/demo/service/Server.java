/**
 * 
 */
package com.chaboshi.scf.demo.service;

/**
 * @author lee_y
 *
 */
public class Server {

  public static void main(String[] args) throws Exception {
    String userDir = Server.class.getResource("/").getPath();
    System.setProperty("user.dir", userDir);
    com.chaboshi.scf.server.bootstrap.Main.main(new String[] { "-dscf.service.name=demo", "-Dscf.service.name=demo" });
  }
}
