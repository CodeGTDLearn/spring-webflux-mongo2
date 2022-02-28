package com.webflux.api.core.config.testcontainer;

import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.io.File;
import java.time.Duration;

/*
     ╔═══════════════════════════════╗
     ║ USING CONTAINERS IN THE TESTS ║
     ╠═══════════════════════════════╩════════════════╗
     ║ CONFLICT: TEST-CONTAINERS X DOCKER-CONTAINERS  ║
     ║           THEY DO NOT WORKS TOGETHER           ║
     ╠════════════════════════════════════════════════╩═════════╗
     ║A) TEST-CONTAINERS:                                       ║
     ║   A.1) STOP+CLEAN DOCKER-CONTAINERS  (DOCKER-BAT-SCRIPT) ║
     ║   A.2) "UNCOMMENT" THE TEST-CONTAINERS-ANNOTATION BELOW  ║
     ║   A.3) RUN THE TESTS                                     ║
     ║                                                          ║
     ║B) DOCKER-CONTAINERS:                                     ║
     ║   B.1) SET PROFILE-ACTIVE IN APPLICATION.YML             ║
     ║   B.2) "COMMENT" THE TEST-CONTAINERS-ANNOTATION BELOW    ║
     ║   B.3) START DOCKER-CONTAINER (DOCKER-BAT-SCRIPT-PROFILE)║
     ║   B.4) RUN THE TESTS                                     ║
     ╚══════════════════════════════════════════════════════════╝
*/
/*
SPEED-UP TESTCONTAINERS
https://callistaenterprise.se/blogg/teknik/2020/10/09/speed-up-your-testcontainers-tests/
https://medium.com/pictet-technologies-blog/speeding-up-your-integration-tests-with
-testcontainers-e54ab655c03d
 */
public class TestcontainerComposeConfig {

  final static public int STARTUP_TIMEOUT = 15;
  final static public int SERVICE_PORT = 27017;
  final static public String SERVICE = "api-test";
  final static private String COMPOSE_PATH = "src/test/resources/compose-test.yml";

  //format 01: using a variable to create the tcContainerCompose
  private final DockerComposeContainer<?> testcontainerCompose =
       new DockerComposeContainer<>(new File(COMPOSE_PATH))
            .withExposedService(SERVICE,
                                SERVICE_PORT,
                                Wait.forListeningPort()
                                    .withStartupTimeout(
                                         Duration.ofSeconds(STARTUP_TIMEOUT))
                               );

  //format 02: using a getter/accessor to create the tcContainerCompose
  public DockerComposeContainer<?> getContainer() {

    return testcontainerCompose;
  }
}