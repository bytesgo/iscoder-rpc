package com.chaboshi.scf.server.core;

import com.chaboshi.scf.server.contract.context.SCFContext;

public interface ServerHandler {

  public void writeResponse(SCFContext context);

}