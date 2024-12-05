package com.rest.exception;

import com.rest.response.DefaultResponseCode;
import com.rest.response.RestResponseCode;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CustomException {
  Class<? extends RestResponseCode> codeClass() default DefaultResponseCode.class;
  String codeName() default "INTERNAL_SERVER_ERROR";
}
