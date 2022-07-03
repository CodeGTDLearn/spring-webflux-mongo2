package com.webflux.api.modules.project.resource;

import com.webflux.api.core.config.annotations.ResourceConfig;
import com.webflux.api.core.config.config.DbUtilsConfig;
import com.webflux.api.core.config.testcontainer.compose.TcCompose;
import com.webflux.api.core.config.testcontainer.compose.TcComposeConfig;
import com.webflux.api.core.config.utils.TestDbUtils;
import com.webflux.api.modules.project.entity.Project;
import com.webflux.api.modules.project.entity.ProjectChild;
import com.webflux.api.modules.task.entity.Task;
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

import static com.webflux.api.core.config.databuilders.ProjectBuilder.projecNoID;
import static com.webflux.api.core.config.databuilders.ProjectBuilder.projectWithID;
import static com.webflux.api.core.config.databuilders.ProjectChildBuilder.projectChildWithID;
import static com.webflux.api.core.config.databuilders.TaskBuilder.taskWithID;
import static com.webflux.api.core.config.utils.RestAssureSpecs.requestSpecsSetPath;
import static com.webflux.api.core.config.utils.RestAssureSpecs.responseSpecs;
import static com.webflux.api.core.config.utils.TestUtils.*;
import static com.webflux.api.modules.project.core.routes.template.RoutesLookupProjection.TEMPL_LOOKUP_PROJ;
import static com.webflux.api.modules.project.core.routes.template.RoutesLookupProjection.TEMPL_ROOT_LOOKUP;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.List.of;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpStatus.OK;

@Tags(value = {
     @Tag("replicaset"),
     @Tag("standalone"),
     @Tag("testcontainer")
})
@Import({DbUtilsConfig.class})
@DisplayName("4.4 ResourceLookupProjectionTest")
@ResourceConfig
@TcCompose
public class ResourceLookupProjectionTest {

  @Container
  private static final DockerComposeContainer<?> compose = new TcComposeConfig().getContainer();
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

    RestAssuredWebTestClient.reset();
    RestAssuredWebTestClient.requestSpecification =
         requestSpecsSetPath("http://localhost:8080" + TEMPL_ROOT_LOOKUP);
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
  @DisplayName("findAllProjectTasks")
  public void findAllProjectTasks() {

    createTwoTaskProjects();

    RestAssuredWebTestClient

         .given()
         .webTestClient(mockedWebClient)

         .when()
         .get(TEMPL_LOOKUP_PROJ)

         .then()
         .log()
         .everything()

         .statusCode(OK.value())
         .body("[0]._id", equalTo(projectLookup1.get_id()))
         .body("[0].name", equalTo(projectLookup1.getName()))
         .body("[0].taskName", equalTo(taskLookup1.getName()))
         .body("[1]._id", equalTo(projectLookup2.get_id()))
         .body("[1].name", equalTo(projectLookup2.getName()))
         .body("[1].taskName", equalTo(taskLookup2.getName()))
         .body(matchesJsonSchemaInClasspath("contracts/lookupProjection/taskProjects.json"))
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