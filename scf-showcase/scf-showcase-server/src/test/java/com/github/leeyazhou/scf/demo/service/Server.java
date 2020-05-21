/**
 * 
 */
package com.github.leeyazhou.scf.demo.service;

import java.util.Set;
import org.junit.Test;
import com.github.leeyazhou.scanner.ClassScanner;
import com.github.leeyazhou.scanner.DefaultClassScanner;
import com.github.leeyazhou.scanner.Scanner;
import com.github.leeyazhou.scf.core.annotation.ServiceBehavior;

/**
 * @author lee_y
 *
 */
public class Server {

  @Test
  public void main() throws Exception {
    String userDir = Server.class.getResource("/").getPath();
    System.setProperty("user.dir", userDir);
    com.github.leeyazhou.scf.server.bootstrap.Main
        .main(new String[] {"-dscf.service.name=showcase", "-Dscf.service.name=showcase"});
  }

  @Test
  public void test() {
    ClassScanner classScanner = new DefaultClassScanner(Scanner.builder().setBasePackage(""));
    Set<Class<?>> a = classScanner.getByAnnotation(ServiceBehavior.class);
    System.out.println(a);
  }

}
