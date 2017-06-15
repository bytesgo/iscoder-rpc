package com.chaboshi.scf.server.deploy.bytecode;

import com.chaboshi.scf.server.contract.annotation.OperationContract;

public interface IFirst {
  @OperationContract
  public String loadByID(int id) throws Exception;
}
