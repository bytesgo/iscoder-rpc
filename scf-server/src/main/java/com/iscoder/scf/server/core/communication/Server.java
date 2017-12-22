package com.iscoder.scf.server.core.communication;

/**
 * a interface for description start/stop socket server
 * 
 */
public interface Server {

  public void start() throws Exception;

  public void stop() throws Exception;
}