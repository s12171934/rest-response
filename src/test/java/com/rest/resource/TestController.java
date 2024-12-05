package com.rest.resource;

import com.rest.response.RestResponse;
import com.rest.response.RestResponseFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {

  @GetMapping
  public ResponseEntity<RestResponse<String>> test() {

    return RestResponseFactory.createContentResponseEntity(HttpStatus.OK, "test");
  }

  @GetMapping("/custom-exception")
  public ResponseEntity<RestResponse<String>> testCustomException() throws CustomTestException {

    throw new CustomTestException("");
  }

  @GetMapping("/custom-runtime")
  public ResponseEntity<RestResponse<String>> testCustomRuntime() {

    throw new CustomTestRuntimeException("");
  }
}
