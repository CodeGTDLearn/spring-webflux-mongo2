package com.webflux.api.core.exception;

import com.webflux.api.core.TestDbUtilsConfig;
import com.webflux.api.modules.project.entity.Project;
import com.webflux.api.modules.task.Task;
import config.annotations.MergedResource;
import config.testcontainer.TcComposeConfig;
import config.utils.TestDbUtils;
import io.restassured.module.webtestclient.RestAssuredWebTestClient;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.EnabledIf;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.junit.jupiter.Container;
import reactor.core.publisher.Flux;

import java.util.Arrays;

import static com.webflux.api.modules.project.core.routes.RoutesCrud.ERROR_PATH;
import static com.webflux.api.modules.project.core.routes.RoutesCrud.PROJ_ROOT_CRUD;
import static config.databuilders.ProjectBuilder.projecNoID;
import static config.databuilders.TaskBuilder.taskWithID;
import static config.testcontainer.TcComposeConfig.TC_COMPOSE_SERVICE;
import static config.testcontainer.TcComposeConfig.TC_COMPOSE_SERVICE_PORT;
import static config.utils.BlockhoundUtils.bhWorks;
import static config.utils.RestAssureSpecs.requestSpecsSetPath;
import static config.utils.RestAssureSpecs.responseSpecs;
import static config.utils.TestUtils.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static java.util.Collections.singletonList;
import static java.util.List.of;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpStatus.NOT_FOUND;

// ==> EXCEPTIONS IN CONTROLLER:
// *** REASON: IN WEBFLUX, EXCEPTIONS MUST BE IN CONTROLLER - WHY?
//     - "Como stream pode ser manipulado por diferentes grupos de thread,
//     - caso um erro aconteça em uma thread que não é a que operou a controller,
//     - o ControllerAdvice não vai ser notificado "
//     - https://medium.com/nstech/programa%C3%A7%C3%A3o-reativa-com-spring-boot-webflux-e-mongodb-chega-de-sofrer-f92fb64517c3
@Import({TestDbUtilsConfig.class})
@DisplayName("GlobalExceptionTest")
@MergedResource
class GlobalExceptionTest {

  // STATIC-@Container: one service for ALL tests -> SUPER FASTER
  // NON-STATIC-@Container: one service for EACH test
  @Container
  private static final DockerComposeContainer<?> compose = new TcComposeConfig().getTcCompose();

  final String enabledTest = "true";

  // MOCKED-SERVER: WEB-TEST-CLIENT(non-blocking client)'
  // SHOULD BE USED WITH 'TEST-CONTAINERS'
  // BECAUSE THERE IS NO 'REAL-SERVER' CREATED VIA DOCKER-COMPOSE
  @Autowired
  WebTestClient mockedWebClient;

  @Autowired
  TestDbUtils dbUtils;

  @Autowired
  GlobalExceptionCustomAttributes globalException;

  private Project project1;


  @BeforeAll
  static void beforeAll(TestInfo testInfo) {

    globalBeforeAll();
    globalTestMessage(testInfo.getDisplayName(), "class-start");
    globalComposeServiceContainerMessage(compose,
                                         TC_COMPOSE_SERVICE,
                                         TC_COMPOSE_SERVICE_PORT
                                        );
    RestAssuredWebTestClient.reset();
    RestAssuredWebTestClient.requestSpecification =
         requestSpecsSetPath("http://localhost:8080/" + PROJ_ROOT_CRUD);
    RestAssuredWebTestClient.responseSpecification = responseSpecs();
  }


  @AfterAll
  static void afterAll(TestInfo testInfo) {

    globalAfterAll();
    globalTestMessage(testInfo.getDisplayName(), "class-end");
  }


  @BeforeEach
  void beforeEach(TestInfo testInfo) {

    //REAL-SERVER INJECTED IN WEB-TEST-CLIENT(non-blocking client)'
    //SHOULD BE USED WHEN 'DOCKER-COMPOSE' UP A REAL-WEB-SERVER
    //BECAUSE THERE IS 'REAL-SERVER' CREATED VIA DOCKER-COMPOSE
    // realWebClient = WebTestClient.bindToServer()
    //                      .baseUrl("http://localhost:8080/customer")
    //                      .build();

    globalTestMessage(testInfo.getTestMethod()
                              .toString(), "method-start");

    project1 = projecNoID("C",
                          "2020-05-05",
                          "2021-05-05",
                          1000L,
                          of("UK", "USA")
                         ).create();

    Project project2 = projecNoID("B",
                                  "2020-06-06",
                                  "2021-06-06",
                                  2000L,
                                  of("UK", "USA")
                                 ).create();
    Flux<Project> projectFlux = dbUtils.saveProjectList(Arrays.asList(project1, project2));
    dbUtils.countAndExecuteFlux(projectFlux, 2);

    Task task1 = taskWithID("3",
                            "Mark",
                            1000L
                           ).create();
    Flux<Task> taskFlux = dbUtils.saveTaskList(singletonList(task1));
    dbUtils.countAndExecuteFlux(taskFlux, 1);
  }


  @AfterEach
  void tearDown(TestInfo testInfo) {

    globalTestMessage(testInfo.getTestMethod()
                              .toString(), "method-end");
  }

  @Test
  @DisplayName("globalException")
  @EnabledIf(expression = enabledTest, loadContext = true)
  void globalException() {

    RestAssuredWebTestClient
         .given()
         .webTestClient(mockedWebClient)

         .when()
         .get(ERROR_PATH)

         .then()
         .statusCode(NOT_FOUND.value())
         .log()
         .everything()

         .body("Global-Dev-Atribute", equalTo(globalException.getDeveloperMessage()))
         .body("Global-Global-Atribute", equalTo(globalException.getGlobalMessage()))
         .body(matchesJsonSchemaInClasspath("contracts/exceptions/globalException.json"))
    ;
  }

  @Test
  @DisplayName("Global-Exception Error Stack")
  @EnabledIf(expression = enabledTest, loadContext = true)
  void globalExceptionErrorStack() {

    RestAssuredWebTestClient
         .given()
         .webTestClient(mockedWebClient)
         .queryParam("completeStackTrace", true)

         .when()
         .get(ERROR_PATH)

         .then()
         .statusCode(NOT_FOUND.value())
         .log()
         .everything()
    //         .body(
    //              globalException.getGlobalAttribute(),
    //              equalTo("404 NOT_FOUND \"" + globalException.getGlobalMessage() + "\"")
    //              )
    //         .body(
    //              globalException.getDeveloperAttribute(),
    //              equalTo(globalException.getDeveloperMessage())
    //              )
    //         .body(matchesJsonSchemaInClasspath
    //         ("contracts/exceptions/global/globalExceptionStack.json"))
    ;
  }

  @Test
  @EnabledIf(expression = enabledTest, loadContext = true)
  @DisplayName("BHWorks")
  void bHWorks() {

    bhWorks();
  }

}