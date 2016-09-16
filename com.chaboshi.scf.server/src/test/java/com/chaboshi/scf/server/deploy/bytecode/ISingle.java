package com.chaboshi.scf.server.deploy.bytecode;

import com.chaboshi.scf.server.contract.annotation.OperationContract;
import com.chaboshi.scf.server.contract.annotation.ServiceContract;

@ServiceContract
public interface ISingle {
  @OperationContract
  public String loadByID(int id) throws Exception;
}
