package com.chaboshi.scf.server.contract.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.chaboshi.scf.server.contract.annotation.HttpParameterType;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface HttpPathParameter {
  public String mapping();

  public HttpParameterType paramType() default HttpParameterType.PathParameter;
}