package com.webflux.mongo2.config;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@Profile({"dev"})
@Configuration
@EnableReactiveMongoRepositories(
     basePackages = {
          "com.webflux.mongo2.project.repo",
          "com.webflux.mongo2.task.repo"})
public class ConfigDbDev extends AbstractReactiveMongoConfiguration {

  @Value("${spring.data.mongodb.database}")
  private String database;

  @Value("${spring.data.mongodb.host}")
  private String host;

  @Value("${spring.data.mongodb.port}")
  private String port;

  @Value("${spring.data.mongodb.username}")
  private String username;

  @Value("${spring.data.mongodb.password}")
  private String password;

  @Value("${spring.data.mongodb.authentication-database}")
  private String auth;

  //  @Autowired
  //  private MappingMongoConverter mongoConverter;


  @Override
  public MongoClient reactiveMongoClient() {

    return MongoClients.create(
         "mongodb://" +
              username + ":" + password + "@" +
              host + ":" + port + "/" + database +
              "?authSource=" + auth);

  }


  @Override
  protected String getDatabaseName() {
    // TODO Auto-generated method stub
    return database;
  }


  //  @Bean
  //  public ReactiveMongoTemplate reactiveMongoTemplate() {
  //    return new ReactiveMongoTemplate(reactiveMongoClient(),getDatabaseName());
  //  }
  //
  //
  //  @Bean
  //  ReactiveMongoTransactionManager transactionManager(ReactiveMongoDatabaseFactory factory) {
  //    return new ReactiveMongoTransactionManager(factory);
  //  }
  //
  //
  //  @Bean
  //  public ReactiveGridFsTemplate reactiveGridFsTemplate() throws Exception {
  //    return new ReactiveGridFsTemplate(reactiveMongoDbFactory(),mongoConverter);
  //  }
}