/*
 * Copyright 2010 58.com, Inc. SPAT team blog: http://blog.58.com/spat/ website: http://www.58.com
 */
package com.chaboshi.scf.protocol.serializer;

/**
 * JsonSerialize
 *
 * @author Service Platform Architecture Team (spat@58.com)
 */
class JSONSerializer extends AbstractSerializer {

  @Override
  public byte[] serialize(Object obj) throws Exception {
    throw new UnsupportedOperationException("Not supported json serialize!");
  }

  @Override
  public <T> T deserialize(byte[] data, Class<T> cls) throws Exception {
    throw new UnsupportedOperationException("Not supported json serialize!");
  }
}