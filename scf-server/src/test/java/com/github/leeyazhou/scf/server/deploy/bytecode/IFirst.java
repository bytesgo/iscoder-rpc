package com.github.leeyazhou.scf.server.deploy.bytecode;

import com.github.leeyazhou.scf.core.annotation.OperationContract;

public interface IFirst {
  @OperationContract
  public String loadByID(int id) throws Exception;
}
