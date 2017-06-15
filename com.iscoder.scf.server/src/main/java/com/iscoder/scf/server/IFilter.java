package com.iscoder.scf.server;

import com.iscoder.scf.server.contract.context.SCFContext;

public interface IFilter {

  /**
   * 获得优先级
   * 
   * @return
   */
  public int getPriority();

  /**
   * 过虑
   * 
   * @param context
   * @return
   * @throws Exception
   */
  public void filter(SCFContext context) throws Exception;

}
