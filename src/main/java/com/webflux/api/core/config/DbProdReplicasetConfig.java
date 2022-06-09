package com.webflux.api.core.config;


import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

// ========================== PropertySource + ConfigurationProperties =============================
// Check - PropertySource: https://www.baeldung.com/configuration-properties-in-spring-boot
// Getter+Setter are CRUCIAL for PropertySource + ConfigurationProperties works properly
@PropertySource(value = "classpath:application.yml", factory = YmlConverter.class)
@ConfigurationProperties(prefix = "db.mongodb.replicaset")
@Setter
@Getter
// =================================================================================================
@Profile("rs3-noauth")
@Import({DbTransactionManagerConfig.class})
@Slf4j
@Configuration
@EnableReactiveMongoRepositories(
     basePackages = {
          "com.webflux.api.modules.project.repo",
          "com.webflux.api.modules.task.repo"})
public class DbProdReplicasetConfig extends AbstractReactiveMongoConfiguration {

  private String rootUri;
  private String db;
  private String rsName;
  private String authDb;
  private String username;
  private String password;

  @Override
  public MongoClient reactiveMongoClient() {
    /*╔════════════════════════════════════════════════╗
      ║ REPLICASET-THREE-NODES-MONGO-DB PRODUCTION URL ║
      ╠════════════════════════════════════════════════╩═════╗
      ║ mongodb://mongo1:9042,mongo2:9142,mongo3:9242/api-db ║
      ║           ?replicaSet=docker-rs&authSource=admin     ║
      ╚══════════════════════════════════════════════════════╝*/

    final String appDbConnection =
         rootUri +
              "/" + db +
              "?replicaSet=" + rsName +
              "&authSource=" + authDb;

    System.out.println("Connection Replicaset ---> " + appDbConnection);

    return MongoClients.create(appDbConnection);
  }


  @Override
  protected String getDatabaseName() {

    return db;
  }
}