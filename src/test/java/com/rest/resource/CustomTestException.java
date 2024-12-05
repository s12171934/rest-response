package com.rest.resource;

import com.rest.exception.BaseException;
import com.rest.exception.CustomException;

@CustomException(codeClass = TestResponseCode.class, codeName = "TEST_BAD_REQUEST")
public class CustomTestException extends BaseException {
  public CustomTestException(String message) {
    super(message);
  }
}
