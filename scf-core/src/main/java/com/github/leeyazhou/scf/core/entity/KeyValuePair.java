package com.github.leeyazhou.scf.core.entity;

import java.io.Serializable;

import com.github.leeyazhou.scf.core.annotation.SCFMember;
import com.github.leeyazhou.scf.core.annotation.SCFSerializable;

/**
 * KeyValuePair
 *
 */
@SuppressWarnings("serial")
@SCFSerializable(name = "RpParameter")
public class KeyValuePair implements Serializable {

  @SCFMember(name = "name")
  private String key;

  @SCFMember
  private Object value;

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public Object getValue() {
    return value;
  }

  public void setValue(Object value) {
    if (value instanceof Out) {
      this.value = ((Out<?>) value).getOutPara();
    } else {
      this.value = value;
    }
  }

  public KeyValuePair() {
  }

  public KeyValuePair(String key, Object value) {
    this.setKey(key);
    this.setValue(value);
  }
}