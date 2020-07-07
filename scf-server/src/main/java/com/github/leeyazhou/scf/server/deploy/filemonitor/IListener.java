package com.github.leeyazhou.scf.server.deploy.filemonitor;

/**
 * 
 */
public interface IListener {

  void fileChanged(FileInfo fInfo);

}