/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chaboshi.scf.serializer.component.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author Administrator
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface SCFMember {
  String name() default "";

  int sortId() default -1;
}
