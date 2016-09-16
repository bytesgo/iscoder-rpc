package com.chaboshi.scf.server.contract.server;

import com.chaboshi.scf.server.contract.context.SCFContext;

public interface IServerHandler {

  public void writeResponse(SCFContext context);

}