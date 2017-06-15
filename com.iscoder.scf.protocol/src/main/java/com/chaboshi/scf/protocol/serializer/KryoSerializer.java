package com.chaboshi.scf.protocol.serializer;

import com.chaboshi.scf.protocol.serializer.util.KryoUtils;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

/**
 * ByteCodeSerialize
 */
class KryoSerializer extends AbstractSerializer {

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