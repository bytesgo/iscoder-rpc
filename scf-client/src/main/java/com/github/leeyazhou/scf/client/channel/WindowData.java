package com.github.leeyazhou.scf.client.channel;

import com.github.leeyazhou.scf.client.entity.AutoResetEvent;
import com.github.leeyazhou.scf.client.proxy.builder.ReceiveHandler;

/**
 * WindowData
 */
public class WindowData {

  AutoResetEvent _event;
  byte[] _data;
  private byte flag = 0;
  /** 0:同步(默认值)1:异步 */
  private Exception exception;
  private long timestamp;
  private ReceiveHandler receiveHandler;
  private SCFChannel csocket;
  private byte[] sendData;

  public WindowData(AutoResetEvent event) {
    _event = event;
  }

  public WindowData(ReceiveHandler receiveHandler, SCFChannel csocket) {
    this.flag = 1;
    this.receiveHandler = receiveHandler;
    this.csocket = csocket;
  }

  public WindowData(ReceiveHandler receiveHandler, SCFChannel csocket, byte[] sendData) {
    this.flag = 1;
    this.receiveHandler = receiveHandler;
    this.csocket = csocket;
    this.sendData = sendData;
    this.timestamp = System.currentTimeMillis();
  }

  public AutoResetEvent getEvent() {
    return _event;
  }

  public byte[] getData() {
    return _data;
  }

  public void setData(byte[] data) {
    _data = data;
  }

  /**
   * @return the flag
   */
  public byte getFlag() {
    return flag;
  }

  /**
   * @param flag the flag to set
   */
  public void setFlag(byte flag) {
    this.flag = flag;
  }

  /**
   * @return the exception
   */
  public Exception getException() {
    return exception;
  }

  /**
   * @param exception the exception to set
   */
  public void setException(Exception exception) {
    this.exception = exception;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(long timestamp) {
    this.timestamp = timestamp;
  }

  public ReceiveHandler getReceiveHandler() {
    return receiveHandler;
  }

  public void setReceiveHandler(ReceiveHandler receiveHandler) {
    this.receiveHandler = receiveHandler;
  }

  public SCFChannel getCsocket() {
    return csocket;
  }

  public void setCsocket(SCFChannel csocket) {
    this.csocket = csocket;
  }

  public byte[] getSendData() {
    return sendData;
  }

  public void setSendData(byte[] sendData) {
    this.sendData = sendData;
  }

}
