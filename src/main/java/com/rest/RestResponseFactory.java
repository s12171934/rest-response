package com.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Properties;

public class RestResponseFactory<T> {

  private final RestResponseCode responseCode;
  private HttpHeaders headers;
  private RestResponse<T> restResponse;

  private RestResponseFactory(RestResponseCode responseCode, HttpHeaders headers, T result) {

    this.responseCode = responseCode;
    this.headers = headers;
    this.restResponse = new RestResponse<>(
        this.responseCode.getHttpStatus().is2xxSuccessful(),
        new RestResponse.Status(
            this.responseCode.getCode(),
            this.responseCode.getMessage()
        ),
        result,
        new RestResponse.MetaData(
            LocalDateTime.now(),
            ServletUriComponentsBuilder.fromCurrentRequest().toUriString(),
            getApiVersion()
        )
    );
  }

  private RestResponseFactory(RestResponseCode responseCode, HttpHeaders headers) {

    this.responseCode = responseCode;
    this.headers = headers;
  }

  private RestResponseFactory(RestResponseCode responseCode) {

    this.responseCode = responseCode;
  }

  private ResponseEntity<RestResponse<T>> createResponseEntity() {

    return new ResponseEntity<>(restResponse, this.headers, this.responseCode.getHttpStatus());
  }

  private String getApiVersion() {

    try {
      YamlPropertiesFactoryBean yamlPropertiesFactoryBean = new YamlPropertiesFactoryBean();
      yamlPropertiesFactoryBean.setResources(new ClassPathResource("application.yaml"));
      Properties properties = yamlPropertiesFactoryBean.getObject();
      return properties.getProperty("api.version", "no_version");
    }
    catch (Exception e) {
      return "no_version";
    }
  }

  private void setResponse(HttpServletResponse response) {

    response.setStatus(this.responseCode.getCode());
    response.setContentType("application/json;charset=UTF-8");

    for (String key : Objects.requireNonNull(this.headers.keySet())) {
      for (String value : Objects.requireNonNull(this.headers.get(key))) {
        response.setHeader(key, value);
      }
    }

    try {
      PrintWriter printWriter = response.getWriter();
      ObjectMapper objectMapper = new ObjectMapper();
      printWriter.write(objectMapper.writeValueAsString(this.restResponse));
      printWriter.flush();
      printWriter.close();
    }
    catch (IOException ioException) {

      throw new RuntimeException(ioException);
    }
  }

  public static <E> ResponseEntity<RestResponse<E>> createFullResponseEntity(RestResponseCode restResponse, HttpHeaders responseHeaders, E result) {

    return new RestResponseFactory<>(restResponse, responseHeaders, result).createResponseEntity();
  }

  public static <E> ResponseEntity<RestResponse<E>> createFullResponseEntity(HttpStatus httpStatus, HttpHeaders responseHeaders, E result) {

    return new RestResponseFactory<>(DefaultResponseCode.findCode(httpStatus), responseHeaders, result).createResponseEntity();
  }

  public static ResponseEntity<RestResponse<Void>> createBasicResponseEntity(RestResponseCode restResponse) {

    return new RestResponseFactory<Void>(restResponse).createResponseEntity();
  }

  public static ResponseEntity<RestResponse<Void>> createBasicResponseEntity(HttpStatus httpStatus) {

    return new RestResponseFactory<Void>(DefaultResponseCode.findCode(httpStatus)).createResponseEntity();
  }

  public static <E> ResponseEntity<RestResponse<E>> createResultResponseEntity(RestResponseCode restResponse, E result) {

    return new RestResponseFactory<>(restResponse, null, result).createResponseEntity();
  }

  public static <E> ResponseEntity<RestResponse<E>> createResultResponseEntity(HttpStatus httpStatus, E result) {

    return new RestResponseFactory<>(DefaultResponseCode.findCode(httpStatus), null, result).createResponseEntity();
  }

  public static ResponseEntity<RestResponse<Void>> createHeaderResponseEntity(RestResponseCode restResponse, HttpHeaders responseHeaders) {

    return new RestResponseFactory<Void>(restResponse, responseHeaders).createResponseEntity();
  }

  public static ResponseEntity<RestResponse<Void>> createHeaderResponseEntity(HttpStatus httpStatus, HttpHeaders responseHeaders) {

    return new RestResponseFactory<Void>(DefaultResponseCode.findCode(httpStatus), responseHeaders).createResponseEntity();
  }

  private static <E> void setFullResponse(HttpServletResponse response, RestResponseCode restResponse, HttpHeaders responseHeaders, E result) {

    new RestResponseFactory<>(restResponse, responseHeaders, result).setResponse(response);
  }

  private static <E> void setFullResponse(HttpServletResponse response, HttpStatus httpStatus, HttpHeaders responseHeaders, E result) {

    new RestResponseFactory<>(DefaultResponseCode.findCode(httpStatus), responseHeaders, result).setResponse(response);
  }

  private static void setBasicResponse(HttpServletResponse response, RestResponseCode restResponse) {

    new RestResponseFactory<>(restResponse).setResponse(response);
  }

  private static void setBasicResponse(HttpServletResponse response, HttpStatus httpStatus) {

    new RestResponseFactory<>(DefaultResponseCode.findCode(httpStatus)).setResponse(response);
  }

  private static <E> void setResultResponse(HttpServletResponse response, RestResponseCode restResponse, HttpHeaders responseHeaders, E result) {

    new RestResponseFactory<>(restResponse, responseHeaders, result).setResponse(response);
  }

  private static <E> void setResultResponse(HttpServletResponse response, HttpStatus httpStatus, HttpHeaders responseHeaders, E result) {

    new RestResponseFactory<>(DefaultResponseCode.findCode(httpStatus), responseHeaders, result).setResponse(response);
  }

  private static void setHeaderResponse(HttpServletResponse response, RestResponseCode restResponse, HttpHeaders responseHeaders) {

    new RestResponseFactory<>(restResponse, responseHeaders).setResponse(response);
  }

  private static void setHeaderResponse(HttpServletResponse response, HttpStatus httpStatus, HttpHeaders responseHeaders) {

    new RestResponseFactory<>(DefaultResponseCode.findCode(httpStatus), responseHeaders).setResponse(response);
  }
}
