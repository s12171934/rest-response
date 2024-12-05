package com.rest.exception;

import com.rest.response.RestResponse;
import com.rest.response.RestResponseCode;
import com.rest.response.RestResponseFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;

@RestControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
public class BaseControllerAdvice {

  @ExceptionHandler({BaseException.class, BaseRuntimeException.class})
  public ResponseEntity<RestResponse<Void>> handleBaseException(Exception exception) {

    CustomException annotation = exception.getClass().getAnnotation(CustomException.class);

    try {

      Class<? extends RestResponseCode> codeClass = annotation.codeClass();
      String codeName = annotation.codeName();

      RestResponseCode responseCode = Arrays.stream(codeClass.getEnumConstants())
          .filter(code -> code.toString().equals(codeName))
          .findFirst()
          .orElse(null);

      return RestResponseFactory.createBasicResponseEntity(responseCode);

    } catch (Exception e) {

      return handleDefaultException();
    }
  }

  public ResponseEntity<RestResponse<Void>> handleDefaultException() {

    return RestResponseFactory.createBasicResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
