package com.webflux.api.core.config;


import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

import java.nio.file.Files;
import java.nio.file.Paths;

@PropertySource(value = "classpath:application.yml", factory = YamlProcessor.class)
@ConfigurationProperties(prefix = "db.mongodb.replicaset")
@Setter
@Getter
@Profile("prod-single-node-rs-auth")
@Import({TransactionManagerConfig.class})
@Slf4j
@Configuration
@EnableReactiveMongoRepositories(basePackages = {"com.mongo.rs.modules.user"})
public class ReplicasetSingleNodeAuthConfig extends AbstractReactiveMongoConfiguration {

  private String uri;
  private String database;
  private String replicasetName;
  private String authenticationDatabase;
  private String username;
  private String password;

  @Override
  public MongoClient reactiveMongoClient() {
    /*╔══════════════════════════════════════════════════════════════════════╗
      ║ REPLICASET-3-NODES-MONGO-DB PRODUCTION URL (FULL:NO USER + PASSWORD) ║
      ╠══════════════════════════════════════════════════════════════════════╣
      ║ mongodb://app_db_username:app_db_password                                          ║
      ║           @mongo1:9042,mongo2:9142,mongo3:9242/api-db                ║
      ║           ?replicaSet=docker-rs&authSource=app_db_name.txt                     ║
      ╚══════════════════════════════════════════════════════════════════════╝*/
    final String connection =
         "mongodb://" +
              // app_db_username + ":" + app_db_password +
              getDockerSecret(username) + ":" + getDockerSecret(password) +
              // ROOTURI: replicasetPrimary + ":" + replicasetPort
              "@" + uri +
              // DATABASE: OMMIT/SUPRESS database when it should be created late/after
              "/" + getDockerSecret(database) +
              "?replicaSet=" + replicasetName +
              "&authSource=" + authenticationDatabase;


    /*╔══════════════════════════════════════════════════════════════════════╗
      ║      REPLICASET-3-NODES-MONGO-DB PRODUCTION URL (UDEMY COURSE)       ║
      ╚══════════════════════════════════════════════════════════════════════╝*/
    // "mongodb://" +
    //      replicasetUsername + ":" + replicasetPassword +
    //      "@" + replicasetPrimary + ":" + replicasetPort +
    //      "/" + database +
    //      "?replicaSet=" + replicasetName +
    //      "&authSource=" + replicasetAuthenticationDb

    System.out.println(
         "DevThreeNodesReplicasetAuth ---> " + connection);

    return MongoClients.create(connection);
  }


  @Override
  protected String getDatabaseName() {

    return getDockerSecret(database) ;
  }

  @SneakyThrows
  private String getDockerSecret(String secretName) {
    // 1) Creates secret-path-folder
    final String dockerSecretsFolderPath = "/run/secrets/";
    String secretPath = dockerSecretsFolderPath + secretName;
    String passwordSecret = "";

    // 2) if a secret is present, inject as a 'secret' (readAllBytes from secretPath)
    if (Files.exists(Paths.get(secretPath))) {

      final byte[] readFile = Files.readAllBytes(Paths.get(secretPath));

      passwordSecret = new StringBuilder(
           new String(
                readFile))
           .toString();
    }

    return passwordSecret;
  }
}