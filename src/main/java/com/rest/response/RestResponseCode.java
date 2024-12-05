package com.rest.response;

import org.springframework.http.HttpStatus;

public interface RestResponseCode {

  int getCode();
  String getMessage();
  HttpStatus getHttpStatus();
}
