/*
 * Copyright 2010 58.com, Inc. SPAT team blog: http://blog.58.com/spat/ website: http://www.58.com
 */
package com.chaboshi.scf.protocol.serializer;

import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

/**
 * ByteCodeSerialize
 *
 * @author Service Platform Architecture Team (spat@58.com)
 */
class KryoSerialize extends SerializeBase {

  @Override
  public byte[] serialize(Object obj) throws Exception {
    Output output = new Output(256, -1);
    try {
      KryoUtils.getKryo().writeClassAndObject(output, obj);
      return output.toBytes();
    } finally {
      output.close();
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> T deserialize(byte[] data, Class<T> cls) throws Exception {
    Input input = new Input(data);
    try {
      return (T) KryoUtils.getKryo().readClassAndObject(input);
    } finally {
      input.close();
    }
  }
}