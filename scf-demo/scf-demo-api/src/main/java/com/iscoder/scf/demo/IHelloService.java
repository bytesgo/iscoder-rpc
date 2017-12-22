/**
 * 
 */
package com.iscoder.scf.demo;

import com.iscoder.scf.common.annotation.OperationContract;
import com.iscoder.scf.common.annotation.ServiceContract;

/**
 * @author lee_y
 *
 */
@ServiceContract
public interface IHelloService {

  @OperationContract
  public String say(String username);

}
