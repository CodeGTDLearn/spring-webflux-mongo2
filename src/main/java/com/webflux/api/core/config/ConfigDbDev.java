package com.webflux.api.core.config;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.ReactiveMongoTransactionManager;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

// ========================== PropertySource + ConfigurationProperties =============================
// Check - PropertySource: https://www.baeldung.com/configuration-properties-in-spring-boot
// Getter+Setter are CRUCIAL for PropertySource + ConfigurationProperties works properly
@PropertySource(value = "classpath:application-dev.yml", factory = YamlFileConverter.class)
@ConfigurationProperties(prefix = "spring.data.mongodb")
@Setter
@Getter
// =================================================================================================
@Slf4j
@Profile("dev")
@Configuration
@EnableReactiveMongoRepositories(
     basePackages = {
          "com.webflux.api.modules.project.repo",
          "com.webflux.api.modules.task.repo"})
public class ConfigDbDev extends AbstractReactiveMongoConfiguration {
  private String database;
  private String host;
  private String port;
  private String username;
  private String password;
  private String authenticationDatabase;

  // 01) REACTIVE-MONGO-TEMPLATE-BEANS:
  @Override
  public MongoClient reactiveMongoClient() {
/*
     ╔═══════════════════════════════╗
     ║        SIMPLE-MONGO-DB        ║
     ╚═══════════════════════════════╝
*/
    String connectionURI = "mongodb://" +
         username + ":" + password + "@" +
         host + ":" + port + "/" + database +
         "?authSource=" + authenticationDatabase +
         "&authMechanism=SCRAM-SHA-1";

    System.out.println("Connection --------------->  URI  ---------------> :" + connectionURI);

    return MongoClients.create(connectionURI);
  }


  @Override
  protected String getDatabaseName() {

    return database;
  }


  @Bean
  public ReactiveMongoTemplate reactiveMongoTemplate() {

    return new ReactiveMongoTemplate(reactiveMongoClient(), getDatabaseName());
  }

  // 02) TRANSACTION-BEANS:
  @Bean
  ReactiveMongoTransactionManager transactionManager(ReactiveMongoDatabaseFactory factory) {

    return new ReactiveMongoTransactionManager(factory);
  }


  // 03) GRID-FS-BEANS:
  //  @Bean
  //  public ReactiveGridFsTemplate reactiveGridFsTemplate() throws Exception {
  //    return new ReactiveGridFsTemplate(reactiveMongoDbFactory(),mongoConverter);
  //  }
}