package com.github.leeyazhou.scf.server.deploy.bytecode;

import com.github.leeyazhou.scf.core.annotation.OperationContract;

public interface ISecond {
  @OperationContract
  public String loadByName(String name) throws Exception;
}
