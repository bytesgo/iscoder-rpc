/**
 * 
 */

package com.chaboshi.scf.protocol.serializer;

import org.junit.Test;

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
    SerializeBase serializeBase = new KryoSerialize();
    byte[] userByte = serializeBase.serialize(user);
    System.out.println(userByte.length);
    User user2 = serializeBase.deserialize(userByte, User.class);
    System.out.println(user2);

  }
}
