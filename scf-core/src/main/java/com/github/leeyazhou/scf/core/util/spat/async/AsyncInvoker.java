package com.github.leeyazhou.scf.core.util.spat.async;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import com.github.leeyazhou.scf.core.util.ThreadNamedFactory;

public class AsyncInvoker {

  /**
   * Round Robin
   */
  private int rr = 0;

  /**
   * 工作线程
   */
  private AsyncWorker[] workers = null;

  /**
   * 获取AsyncInvoker实例
   * 
   * @param workerCount 工作线程数
   * @return
   */
  public static AsyncInvoker getInstance(int workerCount) {
    return new AsyncInvoker(workerCount, false, null);
  }

  /**
   * 获取AsyncInvoker实例(默认工作线程数为CPU的个数)
   * 
   * @return
   */
  public static AsyncInvoker getInstance() {
    // 获取CPU个数
    int cpuCount = Runtime.getRuntime().availableProcessors();
    return new AsyncInvoker(cpuCount, false, null);
  }

  /**
   * 获取AsyncInvoker实例
   * 
   * @param workerCount 工作线程数
   * @param timeoutEffect 是否启用调用超时
   * @return
   */
  public static AsyncInvoker getInstance(int workerCount, boolean timeoutEffect) {
    return new AsyncInvoker(workerCount, timeoutEffect, null);
  }

  public static AsyncInvoker getInstance(int workerCount, boolean timeoutEffect, String threadFactoryName) {
    return new AsyncInvoker(workerCount, timeoutEffect, threadFactoryName);
  }

  /**
   * 获取AsyncInvoker实例
   * 
   * @param workerCount 工作线程个数
   */
  private AsyncInvoker(int workerCount, boolean timeoutEffect, String threadFactoryName) {
    if (null == threadFactoryName) {
      threadFactoryName = "worker";
    }
    this.workers = new AsyncWorker[workerCount];
    final int availableProcessors = Runtime.getRuntime().availableProcessors();
    final ExecutorService executor = new ThreadPoolExecutor(availableProcessors, availableProcessors * 2, 60L,
        TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), new ThreadNamedFactory(threadFactoryName));

    for (int i = 0; i < workerCount; i++) {
      workers[i] = new AsyncWorker(executor, timeoutEffect);
      workers[i].setDaemon(true);
      workers[i].setName("async task worker[" + i + "]");
      workers[i].start();
    }
  }

  /**
   * 执行异步任务(无阻塞立即返回,当前版本只实现轮询分配,下个版本增加工作线程间的任务窃取)
   * 
   * @param task
   */
  @Deprecated
  public void run(AsyncTask task) {
    if (rr > 10000) {
      rr = 0;
    }
    int idx = rr % workers.length;
    workers[idx].addTask(task);
    ++rr;
  }

  /**
   * 执行异步任务(无阻塞立即返回,当前版本只实现轮询分配,下个版本增加工作线程间的任务窃取)
   * 
   * @param timeOut 超时时间
   * @param handler 任务handler
   */
  public void run(int timeOut, IAsyncHandler handler) {
    AsyncTask task = new AsyncTask(timeOut, handler);
    if (rr > 10000) {
      rr = 0;
    }
    int idx = rr % workers.length;
    workers[idx].addTask(task);
    ++rr;
  }

  /**
   * 停止所有工作线程
   */
  public void stop() {
    for (AsyncWorker worker : workers) {
      worker.end();
    }
  }
}
