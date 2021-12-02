package com.webflux.mongo2.core;

import config.utils.TestDbUtils;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestDbUtilsConfig {

  @Bean
  public TestDbUtils testDbUtils() {
    return new TestDbUtils();
  }
}