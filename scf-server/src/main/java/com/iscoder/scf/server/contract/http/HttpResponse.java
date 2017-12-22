package com.iscoder.scf.server.contract.http;

import java.util.HashMap;
import java.util.Map;

public class HttpResponse {

  private Map<String, String> headers = new HashMap<String, String>();

  private String contentType;

  private int code;

  /**
   * 
   * @param content
   */
  public void write(String content) {
    write(content, "utf-8");
  }

  /**
   * 
   * @param content
   * @param encoding
   */
  public void write(String content, String encoding) {

  }

  /**
   * 
   * @param buffer
   */
  public void write(byte[] buffer) {

  }

  /**
   * 
   * @param key
   * @param value
   */
  public void addHeader(String key, String value) {
    headers.put(key, value);
  }

  public Map<String, String> getHeaders() {
    return headers;
  }

  public void setHeaders(Map<String, String> headers) {
    this.headers = headers;
  }

  public String getContentType() {
    return contentType;
  }

  public void setContentType(String contentType) {
    this.contentType = contentType;
  }

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }
}
