package com.github.leeyazhou.scf.protocol.serializer;

/**
 * JsonSerialize
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