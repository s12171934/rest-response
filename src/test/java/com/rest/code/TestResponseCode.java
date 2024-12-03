package com.rest.code;

import com.rest.RestResponseCode;
import org.springframework.http.HttpStatus;

public enum TestResponseCode implements RestResponseCode {
  TEST_RESPONSE_CODE(20000,"TEST_RESPONSE_CODE",HttpStatus.OK);

  private final int code;
  private final String message;
  private final HttpStatus httpStatus;

  TestResponseCode(int code, String message, HttpStatus httpStatus) {

    this.code = code;
    this.message = message;
    this.httpStatus = httpStatus;
  }

  @Override
  public int getCode() {
    return this.code;
  }

  @Override
  public String getMessage() {
    return this.message;
  }

  @Override
  public HttpStatus getHttpStatus() {
    return this.httpStatus;
  }
}
