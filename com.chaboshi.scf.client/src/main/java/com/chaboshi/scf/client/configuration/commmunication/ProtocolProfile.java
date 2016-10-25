/*
 * Copyright 2010 www.58.com, Inc.
 * @author Service Platform Architecture Team mail: spat@58.com web: http://www.58.com
 */
package com.chaboshi.scf.client.configuration.commmunication;

import java.nio.charset.Charset;

import javax.naming.directory.NoSuchAttributeException;

import org.w3c.dom.Node;

import com.chaboshi.scf.protocol.serializer.AbstractSerializer;
import com.chaboshi.scf.protocol.sfp.enumeration.CompressType;
import com.chaboshi.scf.protocol.sfp.enumeration.SerializeType;

/**
 * ProtocolProfile
 *
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class ProtocolProfile {

  private SerializeType serializerType;
  private AbstractSerializer serialize;
  public Charset Encoder;
  public byte serviceID;
  public CompressType compress;

  public ProtocolProfile(Node node) throws Exception {
    Node attrSer = node.getAttributes().getNamedItem("serialize");
    if (attrSer == null) {
      throw new ExceptionInInitializerError("Not find attrbuts:" + node.getNodeName() + "[@'serialize']");
    }
    String value = attrSer.getNodeValue().trim().toLowerCase();
    if (value.equalsIgnoreCase("binary")) {
      serializerType = SerializeType.JAVABinary;
    } else if (value.equalsIgnoreCase("json")) {
      serializerType = SerializeType.JSON;
    } else if (value.equalsIgnoreCase("xml")) {
      serializerType = SerializeType.XML;
    } else if (value.equalsIgnoreCase("scf")) {
      serializerType = SerializeType.SCFBinary;
    } else {
      throw new NoSuchAttributeException("Protocol not supported " + value + "!");
    }
    this.serialize = AbstractSerializer.getInstance(serializerType);
    attrSer = node.getAttributes().getNamedItem("encoder");
    if (attrSer == null) {
      this.Encoder = Charset.forName("UTF-8");
    } else {
      this.Encoder = Charset.forName(attrSer.getNodeValue());
    }
    this.serialize.setEncoder(this.Encoder);
    serviceID = Byte.parseByte(node.getParentNode().getParentNode().getAttributes().getNamedItem("id").getNodeValue());// TODO
                                                                                                                       // 待检验
    compress = (CompressType) Enum.valueOf(CompressType.class, node.getAttributes().getNamedItem("compressType").getNodeValue());
  }

  public Charset getEncoder() {
    return Encoder;
  }

  public CompressType getCompress() {
    return compress;
  }

  public AbstractSerializer getSerializer() {
    return serialize;
  }

  public SerializeType getSerializerType() {
    return serializerType;
  }

  public byte getServiceID() {
    return serviceID;
  }
}
