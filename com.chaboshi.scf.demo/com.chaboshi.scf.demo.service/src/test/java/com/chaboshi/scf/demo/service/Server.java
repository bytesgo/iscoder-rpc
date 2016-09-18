/**
 * 
 */
package com.chaboshi.scf.demo.service;

import com.chaboshi.scf.server.bootstrap.Main;

/**
 * @author lee_y
 *
 */
public class Server {

  public static void main(String[] args) throws Exception {
    Main.main(new String[] { "-dscf.service.name=demo", "-Dscf.service.name=demo" });
  }
}
