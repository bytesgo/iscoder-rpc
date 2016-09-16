/**
 * 
 */
package com.chaboshi.scf.demo.service.impl;

import com.chaboshi.scf.demo.contract.IHelloService;
import com.chaboshi.scf.server.contract.annotation.ServiceBehavior;

/**
 * @author lee_y
 *
 */
@ServiceBehavior(lookUP = "HelloService")
public class HelloService implements IHelloService {

  @Override
  public String say(String username) {
    return "my name is : " + username + " !";
  }

}
