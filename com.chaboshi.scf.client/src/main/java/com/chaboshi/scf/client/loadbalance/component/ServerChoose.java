package com.chaboshi.scf.client.loadbalance.component;

public class ServerChoose {
  private int serviceCount;
  private String[] serverName;

  public int getServiceCount() {
    return serviceCount;
  }

  public void setServiceCount(int serviceCount) {
    this.serviceCount = serviceCount;
  }

  public String[] getServerName() {
    return serverName;
  }

  public void setServerName(String[] serverName) {
    this.serverName = serverName;
  }

}
