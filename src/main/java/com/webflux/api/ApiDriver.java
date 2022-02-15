package com.webflux.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.blockhound.BlockHound;

@SpringBootApplication
public class ApiDriver {
  static {
    BlockHound.install();
  }

  public static void main(String[] args) {
    SpringApplication.run(ApiDriver.class,args);
  }

}