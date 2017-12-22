/**
 * 
 */

package com.chaboshi.scf.demo.benchmark;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

import com.iscoder.scf.client.SCFInit;
import com.iscoder.scf.client.proxy.builder.ProxyFactory;
import com.iscoder.scf.demo.IHelloService;
import com.iscoder.scf.demo.IUserService;

/**
 * @author lee
 */
public class BenchmarkClient extends AbstractBenchmarkClient {

  private static int threadNum = 32;

  public static void main(String[] args) throws InterruptedException {
    runtime = 60;
    if (args != null && args.length > 0) {
      runtime = Long.parseLong(args[0]);
      if (args.length > 1) {
        threadNum = Integer.parseInt(args[1]);
      }
    }
    SCFInit.init(Thread.currentThread().getContextClassLoader().getResource("conf/scf.config").getPath());
    CyclicBarrier barrier = new CyclicBarrier(threadNum);
    CountDownLatch countDownlatch = new CountDownLatch(threadNum);
    IUserService userService = ProxyFactory.create(IUserService.class, "tcp://demo/UserService");
    long endTime = System.currentTimeMillis() + runtime * 1000;
    System.out.println(
        "ready to start client benchmark, benchmark will end at:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(endTime)));
    benchmarkRunnables = new ArrayList<BenchmarkRunnable>(threadNum);

    for (int i = 0; i < threadNum; i++) {
      BenchmarkRunnable benchmarkRunnable = new BenchmarkRunnableImpl(barrier, countDownlatch, userService, endTime);
      benchmarkRunnables.add(benchmarkRunnable);
    }

    for (int i = 0, size = benchmarkRunnables.size(); i < size; i++) {
      new Thread(benchmarkRunnables.get(i), "benchmark-" + i).start();
    }

    countDownlatch.await();
    show();
    System.exit(0);
  }

}
