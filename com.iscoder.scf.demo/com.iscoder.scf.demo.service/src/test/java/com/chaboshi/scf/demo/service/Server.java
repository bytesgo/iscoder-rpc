/**
 * 
 */
package com.chaboshi.scf.demo.service;

import java.util.Set;

import org.junit.Test;

import com.chaboshi.common.annotation.ServiceBehavior;
import com.chaboshi.common.scanner.DefaultClassScanner;

/**
 * @author lee_y
 *
 */
public class Server {

  @Test
  public void main() throws Exception {
    String userDir = Server.class.getResource("/").getPath();
    System.setProperty("user.dir", userDir);
    com.chaboshi.scf.server.bootstrap.Main.main(new String[] { "-dscf.service.name=demo", "-Dscf.service.name=demo" });
  }

  @Test
  public void test() {
    Set<Class<?>> a = DefaultClassScanner.getInstance().getClassListByAnnotation("", ServiceBehavior.class);
    System.out.println(a);
  }

}
