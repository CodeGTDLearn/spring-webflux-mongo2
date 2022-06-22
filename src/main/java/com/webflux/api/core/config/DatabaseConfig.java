package com.webflux.api.core.config;


import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

// ========================== PropertySource + ConfigurationProperties =============================
// Check - PropertySource: https://www.baeldung.com/configuration-properties-in-spring-boot
// Getter+Setter are CRUCIAL for PropertySource + ConfigurationProperties works properly
@PropertySource(value = "classpath:application.yml", factory = YamlProcessor.class)
@ConfigurationProperties(prefix = "spring.data.mongodb")
@Setter
@Getter
// =================================================================================================
@Profile({"dev-replicaset", "dev-standalone"})
@Import({TransactionManagerConfig.class})
@Slf4j
@Configuration
@EnableReactiveMongoRepositories(
     basePackages = {
          "com.webflux.api.modules.project.repo",
          "com.webflux.api.modules.task.repo"})
public class DatabaseConfig extends AbstractReactiveMongoConfiguration {

  private String uri;
  private String host;
  private String port;
  private String database;
  private String authenticationDatabase;
  private String username;
  private String password;

  @Autowired
  private Environment environment;


  @Override
  public MongoClient reactiveMongoClient() {
    /*╔════════════════════════════════════════════════╗
      ║ REPLICASET-THREE-NODES-MONGO-DB URL            ║
      ╠════════════════════════════════════════════════╩═════╗
      ║ mongodb://mongo1:9042,mongo2:9142,mongo3:9242/api-db ║
      ║           ?replicaSet=docker-rs&authSource=admin     ║
      ╚══════════════════════════════════════════════════════╝*/

    final String connection = getConnection(environment.getActiveProfiles()[0]);

    return MongoClients.create(connection);
  }

  @NotNull
  private String getConnection(String profile) {

    String connection;

    switch (profile) {

      case "replicaset1":
        connection = uri + "&authSource=" + authenticationDatabase;
        break;
      default:
        connection =
             "mongodb://" +
                  username + ":" + password +
                  "@" + host + ":" + port +
                  "/" + database +
                  "?authSource=" + authenticationDatabase;

    }

    System.out.println("Connecting " + profile + "-Profile ---> " + connection);
    return connection;
  }


  @Override
  protected String getDatabaseName() {

    return database;
  }
}
/*
      case "rs3-noauth":
        connection =
             rootUri +
                  "/" + database +
                  "?replicaSet=" + rsName +
                  "&authSource=" + authenticationDatabase;
        break;

      case "rs1-noauth":
        connection =
             rootUri +
                  "/?connect=direct" +
                  "&replicaSet=" + rsName +
                  "&readPreference=primary";
 */