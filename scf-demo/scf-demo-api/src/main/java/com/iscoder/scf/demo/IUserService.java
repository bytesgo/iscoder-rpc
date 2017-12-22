/**
 * 
 */

package com.iscoder.scf.demo;

import com.iscoder.scf.common.annotation.OperationContract;
import com.iscoder.scf.common.annotation.ServiceContract;
import com.iscoder.scf.demo.model.User;

/**
 * @author lee
 */
@ServiceContract
public interface IUserService {

  @OperationContract
  public String sayName(String name);
  
  @OperationContract
  public boolean sayUser(User user
      );
}
