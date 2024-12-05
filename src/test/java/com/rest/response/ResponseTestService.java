package com.rest.response;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResponseTestService {

  public List<String> getTest() {
    return List.of(new String[]{"test1", "test2"});
  }
}