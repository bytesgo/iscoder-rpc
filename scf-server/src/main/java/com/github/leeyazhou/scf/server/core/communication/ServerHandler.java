package com.github.leeyazhou.scf.server.core.communication;

import com.github.leeyazhou.scf.server.contract.context.SCFContext;

public interface ServerHandler {

  public void writeResponse(SCFContext context);

}