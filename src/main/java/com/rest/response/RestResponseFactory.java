package com.rest.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rest.config.AppProperties;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Objects;

public class RestResponseFactory<T> {

  private final RestResponseCode responseCode;
  private final HttpHeaders headers;
  private final RestResponse<T> restResponse;

  private RestResponseFactory(RestResponseCode responseCode, HttpHeaders headers, T content, Pagination pagination) {

    this.responseCode = responseCode;
    this.headers = setDefaultHeaders(headers);
    this.restResponse = new RestResponse<>(responseCode, content, pagination);
  }

  private static <T> Builder<T> builder() {

    return new Builder<>();
  }

  private static class Builder<T> {

    private RestResponseCode responseCode;
    private HttpHeaders headers;
    private T content;
    private Pagination pagination;

    private Builder() {}

    private Builder<T> responseCode(RestResponseCode responseCode) {

      this.responseCode = responseCode;
      return this;
    }

    private Builder<T> headers(HttpHeaders headers) {

      this.headers = headers;
      return this;
    }

    private Builder<T> content(T content) {

      this.content = content;
      return this;
    }

    private Builder<T> pagination(Pagination pagination) {

      this.pagination = pagination;
      return this;
    }

    public RestResponseFactory<T> build() {

      return new RestResponseFactory<>(responseCode, headers, content, pagination);
    }
  }

  private HttpHeaders setDefaultHeaders(HttpHeaders headers) {

    if (headers == null) headers = new HttpHeaders();
    headers.add(HttpHeaders.CONTENT_TYPE, "application/json; charset=utf-8");
    headers.add(HttpHeaders.LINK,  String.format("<%s>; rel=\"profile\"", AppProperties.restApiReference()));
    return headers;
  }

  private ResponseEntity<RestResponse<T>> createResponseEntity() {

    return new ResponseEntity<>(restResponse, this.headers, this.responseCode.getHttpStatus());
  }

  private void setResponse(HttpServletResponse response) throws IOException {

    response.setStatus(this.responseCode.getHttpStatus().value());
    response.setContentType("application/json;charset=UTF-8");

    for (String key : Objects.requireNonNull(this.headers.keySet())) {
      for (String value : Objects.requireNonNull(this.headers.get(key))) {
        response.setHeader(key, value);
      }
    }

    PrintWriter printWriter = response.getWriter();
    ObjectMapper objectMapper = new ObjectMapper();
    printWriter.write(objectMapper.writeValueAsString(this.restResponse));
    printWriter.flush();
    printWriter.close();
  }

  public static <E> ResponseEntity<RestResponse<E>> createFullResponseEntity(
      RestResponseCode responseCode,
      HttpHeaders responseHeaders,
      E content
  ) {

    return RestResponseFactory.<E>builder()
        .responseCode(responseCode)
        .headers(responseHeaders)
        .content(content)
        .build()
        .createResponseEntity();
  }

  public static <E> ResponseEntity<RestResponse<E>> createFullResponseEntity(
      HttpStatus httpStatus,
      HttpHeaders responseHeaders,
      E content
  ) {

    return createFullResponseEntity(
        DefaultResponseCode.findCode(httpStatus),
        responseHeaders,
        content
    );
  }

  public static <E> ResponseEntity<RestResponse<E[]>> createFullResponseEntity(
      RestResponseCode responseCode,
      HttpHeaders responseHeaders,
      E[] content,
      Pagination pagination
  ) {

    return RestResponseFactory.<E[]>builder()
        .responseCode(responseCode)
        .headers(responseHeaders)
        .content(content)
        .pagination(pagination)
        .build()
        .createResponseEntity();
  }

  public static <E> ResponseEntity<RestResponse<E[]>> createFullResponseEntity(
      HttpStatus httpStatus,
      HttpHeaders responseHeaders,
      E[] content,
      Pagination pagination
  ) {

    return createFullResponseEntity(
        DefaultResponseCode.findCode(httpStatus),
        responseHeaders,
        content,
        pagination
    );
  }

  public static <E extends Collection<?>> ResponseEntity<RestResponse<E>> createFullResponseEntity(
      RestResponseCode responseCode,
      HttpHeaders responseHeaders,
      E content,
      Pagination pagination
  ) {

    return RestResponseFactory.<E>builder()
        .responseCode(responseCode)
        .headers(responseHeaders)
        .content(content)
        .pagination(pagination)
        .build()
        .createResponseEntity();
  }

  public static <E extends Collection<?>> ResponseEntity<RestResponse<E>> createFullResponseEntity(
      HttpStatus httpStatus,
      HttpHeaders responseHeaders,
      E content,
      Pagination pagination
  ) {

    return createFullResponseEntity(
        DefaultResponseCode.findCode(httpStatus),
        responseHeaders,
        content,
        pagination
    );
  }

  public static ResponseEntity<RestResponse<Void>> createBasicResponseEntity(
      RestResponseCode responseCode
  ) {

    return RestResponseFactory.<Void>builder()
        .responseCode(responseCode)
        .build()
        .createResponseEntity();
  }

  public static ResponseEntity<RestResponse<Void>> createBasicResponseEntity(
      HttpStatus httpStatus
  ) {

    return createBasicResponseEntity(
        DefaultResponseCode.findCode(httpStatus)
    );
  }

  public static <E> ResponseEntity<RestResponse<E>> createContentResponseEntity(
      RestResponseCode responseCode,
      E content
  ) {

    return RestResponseFactory.<E>builder()
        .responseCode(responseCode)
        .content(content)
        .build()
        .createResponseEntity();
  }

  public static <E> ResponseEntity<RestResponse<E>> createContentResponseEntity(
      HttpStatus httpStatus,
      E content
  ) {

    return createContentResponseEntity(
        DefaultResponseCode.findCode(httpStatus),
        content
    );
  }

  public static <E> ResponseEntity<RestResponse<E[]>> createContentResponseEntity(
      RestResponseCode responseCode,
      E[] content,
      Pagination pagination
  ) {

    return RestResponseFactory.<E[]>builder()
        .responseCode(responseCode)
        .content(content)
        .pagination(pagination)
        .build()
        .createResponseEntity();
  }

  public static <E> ResponseEntity<RestResponse<E[]>> createContentResponseEntity(
      HttpStatus httpStatus,
      E[] content,
      Pagination pagination
  ) {

    return createContentResponseEntity(
        DefaultResponseCode.findCode(httpStatus),
        content,
        pagination
    );
  }

  public static <E extends Collection<?>> ResponseEntity<RestResponse<E>> createContentResponseEntity(
      RestResponseCode responseCode,
      E content,
      Pagination pagination
  ) {

    return RestResponseFactory.<E>builder()
        .responseCode(responseCode)
        .content(content)
        .pagination(pagination)
        .build()
        .createResponseEntity();
  }

  public static <E extends Collection<?>> ResponseEntity<RestResponse<E>> createContentResponseEntity(
      HttpStatus httpStatus,
      E content,
      Pagination pagination
  ) {

    return createContentResponseEntity(
        DefaultResponseCode.findCode(httpStatus),
        content,
        pagination
    );
  }

  public static ResponseEntity<RestResponse<Void>> createHeaderResponseEntity(
      RestResponseCode responseCode,
      HttpHeaders responseHeaders
  ) {

    return RestResponseFactory.<Void>builder()
        .responseCode(responseCode)
        .headers(responseHeaders)
        .build()
        .createResponseEntity();
  }

  public static ResponseEntity<RestResponse<Void>> createHeaderResponseEntity(
      HttpStatus httpStatus,
      HttpHeaders responseHeaders
  ) {

    return createHeaderResponseEntity(
        DefaultResponseCode.findCode(httpStatus),
        responseHeaders
    );
  }

  public static <E> void setFullResponse(
      HttpServletResponse response,
      RestResponseCode responseCode,
      HttpHeaders responseHeaders,
      E content
  ) throws IOException {

    RestResponseFactory.<E>builder()
        .responseCode(responseCode)
        .headers(responseHeaders)
        .content(content)
        .build()
        .setResponse(response);
  }

  public static <E> void setFullResponse(
      HttpServletResponse response,
      HttpStatus httpStatus,
      HttpHeaders responseHeaders,
      E content
  ) throws IOException {

    setFullResponse(
        response,
        DefaultResponseCode.findCode(httpStatus),
        responseHeaders,
        content
    );
  }

  public static <E> void setFullResponse(
      HttpServletResponse response,
      RestResponseCode responseCode,
      HttpHeaders responseHeaders,
      E[] content,
      Pagination pagination
  ) throws IOException {

    RestResponseFactory.<E[]>builder()
        .responseCode(responseCode)
        .headers(responseHeaders)
        .content(content)
        .pagination(pagination)
        .build()
        .setResponse(response);
  }

  public static <E> void setFullResponse(
      HttpServletResponse response,
      HttpStatus httpStatus,
      HttpHeaders responseHeaders,
      E[] content,
      Pagination pagination
  ) throws IOException {

    setFullResponse(
        response,
        DefaultResponseCode.findCode(httpStatus),
        responseHeaders,
        content,
        pagination
    );
  }

  public static <E extends Collection<?>> void setFullResponse(
      HttpServletResponse response,
      RestResponseCode responseCode,
      HttpHeaders responseHeaders,
      E content,
      Pagination pagination
  ) throws IOException {

    RestResponseFactory.<E>builder()
        .responseCode(responseCode)
        .headers(responseHeaders)
        .content(content)
        .pagination(pagination)
        .build()
        .setResponse(response);
  }

  public static <E extends Collection<?>> void setFullResponse(
      HttpServletResponse response,
      HttpStatus httpStatus,
      HttpHeaders responseHeaders,
      E content,
      Pagination pagination
  ) throws IOException {

    setFullResponse(
        response,
        DefaultResponseCode.findCode(httpStatus),
        responseHeaders,
        content,
        pagination
    );
  }

  public static void setBasicResponse(
      HttpServletResponse response,
      RestResponseCode responseCode
  ) throws IOException {

    RestResponseFactory.<Void>builder()
        .responseCode(responseCode)
        .build()
        .setResponse(response);
  }

  public static void setBasicResponse(
      HttpServletResponse response,
      HttpStatus httpStatus
  ) throws IOException {

    setBasicResponse(
        response,
        DefaultResponseCode.findCode(httpStatus)
    );
  }

  public static <E> void setContentResponse(
      HttpServletResponse response,
      RestResponseCode responseCode,
      E content
  ) throws IOException {

    RestResponseFactory.<E>builder()
        .responseCode(responseCode)
        .content(content)
        .build()
        .setResponse(response);
  }

  public static <E> void setContentResponse(
      HttpServletResponse response,
      HttpStatus httpStatus,
      E content
  ) throws IOException {

    setContentResponse(
        response,
        DefaultResponseCode.findCode(httpStatus),
        content
    );
  }

  public static <E> void setContentResponse(
      HttpServletResponse response,
      RestResponseCode responseCode,
      E[] content,
      Pagination pagination
  ) throws IOException {

    RestResponseFactory.<E[]>builder()
        .responseCode(responseCode)
        .content(content)
        .pagination(pagination)
        .build()
        .setResponse(response);
  }

  public static <E> void setContentResponse(
      HttpServletResponse response,
      HttpStatus httpStatus,
      E[] content,
      Pagination pagination
  ) throws IOException {

    setContentResponse(
        response,
        DefaultResponseCode.findCode(httpStatus),
        content,
        pagination
    );
  }

  public static <E extends Collection<?>> void setContentResponse(
      HttpServletResponse response,
      RestResponseCode responseCode,
      E content,
      Pagination pagination
  ) throws IOException {

    RestResponseFactory.<E>builder()
        .responseCode(responseCode)
        .content(content)
        .pagination(pagination)
        .build()
        .setResponse(response);
  }

  public static <E extends Collection<?>> void setContentResponse(
      HttpServletResponse response,
      HttpStatus httpStatus,
      E content,
      Pagination pagination
  ) throws IOException {

    setContentResponse(
        response,
        DefaultResponseCode.findCode(httpStatus),
        content,
        pagination
    );
  }

  public static void setHeaderResponse(
      HttpServletResponse response,
      RestResponseCode responseCode,
      HttpHeaders responseHeaders
  ) throws IOException {

    RestResponseFactory.<Void>builder()
        .responseCode(responseCode)
        .headers(responseHeaders)
        .build()
        .setResponse(response);
  }

  public static void setHeaderResponse(
      HttpServletResponse response,
      HttpStatus httpStatus,
      HttpHeaders responseHeaders
  ) throws IOException {

    setHeaderResponse(
        response,
        DefaultResponseCode.findCode(httpStatus),
        responseHeaders
    );
  }
}
