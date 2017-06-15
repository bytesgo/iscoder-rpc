/**
 * 
 */

package com.chaboshi.scf.demo.contract;

import com.chaboshi.common.annotation.OperationContract;
import com.chaboshi.common.annotation.ServiceContract;

/**
 * @author lee
 */
@ServiceContract
public interface IUserService {

  @OperationContract
  public String sayName(String name);
}
