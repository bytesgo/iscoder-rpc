package com.iscoder.scf.server.core.handler;

import com.iscoder.scf.server.contract.context.SCFContext;

/**
 * a interface for description InvokerHandle such as: AsyncInvokerHandle, AsyncInvokerHandle
 * 
 */
public interface Handler {

  public void invoke(SCFContext context) throws Exception;

}