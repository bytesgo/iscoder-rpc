/**
 * 
 */

package com.chaboshi.scf.demo.contract;

import com.chaboshi.scf.server.contract.annotation.OperationContract;
import com.chaboshi.scf.server.contract.annotation.ServiceContract;

/**
 * @author lee
 */
@ServiceContract
public interface IUserService {

  @OperationContract
  public String sayName(String name);
}
