/**
 * 
 */

package com.iscoder.scf.demo.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iscoder.scf.common.annotation.ServiceBehavior;
import com.iscoder.scf.demo.IUserService;
import com.iscoder.scf.demo.model.User;

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
