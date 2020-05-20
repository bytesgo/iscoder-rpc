package com.github.leeyazhou.scf.server.contract.http;

public abstract class SimpleHttpHandler implements IHttpHandler {

  @Override
  public void get(HttpContext context) {
    context.getResponse().setCode(404);
  }

  @Override
  public void post(HttpContext context) {
    context.getResponse().setCode(404);
  }

  @Override
  public void put(HttpContext context) {
    context.getResponse().setCode(404);
  }

  @Override
  public void delete(HttpContext context) {
    context.getResponse().setCode(404);
  }

  @Override
  public void head(HttpContext context) {
    context.getResponse().setCode(404);
  }
}
