package com.iscoder.scf.server.deploy.bytecode;

import com.iscoder.scf.common.annotation.OperationContract;

public interface ISecond {
  @OperationContract
  public String loadByName(String name) throws Exception;
}
