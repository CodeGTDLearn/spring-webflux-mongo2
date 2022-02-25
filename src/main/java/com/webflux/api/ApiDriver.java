package com.webflux.api;

import com.webflux.api.core.BlockhoundUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApiDriver {
  static {
    BlockhoundUtils.blockhoundInstallAllowAllCalls();
  }

  public static void main(String[] args) {

    SpringApplication.run(ApiDriver.class, args);
  }

}