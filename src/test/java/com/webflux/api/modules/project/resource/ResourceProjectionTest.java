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
import static com.webflux.api.core.config.utils.TestUtils.TestTitlesClass.*;
import static com.webflux.api.modules.project.core.routes.template.RoutesProjection.TEMPL_PROJECTION;
import static com.webflux.api.modules.project.core.routes.template.RoutesProjection.TEMPL_ROOT_PROJECTION;
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
@DisplayName("4.5 ResourceProjectionTest")
@ResourceConfig
@TcCompose
public class ResourceProjectionTest {

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


  @BeforeAll
  static void beforeAll(TestInfo testInfo) {

    globalBeforeAll();
    globalTestMessage(testInfo.getDisplayName(), TestTitlesClass.CLASS_START);

    RestAssuredWebTestClient.reset();
    RestAssuredWebTestClient.requestSpecification =
         requestSpecsSetPath("http://localhost:8080" + TEMPL_ROOT_PROJECTION);
    RestAssuredWebTestClient.responseSpecification = responseSpecs();
  }


  @AfterAll
  static void afterAll(TestInfo testInfo) {

    globalAfterAll();
    globalTestMessage(testInfo.getDisplayName(), CLASS_END);
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
                              .toString(), METHOD_START);

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
                              .toString(), METHOD_END);
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

    Task taskLookup1 = taskWithID("3",
                                  "Mark",
                                  1000L
    ).create();
    taskLookup1.setProjectId(projectLookup1.get_id());

    Task taskLookup2 = taskWithID("3",
                                  "Mark",
                                  1000L
    ).create();
    taskLookup2.setProjectId(projectLookup2.get_id());

    Flux<Task> taskFlux = dbUtils.saveTaskList(asList(taskLookup1, taskLookup2));
    dbUtils.countAndExecuteFlux(taskFlux, 2);
  }


}