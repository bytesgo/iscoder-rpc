package com.iscoder.scf.server.deploy.bytecode;

import com.iscoder.scf.common.annotation.OperationContract;
import com.iscoder.scf.common.annotation.ServiceContract;

@ServiceContract
public interface ISingle {
  @OperationContract
  public String loadByID(int id) throws Exception;
}
