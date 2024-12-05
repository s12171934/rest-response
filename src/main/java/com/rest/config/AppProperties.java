package com.rest.config;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "rest.response")
public class AppProperties {

  private static AppProperties instance;
  private String version = "NO_VERSION";

  @PostConstruct
  public void init() {

    instance = this;
  }

  public static String restApiVersion() {

    return instance.version;
  }

  public String getVersion() {

    return this.version;
  }

  public void setVersion(String version) {

    this.version = version;
  }
}
