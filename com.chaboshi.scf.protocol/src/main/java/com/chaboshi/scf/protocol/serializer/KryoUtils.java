/**
 * 
 */

package com.chaboshi.scf.protocol.serializer;

import java.util.ArrayList;
import java.util.List;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;

/**
 * @author lee
 *
 */
public class KryoUtils {

  private static final List<ClassItem> CLASSLIST = new ArrayList<ClassItem>();

  private static final ThreadLocal<Kryo> kryos = new ThreadLocal<Kryo>() {
    protected Kryo initialValue() {
      Kryo kryo = new Kryo();
      for (ClassItem item : CLASSLIST) {
        if (item.getSerializer() == null) {
          kryo.register(item.getType(), item.getId());
        } else {
          kryo.register(item.getType(), item.getSerializer(), item.getId());
        }
      }
      kryo.setRegistrationRequired(false);
      kryo.setReferences(false);
      return kryo;
    }
  };

  private KryoUtils() {
  }

  /**
   * @param className
   * @param serializer
   * @param id
   */
  public static synchronized void registerClass(Class<?> className, Serializer<?> serializer, final int id) {
    ClassItem ci = new ClassItem(className, serializer, id);
    CLASSLIST.add(ci);
  }

  public static Kryo getKryo() {
    return kryos.get();
  }

  static class ClassItem {
    private Class<?> type;
    private Serializer<?> serializer;
    private Integer id;

    ClassItem(final Class<?> type, final Serializer<?> serializer, final int id) {
      this.type = type;
      this.serializer = serializer;
      this.id = id;
    }

    public Class<?> getType() {
      return type;
    }

    public void setType(Class<?> type) {
      this.type = type;
    }

    public Serializer<?> getSerializer() {
      return serializer;
    }

    public void setSerializer(Serializer<?> serializer) {
      this.serializer = serializer;
    }

    public Integer getId() {
      return id;
    }

    public void setId(Integer id) {
      this.id = id;
    }

    @Override
    public String toString() {
      return "ClassItem [type=" + type + ", serializer=" + serializer + ", id=" + id + "]";
    }
  }
}
