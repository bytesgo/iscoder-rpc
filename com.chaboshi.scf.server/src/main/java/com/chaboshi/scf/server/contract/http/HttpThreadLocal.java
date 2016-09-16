package com.chaboshi.scf.server.contract.http;

public class HttpThreadLocal {
  private static ThreadLocal<HttpContext> httplocal = new ThreadLocal<HttpContext>();

  private static HttpThreadLocal httpthreadlocal;

  public synchronized static HttpThreadLocal getInstance() {
    if (httpthreadlocal == null) {
      httpthreadlocal = new HttpThreadLocal();
    }
    return httpthreadlocal;
  }

  public HttpContext get() {
    return (HttpContext) httplocal.get();
  }

  public void set(HttpContext context) {
    httplocal.set(context);
  }

  public void remove() {
    httplocal.remove();
  }
}
