/**
 * 
 */
package com.chaboshi.scf.demo.contract;

import com.chaboshi.common.annotation.OperationContract;
import com.chaboshi.common.annotation.ServiceContract;

/**
 * @author lee_y
 *
 */
@ServiceContract
public interface IHelloService {

  @OperationContract
  public String say(String username);

}
