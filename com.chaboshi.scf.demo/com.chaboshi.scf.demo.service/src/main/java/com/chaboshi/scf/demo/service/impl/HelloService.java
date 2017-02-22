/**
 * 
 */
package com.chaboshi.scf.demo.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chaboshi.scf.demo.contract.IHelloService;
import com.chaboshi.scf.server.annotation.ServiceBehavior;

/**
 * @author lee_y
 *
 */
@ServiceBehavior(lookUP = "HelloService")
public class HelloService implements IHelloService {

  private static final Logger logger = LoggerFactory.getLogger(HelloService.class);

  @Override
  public String say(String username) {
    String result = "my name is : " + username + " !";
    logger.info(result);
    return result;
  }

}
