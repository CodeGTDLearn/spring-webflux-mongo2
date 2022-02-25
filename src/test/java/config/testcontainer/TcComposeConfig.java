package config.testcontainer;

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
public class TcComposeConfig {

  final static public int TC_COMPOSE_STARTUP_SECONDS_TIMEOUT = 15;
  final static public int TC_COMPOSE_SERVICE_PORT = 27017;
  final static public String TC_COMPOSE_SERVICE = "api-db-tc";
  final static private String TC_COMPOSE_PATH_SIMPLE =
       "src/test/resources/tc-composes/tc-compose-simple.yml";


  //format 01: using a variable to create the tcContainerCompose
  private final DockerComposeContainer<?> tcCompose =
       new DockerComposeContainer<>(new File(TC_COMPOSE_PATH_SIMPLE))
            .withExposedService(TC_COMPOSE_SERVICE,
                                TC_COMPOSE_SERVICE_PORT,
                                Wait.forListeningPort()
                                    .withStartupTimeout(
                                         Duration.ofSeconds(TC_COMPOSE_STARTUP_SECONDS_TIMEOUT))
                               )
       ;

  //format 02: using a getter/accessor to create the tcContainerCompose
  public DockerComposeContainer<?> getTcCompose() {

    return tcCompose;
  }
}