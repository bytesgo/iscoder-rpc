package com.chaboshi.scf.protocol.serializer;

import com.chaboshi.scf.protocol.serializer.util.ProtobufUtils;

/**
 * ByteCodeSerialize
 */
class ProtobufSerializer extends AbstractSerializer {

  @Override
  public byte[] serialize(Object obj) throws Exception {
    return ProtobufUtils.serialize(obj);
  }

  @Override
  public <T> T deserialize(byte[] data, Class<T> cls) throws Exception {
    return ProtobufUtils.deserialize(data, cls);
  }
}