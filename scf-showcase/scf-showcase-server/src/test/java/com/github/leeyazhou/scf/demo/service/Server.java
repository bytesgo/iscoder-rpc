/**
 * 
 */
package com.github.leeyazhou.scf.demo.service;

import java.util.Set;

import org.junit.Test;

import com.github.leeyazhou.scf.core.annotation.ServiceBehavior;
import com.github.leeyazhou.scf.core.scanner.DefaultClassScanner;

/**
 * @author lee_y
 *
 */
public class Server {

  @Test
  public void main() throws Exception {
    String userDir = Server.class.getResource("/").getPath();
    System.setProperty("user.dir", userDir);
    com.github.leeyazhou.scf.server.bootstrap.Main.main(new String[] { "-dscf.service.name=showcase", "-Dscf.service.name=showcase" });
  }

  @Test
  public void test() {
    Set<Class<?>> a = DefaultClassScanner.getInstance().getClassListByAnnotation("", ServiceBehavior.class);
    System.out.println(a);
  }

}
