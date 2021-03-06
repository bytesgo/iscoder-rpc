/**
 * 
 */
package com.github.leeyazhou.scf.demo.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.leeyazhou.scf.core.annotation.ServiceBehavior;
import com.github.leeyazhou.scf.demo.IHelloService;

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
