/**
 * 
 */
package com.chaboshi.scf.demo.contract;

import com.chaboshi.scf.server.contract.annotation.OperationContract;
import com.chaboshi.scf.server.contract.annotation.ServiceContract;

/**
 * @author lee_y
 *
 */
@ServiceContract
public interface IHelloService {

  @OperationContract
  public String say(String username);

}
