package com.webflux.api.core.config;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import lombok.Setter;
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
// Setter are CRUCIAL for PropertySource + ConfigurationProperties works properly
@PropertySource(value = "classpath:application-dev.yml", ignoreResourceNotFound = true)
@ConfigurationProperties(prefix = "spring.data.mongodb")
@Setter
// =================================================================================================
@Profile("dev")
@Configuration
//@EnableTransactionManagement
@EnableReactiveMongoRepositories(
     basePackages = {
          "com.webflux.mongo2.project",
          "com.webflux.mongo2.task.repo"})
public class ConfigDbDevProfile extends AbstractReactiveMongoConfiguration {
  private String database;
  private String host;
  private String port;
  private String username;
  private String password;
  private String authenticationDatabase;


  // 01) REACTIVE-MONGO-TEMPLATE-BEANS:
  @Override
  public MongoClient reactiveMongoClient() {

    String connectionURI = "mongodb://" +
         username + ":" + password + "@" +
         host + ":" + port + "/" + database +
         "?authSource=" + authenticationDatabase +
         "&authMechanism=SCRAM-SHA-1";

    // TEMPLATE-URI: mongodb://test:pass@host1:port/?authSource=admin&authMechanism=SCRAM-SHA-1

    System.out.println("Connection --------------->  URI ---------------> :" + connectionURI);
    //    Caused by: java.lang.IllegalArgumentException: The connection string contains an
    //    invalid host '${HOST}:${PORT}'. The port '${PORT}' is not a valid, it must be an
    //    integer between 0 and 65535
    //    at com.mongodb.ConnectionString.validatePort(ConnectionString.java:1069)
    //    at com.mongodb.ConnectionString.parseHosts(ConnectionString.java:1049)
    //    at com.mongodb.ConnectionString.<init>(ConnectionString.java:350)
    //    at com.mongodb.reactivestreams.client.MongoClients.create(MongoClients.java:62)
    //    at com.webflux.mongo2.core.database.DevConfigDb.reactiveMongoClient(DevConfigDb.java:52)

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