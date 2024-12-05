package com.rest.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.rest.config.AppProperties;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record RestResponse<T>(
    Boolean success,
    Status status,
    T content,
    MetaData metaData
) {

  public RestResponse(RestResponseCode responseCode, T content, Pagination pagination) {
    this(
        responseCode.getHttpStatus().is2xxSuccessful(),
        new Status(
            responseCode.getCode(),
            responseCode.getMessage()
        ),
        content,
        new MetaData(
            LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),
            ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString(),
            AppProperties.restApiVersion(),
            pagination
        )
    );
  }

  record Status(
      int code,
      String message
  ) {}

  @JsonInclude(JsonInclude.Include.NON_NULL)
  record MetaData(
      String timeStamp,
      String path,
      String version,
      Pagination pagination
  ) {

    public MetaData(String timeStamp, String path, String version) {
      this(timeStamp, path, version, null);
    }
  }
}