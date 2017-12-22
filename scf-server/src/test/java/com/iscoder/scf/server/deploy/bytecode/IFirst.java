package com.iscoder.scf.server.deploy.bytecode;

import com.iscoder.scf.common.annotation.OperationContract;

public interface IFirst {
  @OperationContract
  public String loadByID(int id) throws Exception;
}
