/**
 * 
 */
package com.chaboshi.scf.demo.client;

import com.chaboshi.scf.client.SCFInit;
import com.chaboshi.scf.client.proxy.builder.ProxyFactory;
import com.chaboshi.scf.demo.contract.IHelloService;

/**
 * @author lee_y
 *
 */
public class ClientConsumer {

  static {
    SCFInit.init("../conf/scf.config");
  }

  private static IHelloService helloService = ProxyFactory.create(IHelloService.class, "tcp://demo/HelloService");

  public static void main(String[] args) {
    String result = helloService.say("李亚州");
    System.out.println("说话内容 : " + result);
  }

}
