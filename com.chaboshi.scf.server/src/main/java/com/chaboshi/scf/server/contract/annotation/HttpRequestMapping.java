package com.chaboshi.scf.server.contract.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.chaboshi.scf.server.contract.annotation.HttpRequestMethod;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface HttpRequestMapping {
  public String uri();

  public HttpRequestMethod[] method() default { HttpRequestMethod.GET };
}