package com.webflux.api.core.config;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.ReactiveMongoTransactionManager;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@Profile("prod")
@Configuration
@EnableReactiveMongoRepositories(
     basePackages = {
          "com.webflux.mongo2.project.repo",
          "com.webflux.mongo2.task.repo"})
public class ConfigDbProdProfile extends AbstractReactiveMongoConfiguration {

  @Value("${udemy.mongodb.replicaset.name}")
  private String replicasetName;

  @Value("${udemy.mongodb.replicaset.username}")
  private String replicasetUsername;

  @Value("${udemy.mongodb.replicaset.password}")
  private String replicasetPassword;

  @Value("${udemy.mongodb.replicaset.primary}")
  private String replicasetPrimary;

  @Value("${udemy.mongodb.replicaset.port}")
  private String replicasetPort;

  @Value("${udemy.mongodb.replicaset.database}")
  private String database;

  @Value("${udemy.mongodb.replicaset.authentication-database}")
  private String replicasetAuthenticationDb;


  //  private MappingMongoConverter mongoConverter;


  // 01) REACTIVE-MONGO-TEMPLATE-BEANS:
  @Override
  public MongoClient reactiveMongoClient() {

    String connectionURI = "mongodb://"
         + replicasetUsername + ":" + replicasetPassword +
         "@" + replicasetPrimary + ":" + replicasetPort + "/"
         + database
         + "?replicaSet=" + replicasetName
         + "&authSource=" + replicasetAuthenticationDb;

    System.out.println("Connection --------------->  URI ---------------> :" + connectionURI);

    return MongoClients.create(connectionURI);

  }


  @Override
  protected String getDatabaseName() {
    return database;
  }


  @Bean
  public ReactiveMongoTemplate reactiveRepoTemplate() {
    return new ReactiveMongoTemplate(reactiveMongoClient(), getDatabaseName());
  }

  // 02) TRANSACTION-BEANS:
//  @Bean
//  TransactionalOperator transactionOperator(ReactiveTransactionManager operator) {
//    return new TransactionalOperator(operator);
//  }


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