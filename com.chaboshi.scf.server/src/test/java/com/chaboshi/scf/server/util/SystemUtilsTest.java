package com.chaboshi.scf.server.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.chaboshi.scf.server.util.SystemUtils;

public class SystemUtilsTest {

  private static final Map map = new ConcurrentHashMap();

  public static void main(String[] args) {
    Thread t[] = new Thread[10];
    for (int i = 0; i < 10; i++) {
      t[i] = new Thread(new ReadThread());
      t[i].start();
    }
  }

  static class ReadThread implements Runnable {
    @Override
    public void run() {
      for (;;) {
        int id = SystemUtils.createSessionId();
        if (map.get(id) != null) {
          System.err.println("冲突了");
        } else {
          map.put(id, id);
        }
      }
    }
  }
}
