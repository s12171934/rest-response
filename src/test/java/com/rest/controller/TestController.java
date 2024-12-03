package com.rest.controller;

import com.rest.RestResponse;
import com.rest.RestResponseFactory;
import com.rest.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {

  private final TestService testService;

  @Autowired
  public TestController(TestService testService) {

    this.testService = testService;
  }

  @GetMapping("/api/test")
  public ResponseEntity<RestResponse<List<String>>> getData() {

    return RestResponseFactory.createResultResponseEntity(HttpStatus.OK, testService.getTest());
  }
}
