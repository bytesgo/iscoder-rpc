/**
 * 
 */

package com.iscoder.scf.protocol.serializer;

/**
 * @author lee
 *
 */
public class User {

  private int id;
  private String username;
  private long age;

  /**
   * @return the id
   */
  public int getId() {
    return id;
  }

  /**
   * @param id the id to set
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * @return the username
   */
  public String getUsername() {
    return username;
  }

  /**
   * @param username the username to set
   */
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * @return the age
   */
  public long getAge() {
    return age;
  }

  /**
   * @param age the age to set
   */
  public void setAge(long age) {
    this.age = age;
  }

  /*
   * (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "User [id=" + id + ", username=" + username + ", age=" + age + "]";
  }

}
