package com.rest.response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ResponseTestController {

  private final ResponseTestService responseTestService;

  @Autowired
  public ResponseTestController(ResponseTestService responseTestService) {

    this.responseTestService = responseTestService;
  }

  @GetMapping("/api/test")
  public ResponseEntity<RestResponse<List<String>>> getData() {

    return RestResponseFactory.createContentResponseEntity(HttpStatus.OK, responseTestService.getTest());
  }
}
