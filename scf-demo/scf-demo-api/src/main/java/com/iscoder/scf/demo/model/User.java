/**
 * 
 */
package com.iscoder.scf.demo.model;

import java.io.Serializable;

import com.iscoder.scf.common.annotation.SCFMember;
import com.iscoder.scf.common.annotation.SCFSerializable;

/**
 * @author lee
 *
 */
@SCFSerializable
public class User implements Serializable {
  private static final long serialVersionUID = -1166354333706277498L;
  @SCFMember
  private Integer id;
  @SCFMember
  private String username;
  @SCFMember
  private Integer sex;
  @SCFMember
  private Boolean deleted;
  @SCFMember
  private byte data[];

  /**
   * 
   */
  public User() {
  }

  public User(Integer id, Integer sex, String name) {
    this.id = id;
    this.sex = sex;
    this.username = name;
  }

  /**
   * @return the id
   */
  public Integer getId() {
    return id;
  }

  /**
   * @param id the id to set
   */
  public void setId(Integer id) {
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
   * @return the sex
   */
  public Integer getSex() {
    return sex;
  }

  /**
   * @param sex the sex to set
   */
  public void setSex(Integer sex) {
    this.sex = sex;
  }

  /**
   * @return the deleted
   */
  public Boolean getDeleted() {
    return deleted;
  }

  /**
   * @param deleted the deleted to set
   */
  public void setDeleted(Boolean deleted) {
    this.deleted = deleted;
  }

  /**
   * @return the data
   */
  public byte[] getData() {
    return data;
  }

  /**
   * @param data the data to set
   */
  public void setData(byte[] data) {
    this.data = data;
  }

  @Override
  public String toString() {
    return "User [id=" + id + ", username=" + username + ", sex=" + sex + ", deleted=" + deleted + "]";
  }


}
