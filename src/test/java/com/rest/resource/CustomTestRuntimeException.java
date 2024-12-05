package com.rest.resource;

import com.rest.exception.BaseRuntimeException;
import com.rest.exception.CustomException;

@CustomException(codeClass = TestResponseCode.class, codeName = "RUNTIME_EXCEPTION")
public class CustomTestRuntimeException extends BaseRuntimeException {
  public CustomTestRuntimeException(String message) {
    super(message);
  }
}
