/**
 * 
 */

package com.github.leeyazhou.scf.demo;

import com.github.leeyazhou.scf.core.annotation.OperationContract;
import com.github.leeyazhou.scf.core.annotation.ServiceContract;
import com.github.leeyazhou.scf.demo.model.User;

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
