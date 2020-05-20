/**
 * 
 */
package com.github.leeyazhou.scf.demo;

import com.github.leeyazhou.scf.core.annotation.OperationContract;
import com.github.leeyazhou.scf.core.annotation.ServiceContract;

/**
 * @author lee_y
 *
 */
@ServiceContract
public interface IHelloService {

  @OperationContract
  public String say(String username);

}
