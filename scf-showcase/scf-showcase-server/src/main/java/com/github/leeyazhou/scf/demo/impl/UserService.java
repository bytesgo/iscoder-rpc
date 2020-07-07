/**
 * 
 */

package com.github.leeyazhou.scf.demo.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.leeyazhou.scf.core.annotation.ServiceBehavior;
import com.github.leeyazhou.scf.demo.IUserService;
import com.github.leeyazhou.scf.demo.model.User;

/**
 * @author lee
 */
@ServiceBehavior
public class UserService implements IUserService {
  private static final Logger logger = LoggerFactory.getLogger(UserService.class);
  @Override
  public String sayName(String name) {
    if(logger.isInfoEnabled()) {
      logger.info("say name:{}", name);
    }
    return name;
  }

  @Override
  public boolean sayUser(User user) {
    if(logger.isInfoEnabled()) {
      logger.info("say user:{}",user);
    }
    return false;
  }

}
