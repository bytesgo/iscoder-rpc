package com.chaboshi.scf.server.deploy.bytecode;

import com.chaboshi.scf.server.contract.annotation.OperationContract;

public interface ISecond {
  @OperationContract
  public String loadByName(String name) throws Exception;
}
