package com.iscoder.scf.client.entity;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import com.iscoder.scf.common.SCFConst;
import com.iscoder.scf.common.utils.TimeSpanUtil;

/**
 * ServerProfile
 */
public class ServerProfile {

  private String name;
  private String host;
  private int port;
  private int deadTimeout;
  private float weithtRate;

  public ServerProfile(Node node) {
    NamedNodeMap attributes = node.getAttributes();
    this.name = attributes.getNamedItem("name").getNodeValue();
    this.host = attributes.getNamedItem("host").getNodeValue();
    this.port = Integer.parseInt(attributes.getNamedItem("port").getNodeValue());
    Node atribute = attributes.getNamedItem("weithtRate");
    if (atribute != null) {
      this.weithtRate = Float.parseFloat(atribute.getNodeValue().toString());
    } else {
      this.weithtRate = 1;
    }
    atribute = node.getParentNode().getAttributes().getNamedItem("deadTimeout");
    if (atribute != null) {
      // 设置最小值为30s
      int dtime = TimeSpanUtil.getIntFromTimeSpan(atribute.getNodeValue().toString());
      if (dtime < 30000) {
        dtime = 30000;
      }
      this.deadTimeout = dtime;
    } else {
      this.deadTimeout = SCFConst.DEFAULT_DEAD_TIMEOUT;
    }
  }

  /*
   * Unit is ms
   */
  public int getDeadTimeout() {
    return deadTimeout;
  }

  public String getHost() {
    return host;
  }

  public String getName() {
    return name;
  }

  public int getPort() {
    return port;
  }

  public float getWeithtRate() {
    return weithtRate;
  }
}
