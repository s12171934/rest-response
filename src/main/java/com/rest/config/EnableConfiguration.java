package com.rest.config;

import com.rest.exception.BaseControllerAdvice;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class EnableConfiguration {

  @Bean
  public AppProperties appProperties() {

    return new AppProperties();
  }

  @Bean
  public BaseControllerAdvice baseControllerAdvice() {

    return new BaseControllerAdvice();
  }
}
