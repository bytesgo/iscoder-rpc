package com.github.leeyazhou.scf.demo.benchmark;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.leeyazhou.scf.demo.IUserService;
import com.github.leeyazhou.scf.demo.model.User;

public class BenchmarkRunnableImpl implements BenchmarkRunnable {
  private static final Logger logger = LoggerFactory.getLogger(BenchmarkRunnableImpl.class);
  private CyclicBarrier barrier;
  private CountDownLatch countDownLatch;
  private IUserService userService;
  private boolean running = true;
  private long endTime;
  private long acceptRequest;
  private long errorRequest;

  private long[] responseSpreads = new long[9];

  public BenchmarkRunnableImpl(CyclicBarrier barrier, CountDownLatch countDownLatch, IUserService userService, long endTime) {
    this.userService = userService;
    this.barrier = barrier;
    this.countDownLatch = countDownLatch;
    this.endTime = endTime;
  }

  @Override
  public void run() {
    try {
      barrier.await();
    } catch (Exception e) {
      e.printStackTrace();
    }
    while (running) {
      long start = System.currentTimeMillis();
      if (start > endTime) {
        running = false;
        break;
      }
      try {
        doRun();
      } catch (Exception e) {
        e.printStackTrace();
        errorRequest++;
      }
      long costTime = System.currentTimeMillis() - start;
      sumResponseTimeSpread(costTime);
    }
    countDownLatch.countDown();
  }

  private void doRun() {

    User user = new User();
    user.setId(1);
    user.setDeleted(true);
    user.setSex(1);
    user.setUsername("查博士 - 1");
    // user.setData(new byte[1000]);
    boolean flag = userService.sayUser(user);
    if (logger.isInfoEnabled()) {
      logger.info(user + ", result ： " + flag);
    }

  }

  private void sumResponseTimeSpread(long responseTime) {
    acceptRequest++;
    if (responseTime <= 0) {
      responseSpreads[0] = responseSpreads[0] + 1;
    } else if (responseTime > 0 && responseTime <= 1) {
      responseSpreads[1] = responseSpreads[1] + 1;
    } else if (responseTime > 1 && responseTime <= 5) {
      responseSpreads[2] = responseSpreads[2] + 1;
    } else if (responseTime > 5 && responseTime <= 10) {
      responseSpreads[3] = responseSpreads[3] + 1;
    } else if (responseTime > 10 && responseTime <= 50) {
      responseSpreads[4] = responseSpreads[4] + 1;
    } else if (responseTime > 50 && responseTime <= 100) {
      responseSpreads[5] = responseSpreads[5] + 1;
    } else if (responseTime > 100 && responseTime <= 500) {
      responseSpreads[6] = responseSpreads[6] + 1;
    } else if (responseTime > 500 && responseTime <= 1000) {
      responseSpreads[7] = responseSpreads[7] + 1;
    } else if (responseTime > 1000) {
      responseSpreads[8] = responseSpreads[8] + 1;
    }
  }

  @Override
  public List<long[]> getResult() {
    List<long[]> result = new ArrayList<long[]>(6);
    result.add(responseSpreads);
    result.add(new long[] { acceptRequest });
    result.add(new long[] { errorRequest });
    return result;
  }
}