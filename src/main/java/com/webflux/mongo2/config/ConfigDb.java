package com.webflux.mongo2.config;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@Configuration
@EnableReactiveMongoRepositories(
     basePackages = {
          "com.webflux.mongo2.project.repo",
          "com.webflux.mongo2.task.repo"})
public class ConfigDb extends AbstractReactiveMongoConfiguration {

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

  //  @Autowired
  //  private MappingMongoConverter mongoConverter;


  @Override
  public MongoClient reactiveMongoClient() {

    return MongoClients.create(
         "mongodb://"
              + replicasetUsername + ":" + replicasetPassword +
              "@" + replicasetPrimary + ":" + replicasetPort + "/"
              + database
              + "?replicaSet=" + replicasetName
              + "&authSource=" + replicasetAuthenticationDb);

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