/**
 * 
 */

package com.github.leeyazhou.scf.protocol.serializer;

import org.junit.Test;

import com.github.leeyazhou.scf.protocol.serializer.AbstractSerializer;
import com.github.leeyazhou.scf.protocol.serializer.KryoSerializer;

/**
 * @author lee
 *
 */
public class KryoSerializationUtilsTest {

  @Test
  public void test2() throws Exception {
    User user = new User();
    user.setId(201);
    user.setAge(23);
    user.setUsername("李亚州");
    AbstractSerializer abstractSerializer = new KryoSerializer();
    byte[] userByte = abstractSerializer.serialize(user);
    System.out.println(userByte.length);
    User user2 = abstractSerializer.deserialize(userByte, User.class);
    System.out.println(user2);

  }
}
