package com.webflux.api.core.config.testcontainer.container;

import org.junit.jupiter.api.extension.Extension;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;

import static com.webflux.api.core.config.utils.TestUtils.TestTitlesContainer.*;
import static com.webflux.api.core.config.utils.TestUtils.globalContainerMessage;
import static java.lang.System.setProperty;

/*╔════════════════════════════════════════════════════════════╗
  ║  TEST-TRANSACTIONS + TEST-CONTAINERS + TCCONTAINER-CONFIG  ║
  ╠════════════════════════════════════════════════════════════╣
  ║ a) TRANSACTIONS IN MONGO-DB DEPENDS ON THE REPLICASET      ║
  ║    - MEANING: TRANSACTIONS ONLY WILL WORK WITH REPLICASET  ║
  ║                                                            ║
  ║ b) MongoDBContainer provides REPLICASET automatically      ║
  ║    - MEANING:                                              ║
  ║      B.1) TESTS MUST BE DONE WITH "MongoDBContainer"       ║
  ║      B.2) DO NOT USE TEST-CONTAINER-DOCKER-COMPOSE-MODULE  ║
  ╚════════════════════════════════════════════════════════════╝*/
public class TcContainerConfig implements Extension {

  private final static String IMAGE = "mongo:4.4.4";
  private final static String URI_TEST_APPLICATION_PROPERTY = "spring.data.mongodb.uri";

  /*╔════════════════════════════════════════════════╗
    ║            TEST-CONTAINER-STATIC               ║
    ╠════════════════════════════════════════════════╣
    ║ A) STATIC:                                     ║
    ║ -> One service/container for each TEST-CLASS   ║
    ║ -> SUPER FASTER                                ║
    ║                                                ║
    ║ B) NON-STATIC:                                 ║
    ║ -> One service/container for 'EACH' TEST-METHOD║
    ║ -> SLOW                                        ║
    ╚════════════════════════════════════════════════╝*/
  private static final MongoDBContainer CONTAINER = new MongoDBContainer(
       DockerImageName.parse(IMAGE));

  /*
   ╔════════════════════════════════════════════════════════════════════════════╗
   ║                        ANNOTATION+EXTENSION-CLASSES                        ║
   ╠════════════════════════════════════════════════════════════════════════════╣
   ║ a) In 'ANNOTATION-EXTENSION-CLASSES' only this STATIC-METHOD is allowed    ║
   ║ b) This STATIC-METHOD 'starts automatically'(auto-starting)                ║
   ║    "WHEN" the class is called/started via ANNOTATION                       ║
   ╚════════════════════════════════════════════════════════════════════════════╝
  */
  static {
    startTcContainer();
  }

  public static void startTcContainer() {

    CONTAINER.isHealthy();
    CONTAINER.start();
    System.out.println("checando" + CONTAINER.getReplicaSetUrl());
    setProperty(URI_TEST_APPLICATION_PROPERTY, CONTAINER.getReplicaSetUrl());
    globalContainerMessage(getTcContainer(), CONTAINER_START);
    globalContainerMessage(getTcContainer(), CONTAINER_STATE);
  }


  public static void restartTcContainer() {

    globalContainerMessage(getTcContainer(), CONTAINER_END);
    CONTAINER.close();
    startTcContainer();
  }


  public static void closeTcContainer() {

    setReuseTcContainer(false);
    globalContainerMessage(getTcContainer(), CONTAINER_END);
    if (! CONTAINER.isShouldBeReused()) CONTAINER.stop();
  }


  public static void setReuseTcContainer(boolean reuse) {

    CONTAINER.withReuse(reuse);
  }

  public static void checkTcContainer() {

    getTcContainer().isHealthy();
    getTcContainer().isCreated();
    getTcContainer().isRunning();
    globalContainerMessage(getTcContainer(), CONTAINER_START);
    globalContainerMessage(getTcContainer(), CONTAINER_STATE);
  }

  public static MongoDBContainer getTcContainer() {

    return CONTAINER;
  }
}