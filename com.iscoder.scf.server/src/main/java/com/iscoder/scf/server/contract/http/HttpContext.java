package com.iscoder.scf.server.contract.http;

public class HttpContext {

  private HttpRequest request;

  private HttpResponse response;

  public HttpRequest getRequest() {
    return request;
  }

  public HttpResponse getResponse() {
    return response;
  }

  public void setRequest(HttpRequest request) {
    this.request = request;
  }

  public void setResponse(HttpResponse response) {
    this.response = response;
  }
}
