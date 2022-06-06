package com.webflux.api.core.config.profiles;

import org.springframework.test.context.ActiveProfilesResolver;

import java.util.Map;

public class MyActiveProfilesResolver  implements ActiveProfilesResolver {
  @Override
  public String[] resolve(Class<?> testClass) {

      Map<String, String> env =  System.getenv();

      String profile;

switch (){
  case "webflux":
    break;
    default: profile =
}

    if (env.containsKey("TEST_ENV")) {
          profile = env.get("TEST_ENV");
      } else {
          profile = "local-test"; // it is more convenient to to have this as default to be used in dev env and override it on test env
      }
      return new String[]{profile};
  }
}