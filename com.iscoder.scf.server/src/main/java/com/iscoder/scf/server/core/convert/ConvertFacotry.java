package com.iscoder.scf.server.core.convert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iscoder.scf.protocol.entity.SerializeType;
import com.iscoder.scf.protocol.sfp.Protocol;

/**
 * A convert facotry for create converter
 * 
 * 
 */
public class ConvertFacotry {
  private static Logger logger = LoggerFactory.getLogger(ConvertFacotry.class);

  /**
   * SCFBinary
   */
  private static SCFConvert scfBinaryConvert = new SCFConvert();

  public static Convert getConvert(Protocol p) {
    if (p.getSerializeType() == SerializeType.SCFBinary) {
      return scfBinaryConvert;
    }

    logger.error("can't get IConvert not : json ,java, customBinary ");
    return null;
  }
}