package com.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
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
  private HttpHeaders headers;
  private RestResponse<T> restResponse;

  private RestResponseFactory(RestResponseCode responseCode, HttpHeaders headers, T result, Pagination pagination) {

    this.responseCode = responseCode;
    this.headers = headers;
    this.restResponse = new RestResponse<>(responseCode, result, pagination);
  }

  private RestResponseFactory(RestResponseCode responseCode, HttpHeaders headers, T result) {

    this.responseCode = responseCode;
    this.headers = headers;
    this.restResponse = new RestResponse<>(responseCode, result);
  }

  private RestResponseFactory(RestResponseCode responseCode, HttpHeaders headers) {

    this.responseCode = responseCode;
    this.headers = headers;
    this.restResponse = new RestResponse<>(responseCode, null);
  }

  private RestResponseFactory(RestResponseCode responseCode) {

    this.responseCode = responseCode;
    this.restResponse = new RestResponse<>(responseCode, null);
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

  public static <E> ResponseEntity<RestResponse<E>> createFullResponseEntity(RestResponseCode restResponse, HttpHeaders responseHeaders, E result) {
    return new RestResponseFactory<>(restResponse, responseHeaders, result).createResponseEntity();
  }

  public static <E> ResponseEntity<RestResponse<E>> createFullResponseEntity(HttpStatus httpStatus, HttpHeaders responseHeaders, E result) {
    return createFullResponseEntity(DefaultResponseCode.findCode(httpStatus), responseHeaders, result);
  }

  public static <E> ResponseEntity<RestResponse<E[]>> createFullResponseEntity(RestResponseCode responseCode, HttpHeaders responseHeaders, E[] result, Pagination pagination) {
    return new RestResponseFactory<>(responseCode, responseHeaders, result, pagination).createResponseEntity();
  }

  public static <E> ResponseEntity<RestResponse<E[]>> createFullResponseEntity(HttpStatus httpStatus, HttpHeaders responseHeaders, E[] result, Pagination pagination) {
    return createFullResponseEntity(DefaultResponseCode.findCode(httpStatus), responseHeaders, result, pagination);
  }

  public static <E extends Collection<?>> ResponseEntity<RestResponse<E>> createFullResponseEntity(RestResponseCode responseCode, HttpHeaders responseHeaders, E result, Pagination pagination) {
    return new RestResponseFactory<>(responseCode, responseHeaders, result, pagination).createResponseEntity();
  }

  public static <E extends Collection<?>> ResponseEntity<RestResponse<E>> createFullResponseEntity(HttpStatus httpStatus, HttpHeaders responseHeaders, E result, Pagination pagination) {
    return createFullResponseEntity(DefaultResponseCode.findCode(httpStatus), responseHeaders, result, pagination);
  }

  public static ResponseEntity<RestResponse<Void>> createBasicResponseEntity(RestResponseCode restResponse) {
    return new RestResponseFactory<Void>(restResponse).createResponseEntity();
  }

  public static ResponseEntity<RestResponse<Void>> createBasicResponseEntity(HttpStatus httpStatus) {
    return createBasicResponseEntity(DefaultResponseCode.findCode(httpStatus));
  }

  public static <E> ResponseEntity<RestResponse<E>> createResultResponseEntity(RestResponseCode restResponse, E result) {
    return new RestResponseFactory<>(restResponse, null, result).createResponseEntity();
  }

  public static <E> ResponseEntity<RestResponse<E>> createResultResponseEntity(HttpStatus httpStatus, E result) {
    return createResultResponseEntity(DefaultResponseCode.findCode(httpStatus), result);
  }

  public static <E> ResponseEntity<RestResponse<E[]>> createResultResponseEntity(RestResponseCode responseCode, E[] result, Pagination pagination) {
    return new RestResponseFactory<>(responseCode, null, result, pagination).createResponseEntity();
  }

  public static <E> ResponseEntity<RestResponse<E[]>> createResultResponseEntity(HttpStatus httpStatus, E[] result, Pagination pagination) {
    return createResultResponseEntity(DefaultResponseCode.findCode(httpStatus), result, pagination);
  }

  public static <E extends Collection<?>> ResponseEntity<RestResponse<E>> createResultResponseEntity(RestResponseCode responseCode, E result, Pagination pagination) {
    return new RestResponseFactory<>(responseCode, null, result, pagination).createResponseEntity();
  }

  public static <E extends Collection<?>> ResponseEntity<RestResponse<E>> createResultResponseEntity(HttpStatus httpStatus, E result, Pagination pagination) {
    return createResultResponseEntity(DefaultResponseCode.findCode(httpStatus), result, pagination);
  }

  public static ResponseEntity<RestResponse<Void>> createHeaderResponseEntity(RestResponseCode restResponse, HttpHeaders responseHeaders) {
    return new RestResponseFactory<Void>(restResponse, responseHeaders).createResponseEntity();
  }

  public static ResponseEntity<RestResponse<Void>> createHeaderResponseEntity(HttpStatus httpStatus, HttpHeaders responseHeaders) {
    return createHeaderResponseEntity(DefaultResponseCode.findCode(httpStatus), responseHeaders);
  }

  public static <E> void setFullResponse(HttpServletResponse response, RestResponseCode restResponse, HttpHeaders responseHeaders, E result) throws IOException {
    new RestResponseFactory<>(restResponse, responseHeaders, result).setResponse(response);
  }

  public static <E> void setFullResponse(HttpServletResponse response, HttpStatus httpStatus, HttpHeaders responseHeaders, E result) throws IOException {
    setFullResponse(response, DefaultResponseCode.findCode(httpStatus), responseHeaders, result);
  }

  public static <E> void setFullResponse(HttpServletResponse response, RestResponseCode responseCode, HttpHeaders responseHeaders, E[] result, Pagination pagination) throws IOException {
    new RestResponseFactory<>(responseCode, responseHeaders, result, pagination).setResponse(response);
  }

  public static <E> void setFullResponse(HttpServletResponse response, HttpStatus httpStatus, HttpHeaders responseHeaders, E[] result, Pagination pagination) throws IOException {
    setFullResponse(response, DefaultResponseCode.findCode(httpStatus), responseHeaders, result, pagination);
  }

  public static <E extends Collection<?>> void setFullResponse(HttpServletResponse response, RestResponseCode responseCode, HttpHeaders responseHeaders, E result, Pagination pagination) throws IOException {
    new RestResponseFactory<>(responseCode, responseHeaders, result, pagination).setResponse(response);
  }

  public static <E extends Collection<?>> void setFullResponse(HttpServletResponse response, HttpStatus httpStatus, HttpHeaders responseHeaders, E result, Pagination pagination) throws IOException {
    setFullResponse(response, DefaultResponseCode.findCode(httpStatus), responseHeaders, result, pagination);
  }

  public static void setBasicResponse(HttpServletResponse response, RestResponseCode restResponse) throws IOException {
    new RestResponseFactory<>(restResponse).setResponse(response);
  }

  public static void setBasicResponse(HttpServletResponse response, HttpStatus httpStatus) throws IOException {
    setBasicResponse(response, DefaultResponseCode.findCode(httpStatus));
  }

  public static <E> void setResultResponse(HttpServletResponse response, RestResponseCode restResponse, E result) throws IOException {
    new RestResponseFactory<>(restResponse, null, result).setResponse(response);
  }

  public static <E> void setResultResponse(HttpServletResponse response, HttpStatus httpStatus, E result) throws IOException {
    setResultResponse(response, DefaultResponseCode.findCode(httpStatus), result);
  }

  public static <E> void setResultResponse(HttpServletResponse response, RestResponseCode responseCode, E[] result, Pagination pagination) throws IOException {
    new RestResponseFactory<>(responseCode, null, result, pagination).setResponse(response);
  }

  public static <E> void setResultResponse(HttpServletResponse response, HttpStatus httpStatus, E[] result, Pagination pagination) throws IOException {
    setResultResponse(response, DefaultResponseCode.findCode(httpStatus), result, pagination);
  }

  public static <E extends Collection<?>> void setResultResponse(HttpServletResponse response, RestResponseCode responseCode, E result, Pagination pagination) throws IOException {
    new RestResponseFactory<>(responseCode, null, result, pagination).setResponse(response);
  }

  public static <E extends Collection<?>> void setResultResponse(HttpServletResponse response, HttpStatus httpStatus, E result, Pagination pagination) throws IOException {
    setResultResponse(response, DefaultResponseCode.findCode(httpStatus), result, pagination);
  }

  public static void setHeaderResponse(HttpServletResponse response, RestResponseCode restResponse, HttpHeaders responseHeaders) throws IOException {
    new RestResponseFactory<>(restResponse, responseHeaders).setResponse(response);
  }

  public static void setHeaderResponse(HttpServletResponse response, HttpStatus httpStatus, HttpHeaders responseHeaders) throws IOException {
    setHeaderResponse(response, DefaultResponseCode.findCode(httpStatus), responseHeaders);
  }
}
