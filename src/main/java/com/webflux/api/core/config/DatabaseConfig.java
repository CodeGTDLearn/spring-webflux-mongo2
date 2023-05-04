package com.webflux.api.core.config;


import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
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

import java.nio.file.Files;
import java.nio.file.Paths;

// ========================== PropertySource + ConfigurationProperties =============================
// Check - PropertySource: https://www.baeldung.com/configuration-properties-in-spring-boot
// Getter+Setter are CRUCIAL for PropertySource + ConfigurationProperties works properly
@PropertySource(value = "classpath:application.yml", factory = YamlProcessorConfig.class)
@ConfigurationProperties(prefix = "spring.data.mongodb")
@Setter
@Getter
@Profile({"replica-set", "stand-alone", "replica-set-auth"})
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
  private String replicasetName;


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
      case "replica-set":
        connection = uri + "&authSource=" + authenticationDatabase;
        break;

      case "replica-set-auth":
        connection =
             "mongodb://" +
                  // app_db_username + ":" + app_db_password +
                  getDockerSecret(username) + ":" + getDockerSecret(password) +
                  // URI: replicasetPrimary + ":" + replicasetPort
                  "@" + uri +
                  // DATABASE: OMMIT/SUPRESS database when it should be created late/after
                  "/" + getDockerSecret(database) +
                  "?replicaSet=" + replicasetName +
                  "&authSource=" + authenticationDatabase;
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

  @SneakyThrows
  private String getDockerSecret(String secretName) {
    // 1) Creates secret-path-folder
    final String dockerSecretsFolderPath = "/run/secrets/";
    String path = dockerSecretsFolderPath + secretName;
    String passwordSecret = "";

    // 2) if a secret is present, inject as a 'secret' (readAllBytes from path)
    if (Files.exists(Paths.get(path))) {

      final byte[] readFile = Files.readAllBytes(Paths.get(path));

      passwordSecret = new StringBuilder(
           new String(
                readFile))
           .toString();
    }

    return passwordSecret;
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