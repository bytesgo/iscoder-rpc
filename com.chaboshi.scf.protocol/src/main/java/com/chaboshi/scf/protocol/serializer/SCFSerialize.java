/*
 * Copyright 2010 58.com, Inc. SPAT team blog: http://blog.58.com/spat/ website: http://www.58.com
 */
package com.chaboshi.scf.protocol.serializer;

/**
 * ByteCodeSerialize
 *
 * @author Service Platform Architecture Team (spat@58.com)
 */
class SCFSerialize extends SerializeBase {

  @Override
  public byte[] serialize(Object obj) throws Exception {
    return PBUtils.serialize(obj);
  }

  @Override
  public <T> T deserialize(byte[] data, Class<T> cls) throws Exception {
    return PBUtils.deserialize(data, cls);
  }
}