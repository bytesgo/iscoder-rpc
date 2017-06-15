/**
 * 
 */

package com.iscoder.scf.demo;

import com.iscoder.scf.common.annotation.OperationContract;
import com.iscoder.scf.common.annotation.ServiceContract;

/**
 * @author lee
 */
@ServiceContract
public interface IUserService {

  @OperationContract
  public String sayName(String name);
}
