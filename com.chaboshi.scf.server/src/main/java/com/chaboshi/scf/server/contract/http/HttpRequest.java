package com.chaboshi.scf.server.contract.http;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpRequest {

  private String fromIP;

  private String toIP;

  private String uri;

  private byte[] content;

  private Map<String, String> headers = new HashMap<String, String>();

  private Map<String, List<String>> headers_;

  /**
   * 
   * @param key
   * @return
   */
  public String getQueryString(String key) {
    return null;
  }

  public Map<String, List<String>> getHeaders_() {
    return headers_;
  }

  public void setHeaders_(Map<String, List<String>> headers_) {
    this.headers_ = headers_;
  }

  public String getFromIP() {
    return fromIP;
  }

  public Map<String, String> getHeaders() {
    return headers;
  }

  public void setFromIP(String fromIP) {
    this.fromIP = fromIP;
  }

  public void setHeaders(Map<String, String> headers) {
    this.headers = headers;
  }

  public void setUri(String uri) {
    this.uri = uri;
  }

  public String getUri() {
    return uri;
  }

  public void setContent(byte[] content) {
    this.content = content;
  }

  public byte[] getContent() {
    return content;
  }

  public void setToIP(String toIP) {
    this.toIP = toIP;
  }

  public String getToIP() {
    return toIP;
  }
}
