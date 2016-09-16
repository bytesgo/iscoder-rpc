package com.chaboshi.scf.server.deploy.filemonitor;

/**
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 * 
 *         <a href="http://blog.58.com/spat/">blog</a>
 *         <a href="http://www.58.com">website</a>
 * 
 */
public interface IListener {

  void fileChanged(FileInfo fInfo);

}