/*
 * Copyright 2010 www.58.com, Inc.
 * @author Service Platform Architecture Team mail: spat@58.com web: http://www.58.com
 */
package com.chaboshi.scf.client.entity;

import org.w3c.dom.Node;

/**
 * KeyProfile 授权文件
 * 
 */
public class KeyProfile {

  /**
   * 授权文件
   */
  private String info;

  /**
   * 是否启用权限认证 true为启动否则为否
   */
  // private String rights;

  public KeyProfile(Node node) {
    if (node != null) {
      Node infoNode = node.getAttributes().getNamedItem("info");
      if (infoNode != null) {
        this.info = infoNode.getNodeValue();
      }
    }
  }

  public String getInfo() {
    return info;
  }

  public void setInfo(String info) {
    this.info = info;
  }
}
