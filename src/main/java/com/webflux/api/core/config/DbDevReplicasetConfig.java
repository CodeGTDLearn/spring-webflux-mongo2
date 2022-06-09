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
@Profile("rs1-noauth")
@Import({DbTransactionManagerConfig.class})
@Slf4j
@Configuration
@EnableReactiveMongoRepositories(
     basePackages = {
          "com.webflux.api.modules.project.repo",
          "com.webflux.api.modules.task.repo"})
public class DbDevReplicasetConfig extends AbstractReactiveMongoConfiguration {

  private String rootUri;
  private String db;
  private String rsName;
  private String authDb;
  private String username;
  private String password;

  @Override
  public MongoClient reactiveMongoClient() {
/*╔═══════════════════════════════════════════════════╗
  ║  REPLICASET-SINGLE-NODE-MONGO-DB DEVELOPMENT URL  ║
  ╠═══════════════════════════════════════════════════╩═════════════════╗
  ║ mongodb://myservice-mongodb:27017/                                  ║
  ║ ?connect=direct&replicaSet=singleNodeReplSet&readPreference=primary ║
  ╚═════════════════════════════════════════════════════════════════════╝*/
    final String appDbConnection =
         rootUri +
              "/?connect=direct" +
              "&replicaSet=" + rsName +
              "&readPreference=primary";

    System.out.println("Connect DB Replicaset-Single-Node ---> " + appDbConnection);

    return MongoClients.create(appDbConnection);
  }


  @Override
  protected String getDatabaseName() {

    return db;
  }
}