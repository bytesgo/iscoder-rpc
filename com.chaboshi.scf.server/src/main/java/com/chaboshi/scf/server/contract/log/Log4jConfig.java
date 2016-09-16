package com.chaboshi.scf.server.contract.log;

import org.apache.log4j.xml.DOMConfigurator;

/**
 * This class will do the configuration of Log4j
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 * 
 *         <a href="http://blog.58.com/spat/">blog</a>
 *         <a href="http://www.58.com">website</a>
 * 
 */
public class Log4jConfig {

  public static void configure(String configFilePath) {
    DOMConfigurator.configure(configFilePath);
  }

}