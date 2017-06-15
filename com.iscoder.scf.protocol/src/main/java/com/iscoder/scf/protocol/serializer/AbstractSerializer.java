package com.iscoder.scf.protocol.serializer;

import java.nio.charset.Charset;

import com.iscoder.scf.protocol.entity.SerializeType;

/**
 * SerializeBase
 *
 */
public abstract class AbstractSerializer {

  private static ProtobufSerializer scfSerialize = new ProtobufSerializer();
  private static JSONSerializer jsonSerialize = new JSONSerializer();
  private static KryoSerializer kryoSerializer = new KryoSerializer();

  private Charset charset;

  public Charset getCharset() {
    return charset;
  }

  public void setCharset(Charset encoder) {
    this.charset = encoder;
  }

  public static AbstractSerializer getInstance(SerializeType serializeType) throws Exception {
    if (serializeType == SerializeType.SCFBinary) {
      return scfSerialize;
    } else if (serializeType == SerializeType.JSON) {
      return jsonSerialize;
    } else if (serializeType == SerializeType.KRYO) {
      return kryoSerializer;
    }
    throw new Exception("末知的序列化算法");
  }

  public abstract byte[] serialize(Object obj) throws Exception;

  public abstract <T> T deserialize(byte[] data, Class<T> cls) throws Exception;
}