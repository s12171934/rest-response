package com.rest;

import org.springframework.http.HttpStatus;

import java.util.Arrays;

enum DefaultResponseCode implements RestResponseCode{
  OK(200, "HTTP_STATUS_OK", HttpStatus.OK),
  CREATED(201, "HTTP_STATUS_CREATED", HttpStatus.CREATED),
  ACCEPTED(202, "HTTP_STATUS_ACCEPTED", HttpStatus.ACCEPTED),
  NO_CONTENT(204, "HTTP_STATUS_NO_CONTENT", HttpStatus.NO_CONTENT),
  BAD_REQUEST(400, "HTTP_STATUS_BAD_REQUEST", HttpStatus.BAD_REQUEST),
  UNAUTHORIZED(401, "HTTP_STATUS_UNAUTHORIZED", HttpStatus.UNAUTHORIZED),
  FORBIDDEN(403, "HTTP_STATUS_FORBIDDEN", HttpStatus.FORBIDDEN),
  NOT_FOUND(404, "HTTP_STATUS_NOT_FOUND", HttpStatus.NOT_FOUND),
  METHOD_NOT_ALLOWED(405, "HTTP_STATUS_METHOD_NOT_ALLOWED", HttpStatus.METHOD_NOT_ALLOWED),
  INTERNAL_SERVER_ERROR(500, "HTTP_STATUS_INTERNAL_SERVER_ERROR", HttpStatus.INTERNAL_SERVER_ERROR),
  ;

  private final int code;
  private final String message;
  private final HttpStatus httpStatus;

  DefaultResponseCode(int code, String message, HttpStatus httpStatus) {

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

  static DefaultResponseCode findCode(HttpStatus httpStatus) {

    return Arrays.stream(DefaultResponseCode.values()).filter(defaultResponseCode -> defaultResponseCode.getHttpStatus().equals(httpStatus)).findFirst().orElse(null);
  }
}
