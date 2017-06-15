/**
 * 
 */

package com.iscoder.scf.protocol.serializer.util;

import com.esotericsoftware.kryo.Kryo;

/**
 * @author lee
 *
 */
public class KryoUtils {

  private static final ThreadLocal<Kryo> kryos = new ThreadLocal<Kryo>() {
    protected Kryo initialValue() {
      Kryo kryo = new Kryo();
      kryo.setRegistrationRequired(false);
      kryo.setReferences(true);
      return kryo;
    }
  };

  private KryoUtils() {
  }

  public static Kryo getKryo() {
    return kryos.get();
  }
}
