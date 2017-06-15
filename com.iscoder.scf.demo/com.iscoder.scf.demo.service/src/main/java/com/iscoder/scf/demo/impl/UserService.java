/**
 * 
 */

package com.iscoder.scf.demo.impl;

import com.iscoder.scf.common.annotation.ServiceBehavior;
import com.iscoder.scf.demo.IUserService;

/**
 * @author lee
 */
@ServiceBehavior
public class UserService implements IUserService {

  @Override
  public String sayName(String name) {
    System.out.println("say name : " + name);
    return name;
  }

}
