/**
 * 
 */
package com.chaboshi.scf.demo.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chaboshi.common.annotation.ServiceBehavior;
import com.chaboshi.scf.demo.contract.IHelloService;

/**
 * @author lee_y
 *
 */
@ServiceBehavior
public class HelloService implements IHelloService {

  private static final Logger logger = LoggerFactory.getLogger(HelloService.class);

  @Override
  public String say(String username) {
    String result = "my name is : " + username + " !";
    logger.info(result);
    return result;
  }

}
