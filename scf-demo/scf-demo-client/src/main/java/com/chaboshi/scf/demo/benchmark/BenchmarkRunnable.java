/**
 * 
 */

package com.chaboshi.scf.demo.benchmark;

import java.util.List;

/**
 * @author lee
 */
public interface BenchmarkRunnable extends Runnable {

  public List<long[]> getResult();

}
