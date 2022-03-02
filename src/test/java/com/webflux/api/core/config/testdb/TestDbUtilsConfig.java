package com.webflux.api.core.config.testdb;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.ReactiveMongoTransactionManager;

@TestConfiguration
public class TestDbUtilsConfig {

  /*
   ╔════════════════════════════════════════════════╗
   ║           TRANSACTION-MANAGER-BEAN             ║
   ╠════════════════════════════════════════════════╣
   ║ THIS TRANSACTION-MANAGER-BEAN IS NECESSARY IN: ║
   ║ A) APP-CONTEXT  -> @Configuration              ║
   ║    - SRC/MAIN/JAVA/com/webflux/api/core/config ║
   ║                                                ║
   ║ B) TEST-CONTEXT -> @TestConfiguration          ║
   ║    - SRC/TEST/JAVA/com/webflux/api/core/config ║
   ╚════════════════════════════════════════════════╝
  */
  @Bean
  ReactiveMongoTransactionManager transactionManager(ReactiveMongoDatabaseFactory factory) {

    return new ReactiveMongoTransactionManager(factory);
  }

  @Bean
  public TestDbUtils testDbUtils() {

    return new TestDbUtils();
  }
}