package com.chaboshi.scf.server.core.convert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chaboshi.scf.protocol.sfp.enumeration.SerializeType;
import com.chaboshi.scf.protocol.sfp.v1.Protocol;

/**
 * A convert facotry for create converter
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 * 
 *         <a href="http://blog.58.com/spat/">blog</a> <a href="http://www.58.com">website</a>
 * 
 */
public class ConvertFacotry {
  private static Logger logger = LoggerFactory.getLogger(ConvertFacotry.class);
  /**
   * java
   */
  private static JavaConvert javaConvert = new JavaConvert();

  /**
   * SCFBinary
   */
  private static SCFBinaryConvert scfBinaryConvert = new SCFBinaryConvert();

  public static IConvert getConvert(Protocol p) {
    if (p.getSerializeType() == SerializeType.SCFBinary) {
      return scfBinaryConvert;
    } else if (p.getSerializeType() == SerializeType.JAVABinary) {
      return javaConvert;
    }

    logger.error("can't get IConvert not : json ,java, customBinary ");
    return null;
  }
}