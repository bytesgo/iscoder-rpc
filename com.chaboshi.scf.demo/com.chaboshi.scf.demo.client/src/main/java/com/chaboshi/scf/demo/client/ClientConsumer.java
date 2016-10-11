/**
 * 
 */
package com.chaboshi.scf.demo.client;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
    long start = System.currentTimeMillis();
    String result = helloService.say("李亚州");
    System.out.println("说话内容 : " + result);
    benchRun(6, 100);

    System.out.println(System.currentTimeMillis() - start);
  }

  public static void benchRun(int concurrent, int requestSize) {
    ExecutorService executorService = Executors.newScheduledThreadPool(concurrent);
    for (int i = 0; i < requestSize; i++) {
      final int j = i;
      executorService.submit(new Runnable() {

        @Override
        public void run() {
          String result = helloService.say("李亚州--" + j);
          System.out.println("client get说话内容 : " + result);
        }
      });
    }
  }

}
