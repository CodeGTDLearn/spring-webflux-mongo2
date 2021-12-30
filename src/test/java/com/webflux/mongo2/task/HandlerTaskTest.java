package com.webflux.mongo2.task;

import com.webflux.mongo2.core.TestDbUtilsConfig;
import com.webflux.mongo2.project.entity.Project;
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

import static com.webflux.mongo2.config.routes.task.RoutesTask.*;
import static config.databuilders.ProjectBuilder.projectWithID;
import static config.databuilders.TaskBuilder.taskWithID;
import static config.testcontainer.TcComposeConfig.TC_COMPOSE_SERVICE;
import static config.testcontainer.TcComposeConfig.TC_COMPOSE_SERVICE_PORT;
import static config.utils.RestAssureSpecs.requestSpecsSetPath;
import static config.utils.RestAssureSpecs.responseSpecs;
import static config.utils.TestUtils.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static java.util.Collections.singletonList;
import static java.util.List.of;
import static org.hamcrest.CoreMatchers.containsStringIgnoringCase;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.HttpStatus.OK;

@Import({TestDbUtilsConfig.class})
@DisplayName("HandlerTaskTest")
@MergedResource
class HandlerTaskTest {

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

  private Task task1;


  @BeforeAll
  static void beforeAll(TestInfo testInfo) {
    globalBeforeAll();
    globalTestMessage(testInfo.getDisplayName(),"class-start");
    globalComposeServiceContainerMessage(compose,
                                         TC_COMPOSE_SERVICE,
                                         TC_COMPOSE_SERVICE_PORT
                                        );
    RestAssuredWebTestClient.reset();
    RestAssuredWebTestClient.requestSpecification =
         requestSpecsSetPath("http://localhost:8080/");
    RestAssuredWebTestClient.responseSpecification = responseSpecs();
  }


  @AfterAll
  static void afterAll(TestInfo testInfo) {
    globalAfterAll();
    globalTestMessage(testInfo.getDisplayName(),"class-end");
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
                              .toString(),"method-start");

    Project project1 = projectWithID("C",
                                     "2020-05-05",
                                     "2021-05-05",
                                     1000L,
                                     of("UK", "USA")
                                    ).create();
    Flux<Project> projectFlux = dbUtils.saveProjectList(singletonList(project1));
    dbUtils.countAndExecuteFlux(projectFlux, 1);

    task1 = taskWithID("3",
                       "Mark",
                       1000L
                      ).create();
    Flux<Task> taskFlux = dbUtils.saveTaskList(singletonList(task1));
    dbUtils.countAndExecuteFlux(taskFlux,1);
  }


  @AfterEach
  void tearDown(TestInfo testInfo) {
    globalTestMessage(testInfo.getTestMethod()
                              .toString(),"method-end");
  }


  @Test
  @EnabledIf(expression = enabledTest, loadContext = true)
  @DisplayName("CreateTask")
  void CreateTask() {
    RestAssuredWebTestClient
         .given()
         .webTestClient(mockedWebClient)

         .body(task1)

         .when()
         .post(TASK_CREATE)

         .then()
         .log()
         .everything()

         .statusCode(OK.value())
         .body("_id",containsStringIgnoringCase(task1.get_id()))
         .body("projectId",containsStringIgnoringCase(task1.getProjectId()))
         .body(matchesJsonSchemaInClasspath("contracts/task/task.json"))
    ;
  }

  @Test
  @EnabledIf(expression = enabledTest, loadContext = true)
  @DisplayName("FindAllTask")
  void FindAllTask() {

    RestAssuredWebTestClient
         .given()
         .webTestClient(mockedWebClient)

         .when()
         .get(TASK_FIND_ALL)

         .then()
         .log()
         .everything()

         .statusCode(OK.value())
         .body("size()", is(1))
         .body("$", hasSize(1))
         .body("name", hasItems(task1.getName()))

         .body(matchesJsonSchemaInClasspath("contracts/task/tasks.json"))
    ;
  }
}