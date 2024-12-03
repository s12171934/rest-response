package com.rest.service;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestService {

  public List<String> getTest() {
    return List.of(new String[]{"test1", "test2"});
  }
}