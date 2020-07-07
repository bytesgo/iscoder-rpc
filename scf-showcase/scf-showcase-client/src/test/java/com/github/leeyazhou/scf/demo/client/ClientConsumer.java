package com.github.leeyazhou.scf.demo.client;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import org.junit.Before;
import org.junit.Test;
import com.github.leeyazhou.scf.client.SCFInit;
import com.github.leeyazhou.scf.client.proxy.builder.ProxyFactory;
import com.github.leeyazhou.scf.demo.IHelloService;

public class ClientConsumer {

  @Before
  public void before() {
    SCFInit.init(Thread.currentThread().getContextClassLoader().getResource("conf/scf.config").getPath());

  }

  private IHelloService helloService = ProxyFactory.create(IHelloService.class, "tcp://demo/HelloService");

  @Test
  public void testOne() {
    String ret = helloService.say("CrPC");
    System.out.println("ret: " + ret);
  }

  @Test
  public void testSay() throws InterruptedException {
    long start = System.currentTimeMillis();
    int concurrent = 60;
    int size = 1000;
    CyclicBarrier cyclicBarrier = new CyclicBarrier(concurrent);
    CountDownLatch countDownLatch = new CountDownLatch(concurrent);

    int count = 0;
    while (count++ < concurrent) {
      new BenchTask(cyclicBarrier, countDownLatch, size).start();
    }
    countDownLatch.await();
    System.out.println("耗时:" + (System.currentTimeMillis() - start));
  }

  public void benchRun(int concurrent, int requestSize) {

  }

  class BenchTask extends Thread {

    private CyclicBarrier cyclicBarrier;
    private CountDownLatch countDownLatch;
    private int size;

    public BenchTask(CyclicBarrier cyclicBarrier, CountDownLatch countDownLatch, int size) {
      this.cyclicBarrier = cyclicBarrier;
      this.countDownLatch = countDownLatch;
      this.size = size;
    }

    @Override
    public void run() {

      try {
        cyclicBarrier.await();
        for (int i = 0; i < size; i++) {
          System.out.println("开始等待:" + getName());
          String result = helloService.say("李亚州--" + i);
          System.out.println("执行结果:" + result);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
      System.out.println("执行结束 :" + getName());
      countDownLatch.countDown();

    }
  }

}
