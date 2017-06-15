package com.chaboshi.scf.client.channel;

public class TimeOut {
  private int sessionId;
  private long time;
  private WindowData wd;
  private SCFChannel cSocket;

  public TimeOut(int sessionId, WindowData wd, SCFChannel cSocket) {
    this.sessionId = sessionId;
    this.wd = wd;
    this.cSocket = cSocket;
    this.time = System.currentTimeMillis();
  }

  public int getSessionId() {
    return sessionId;
  }

  public void setSessionId(int sessionId) {
    this.sessionId = sessionId;
  }

  public long getTime() {
    return time;
  }

  public void setTime(long time) {
    this.time = time;
  }

  public WindowData getWd() {
    return wd;
  }

  public void setWd(WindowData wd) {
    this.wd = wd;
  }

  public SCFChannel getcSocket() {
    return cSocket;
  }

  public void setcSocket(SCFChannel cSocket) {
    this.cSocket = cSocket;
  }

}
