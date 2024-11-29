package com.rest;

import org.springframework.http.HttpStatus;

public interface RestResponseCode {

  int getCode();
  String getMessage();
  HttpStatus getHttpStatus();
}
