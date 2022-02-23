package config.utils;

import io.restassured.module.webtestclient.RestAssuredWebTestClient;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.MongoDBContainer;

import static config.utils.BlockhoundUtils.blockhoundInstallSimple;
import static config.utils.RestAssureSpecs.requestSpecs;
import static config.utils.RestAssureSpecs.responseSpecs;

@Slf4j
public class TestUtils {


  @BeforeAll
  public static void globalBeforeAll() {
//    try {
//      TimeUnit.SECONDS.sleep(5);
//    } catch (InterruptedException ie) {
//      Thread.currentThread().interrupt();
//    }

    requestSpecs();
    responseSpecs();
    blockhoundInstallSimple();
    //    blockhoundInstallAllowAllCalls();
  }


  @AfterAll
  public static void globalAfterAll() {

    RestAssuredWebTestClient.reset();
  }


  public static void globalTestMessage(String subTitle, String testType) {


    if (subTitle.contains("repetition"))
      subTitle = "Error: Provide TestInfo testInfo.getTestMethod().toString()";

    if (subTitle.contains("()]")) {
      subTitle = subTitle.replace("()]", "");
      subTitle = subTitle.substring(subTitle.lastIndexOf(".") + 1);
      subTitle = subTitle.substring(0, 1)
                         .toUpperCase() + subTitle.substring(1);
    }

    String title =
         switch (testType.toLowerCase()) {
           case "class-start" -> " STARTING TEST-CLASS...";
           case "class-end" -> "...FINISHED TEST-CLASS ";
           case "method-start" -> "STARTING TEST-METHOD...";
           case "method-end" -> "...FINISHED TEST-METHOD";
           default -> "";
         };

    System.out.printf(
         """
              ╔════════════════════════════════════════════════════════════════════╗
              ║                       %s                                           ║
              ║ --> Name: %s %38s%n"
              ╚════════════════════════════════════════════════════════════════════╝
                       
              """,
         title, subTitle, "║"
                     );
  }


  public static void globalContainerMessage(MongoDBContainer container, String typeTestMessage) {

    String title =
         switch (typeTestMessage.toLowerCase()) {
           case "container-start" -> "STARTING TEST-CONTAINER...";
           case "container-end" -> "...FINISHED TEST-CONTAINER";
           case "container-state" -> "  ...TEST'S TC-CONTAINER  ";
           default -> "";
         };

    System.out.printf(
         """
              ╔═══════════════════════════════════════════════════════════════════════╗
              ║ --> Name: %s
              ║ --> Url: %s
              ║ --> Running: %s
              ╚═══════════════════════════════════════════════════════════════════════╝
              """,
         title,
         container.getContainerName(),
         container.getReplicaSetUrl(),
         container.isRunning()
                     );
  }


  public static void globalComposeServiceContainerMessage(
       DockerComposeContainer<?> compose,
       String service,
       Integer port) {

    System.out.printf(
         """ 
              ╔═══════════════════════════════════════════════════════════════════════
              ║                           %s                        ║
              ║ --> Service: %s
              ║ --> Host: %s
              ║ --> Port: %s
              ║ --> Created: %s
              ║ --> Running: %s
              ╚═══════════════════════════════════════════════════════════════════════
               """,
         "TC-CONTAINER-COMPOSE",
         service,
         compose.getServiceHost(service, port),
         compose.getServicePort(service, port),
         compose.getContainerByServiceName(service + "_1")
                .get()
                .isCreated(),
         compose.getContainerByServiceName(service + "_1")
                .get()
                .isRunning()
                     );
  }
}