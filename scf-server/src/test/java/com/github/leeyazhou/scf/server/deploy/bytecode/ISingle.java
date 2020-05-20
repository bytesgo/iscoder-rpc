package com.github.leeyazhou.scf.server.deploy.bytecode;

import com.github.leeyazhou.scf.core.annotation.OperationContract;
import com.github.leeyazhou.scf.core.annotation.ServiceContract;

@ServiceContract
public interface ISingle {
  @OperationContract
  public String loadByID(int id) throws Exception;
}
