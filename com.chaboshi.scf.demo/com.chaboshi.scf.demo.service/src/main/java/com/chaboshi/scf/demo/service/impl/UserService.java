/**
 * 
 */

package com.chaboshi.scf.demo.service.impl;

import com.chaboshi.scf.demo.contract.IUserService;
import com.chaboshi.scf.server.annotation.ServiceBehavior;

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
