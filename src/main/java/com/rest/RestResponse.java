package com.rest;

import java.time.LocalDateTime;

public record RestResponse<T>(
    Boolean success,
    Status status,
    T result,
    MetaData metaData
) {

  public RestResponse(Boolean success, Status status, T result, MetaData metaData) {
    this.success = success;
    this.status = status;
    this.result = result;
    this.metaData = metaData;
  }

  record Status(
      int code,
      String message
  ) {}

  record MetaData(
      LocalDateTime timeStamp,
      String path,
      String version
  ) {}
}