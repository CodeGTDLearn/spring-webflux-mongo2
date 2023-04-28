package com.webflux.api.core.config.utils;

import io.restassured.module.webtestclient.RestAssuredWebTestClient;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.MongoDBContainer;

import java.util.stream.Stream;

import static com.webflux.api.core.config.utils.BlockhoundUtils.blockhoundInstallWithSpecificAllowedCalls;
import static com.webflux.api.core.config.utils.RestAssureSpecs.requestSpecs;
import static com.webflux.api.core.config.utils.RestAssureSpecs.responseSpecs;
import static com.webflux.api.core.config.utils.TestUtils.TestTitlesClass.*;
import static com.webflux.api.core.config.utils.TestUtils.TestTitlesContainer.*;
import static java.lang.String.valueOf;

@Slf4j
public class TestUtils {

  @BeforeAll
  public static void globalBeforeAll() {

    requestSpecs();
    responseSpecs();
    blockhoundInstallWithSpecificAllowedCalls();
    //  blockhoundInstallWithAllAllowedCalls();
  }

  @AfterAll
  public static void globalAfterAll() {

    RestAssuredWebTestClient.reset();
  }

  public static void globalTestMessage(
       String testDisplayName,
       TestTitlesClass testTitle) {

    final String error =
         "Error: Provide TestInfo testInfo.getTestMethod().toString()";

    // @formatter:off
    var testName =
         Stream.of(testDisplayName)
               .map(check -> check.contains("repetition") ? error : check)
               .map(text -> {
                 var check = text.contains("()]") & ! text.contains("repetition");
                 if (check) return text
                      .transform(txt -> txt.replace("()]", ""))
                      .transform(txt -> txt.substring(text.lastIndexOf(".") + 1))
                      .transform(txt -> txt.substring(0, 1).toUpperCase() + txt.substring(1));
                 return text;
               })
               .findAny()
               .get();
    // @formatter:on

    String title = switch (testTitle) {
      case CLASS_START -> CLASS_START.testType;
      case CLASS_END -> CLASS_END.testType;
      case METHOD_START -> METHOD_START.testType;
      case METHOD_END -> METHOD_END.testType;
    };

    ConsolePanelUtil.simplePanel(title, testName);
  }

  public static void globalContainerMessage(
       MongoDBContainer container,
       TestTitlesContainer testTitle
  ) {

    // @formatter:off
    if (container == null) return;

    String title = switch (testTitle) {
      case CONTAINER_START -> CONTAINER_START.testType;
      case CONTAINER_END -> CONTAINER_END.testType;
      case CONTAINER_STATE -> CONTAINER_STATE.testType;
    };

    ConsolePanelUtil.simplePanel(
         title,
         container.getContainerName().transform("Name: "::concat),
         container.getReplicaSetUrl().transform("Url: "::concat),
         "Running: ".concat(valueOf(container.isRunning())));
    // @formatter:on

  }

  public static void globalComposeServiceContainerMessage(
       DockerComposeContainer<?> compose,
       String service,
       Integer port) {

    // @formatter:off
    if (compose == null) return;

    var container = compose.getContainerByServiceName(service + "_1").get();

    var title = "TC-CONTAINER-COMPOSE";

    ConsolePanelUtil
         .simplePanel(
              title,
              service,
              compose.getServiceHost(service, port),
              compose.getServicePort(service, port).toString(),
              "Created: ".concat(valueOf(container.isCreated())),
              "Running: ".concat(valueOf(container.isRunning()))
         );
    // @formatter:on
  }

  public enum TestTitlesContainer {
    CONTAINER_START("STARTING TEST-CONTAINER..."),
    CONTAINER_END("...FINISHED TEST-CONTAINER"),
    CONTAINER_STATE("  ...TEST'S TC-CONTAINER  ");

    private final String testType;

    TestTitlesContainer(String testType) {

      this.testType = testType;
    }
  }

  public enum TestTitlesClass {
    CLASS_START(" STARTING TEST-CLASS..."),
    CLASS_END("...FINISHED TEST-CLASS "),
    METHOD_START("STARTING TEST-METHOD..."),
    METHOD_END("...FINISHED TEST-METHOD");

    private final String testType;

    TestTitlesClass(String testType) {

      this.testType = testType;
    }
  }
}