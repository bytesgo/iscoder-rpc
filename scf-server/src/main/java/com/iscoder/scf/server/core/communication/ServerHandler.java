package com.iscoder.scf.server.core.communication;

import com.iscoder.scf.server.contract.context.SCFContext;

public interface ServerHandler {

  public void writeResponse(SCFContext context);

}