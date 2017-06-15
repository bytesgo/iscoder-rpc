package com.chaboshi.scf.server.deploy.filemonitor;

/**
 * 
 */
public interface IListener {

  void fileChanged(FileInfo fInfo);

}