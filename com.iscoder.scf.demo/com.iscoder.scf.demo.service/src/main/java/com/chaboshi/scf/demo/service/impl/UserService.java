/**
 * 
 */

package com.chaboshi.scf.demo.service.impl;

import com.chaboshi.common.annotation.ServiceBehavior;
import com.chaboshi.scf.demo.contract.IUserService;

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
