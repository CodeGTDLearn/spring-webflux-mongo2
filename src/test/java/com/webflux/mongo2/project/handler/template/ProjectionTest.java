package com.webflux.mongo2.project.handler.template;

import com.webflux.mongo2.core.TestDbUtilsConfig;
import com.webflux.mongo2.project.entity.Project;
import com.webflux.mongo2.project.entity.ProjectChild;
import com.webflux.mongo2.task.Task;
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
import java.util.List;

import static com.webflux.mongo2.core.routes.project.template.RoutesProjection.TEMPL_PROJECTION;
import static config.databuilders.ProjectBuilder.projecNoID;
import static config.databuilders.ProjectBuilder.projectWithID;
import static config.databuilders.ProjectChildBuilder.projectChildWithID;
import static config.databuilders.TaskBuilder.taskWithID;
import static config.testcontainer.TcComposeConfig.TC_COMPOSE_SERVICE;
import static config.testcontainer.TcComposeConfig.TC_COMPOSE_SERVICE_PORT;
import static config.utils.RestAssureSpecs.requestSpecsSetPath;
import static config.utils.RestAssureSpecs.responseSpecs;
import static config.utils.TestUtils.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.List.of;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpStatus.OK;

@Import({TestDbUtilsConfig.class})
@DisplayName("ProjectionTest")
@MergedResource
class ProjectionTest {

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

  private Project project1, project2;
  private Task task1, task2;
  private ProjectChild project1Child;
  private Project projectLookup1, projectLookup2;
  private Task taskLookup1, taskLookup2;


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
         requestSpecsSetPath("http://localhost:8080/");
    RestAssuredWebTestClient.responseSpecification = responseSpecs();
  }


  @AfterAll
  static void afterAll(TestInfo testInfo) {

    globalAfterAll();
    globalTestMessage(testInfo.getDisplayName(), "class-end");
  }


  @BeforeEach
  void beforeEach(TestInfo testInfo) {

    // REAL-SERVER INJECTED IN WEB-TEST-CLIENT(non-blocking client)'
    // SHOULD BE USED WHEN 'DOCKER-COMPOSE' UP A REAL-WEB-SERVER
    // BECAUSE THERE IS 'REAL-SERVER' CREATED VIA DOCKER-COMPOSE
    // realWebClient = WebTestClient.bindToServer()
    //                      .baseUrl("http://localhost:8080/customer")
    //                      .build();

    globalTestMessage(testInfo.getTestMethod()
                              .toString(), "method-start");

    createThreeSimpleProjects();
    List<Project> projectList = asList(project1, project2);
    Flux<Project> projectFlux = dbUtils.saveProjectList(projectList);
    dbUtils.countAndExecuteFlux(projectFlux, 2);

    createTwoSimpleTasks();
    Flux<Task> taskFlux = dbUtils.saveTaskList(singletonList(task1));
    dbUtils.countAndExecuteFlux(taskFlux, 1);

    createOneProjectChild();
    Flux<ProjectChild> projectChildFlux = dbUtils.saveProjectChildList(
         singletonList(project1Child));
    dbUtils.countAndExecuteFlux(projectChildFlux, 1);
  }

  @AfterEach
  void tearDown(TestInfo testInfo) {

    globalTestMessage(testInfo.getTestMethod()
                              .toString(), "method-end");
  }


  @Test
  @EnabledIf(expression = enabledTest, loadContext = true)
  @DisplayName("findAllProjectDto")
  public void findAllProjectDto() {

    createTwoTaskProjects();

    RestAssuredWebTestClient

         .given()
         .webTestClient(mockedWebClient)

         .when()
         .get(TEMPL_PROJECTION)

         .then()
         .log()
         .everything()

         .statusCode(OK.value())
         .body("[0].name", equalTo(projectLookup1.getName()))
         .body("[0].description", equalTo(projectLookup1.getDescription()))
         .body("[1].name", equalTo(projectLookup2.getName()))
         .body("[1].description", equalTo(projectLookup2.getDescription()))
         .body(matchesJsonSchemaInClasspath("contracts/projection/projection.json"))
    ;

  }


  private void createOneProjectChild() {

    project1Child = projectChildWithID("D",
                                       "2022-07-07",
                                       "2023-07-07",
                                       4000L,
                                       Arrays.asList(task1, task2)
                                      ).create();
  }

  private void createTwoSimpleTasks() {

    task1 = taskWithID("3",
                       "Mark",
                       1000L
                      ).create();

    task2 = taskWithID("4",
                       "Mark Zuck",
                       7000L
                      ).create();
  }

  private void createThreeSimpleProjects() {

    project1 = projecNoID("C",
                          "2020-05-05",
                          "2021-05-05",
                          1000L,
                          of("UK", "USA")
                         ).create();

    project2 = projecNoID("B",
                          "2020-06-06",
                          "2021-06-06",
                          2000L,
                          of("UK", "USA")
                         ).create();

    Project project3 = projecNoID("B",
                                  "2020-07-07",
                                  "2021-07-07",
                                  3000L,
                                  of("UK", "USA")
                                 ).create();
  }

  private void createTwoTaskProjects() {

    projectLookup1 =
         projectWithID("C",
                       "2020-05-05",
                       "2021-05-05",
                       1000L,
                       of("UK", "USA")
                      ).create();

    projectLookup2 =
         projectWithID("C",
                       "2020-05-05",
                       "2021-05-05",
                       1000L,
                       of("UK", "CAN")
                      ).create();

    Flux<Project> projectFlux = dbUtils.saveProjectList(asList(projectLookup1, projectLookup2));
    dbUtils.countAndExecuteFlux(projectFlux, 2);

    taskLookup1 = taskWithID("3",
                             "Mark",
                             1000L
                            ).create();
    taskLookup1.setProjectId(projectLookup1.get_id());

    taskLookup2 = taskWithID("3",
                             "Mark",
                             1000L
                            ).create();
    taskLookup2.setProjectId(projectLookup2.get_id());

    Flux<Task> taskFlux = dbUtils.saveTaskList(asList(taskLookup1, taskLookup2));
    dbUtils.countAndExecuteFlux(taskFlux, 2);
  }


}