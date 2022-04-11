package com.webflux.api.modules.project.resource;

import com.webflux.api.core.config.profiles.ProfileGeneral;
import com.webflux.api.core.config.annotations.ResourceConfig;
import com.webflux.api.core.config.config.DbUtilsConfig;
import com.webflux.api.core.config.databuilders.ProjectChildBuilder;
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
import static com.webflux.api.core.config.databuilders.TaskBuilder.taskWithID;
import static com.webflux.api.core.config.utils.RestAssureSpecs.requestSpecsSetPath;
import static com.webflux.api.core.config.utils.RestAssureSpecs.responseSpecs;
import static com.webflux.api.core.config.utils.TestUtils.*;
import static com.webflux.api.modules.project.core.routes.template.RoutesAggreg.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.List.of;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.http.HttpStatus.OK;

@Tags(value = {
     @Tag("replicaset"),
     @Tag("standalone"),
     @Tag("testcontainer")
})
@Import({DbUtilsConfig.class})
@DisplayName("4.1 ResourceAggregTest")
@ResourceConfig
@ProfileGeneral
@TcCompose
public class ResourceAggregTest {

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

  private Project project2;


  @BeforeAll
  static void beforeAll(TestInfo testInfo) {

    globalBeforeAll();
    globalTestMessage(testInfo.getDisplayName(), "class-start");
    RestAssuredWebTestClient.reset();
    RestAssuredWebTestClient.requestSpecification =
         requestSpecsSetPath("http://localhost:8080" + TEMPL_ROOT_AGGREG);
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

    Project project1 = projecNoID("C",
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

    List<Project> projectList = asList(project1, project2);
    Flux<Project> projectFlux = dbUtils.saveProjectList(projectList);

    dbUtils.countAndExecuteFlux(projectFlux, 2);

    Task task1 = taskWithID("3",
                            "Mark",
                            1000L
                           ).create();
    Task task2 = taskWithID("4",
                            "Mark Zuck",
                            7000L
                           ).create();
    Flux<Task> taskFlux = dbUtils.saveTaskList(singletonList(task1));

    dbUtils.countAndExecuteFlux(taskFlux, 1);

    ProjectChild project1Child = ProjectChildBuilder.projectChildWithID("D",
                                                                        "2022-07-07",
                                                                        "2023-07-07",
                                                                        4000L,
                                                                        Arrays.asList(task1, task2)
                                                                       )
                                                    .create();
    List<ProjectChild> projectChildList = List.of(project1Child);
    Flux<ProjectChild> projectChildFlux = dbUtils.saveProjectChildList(projectChildList);
    dbUtils.countAndExecuteFlux(projectChildFlux, 1);

  }


  @AfterEach
  void tearDown(TestInfo testInfo) {

    globalTestMessage(testInfo.getTestMethod()
                              .toString(), "method-end");
  }


  @Test
  @EnabledIf(expression = enabledTest, loadContext = true)
  @DisplayName("findNoOfProjectsCostGreaterThan")
  public void findNoOfProjectsCostGreaterThan() {

    RestAssuredWebTestClient

         .given()
         .webTestClient(mockedWebClient)
         .queryParam("projectCost", 1000)

         .when()
         .get(TEMPL_AGGREG_NO_GT)

         .then()
         .log()
         .everything()

         .statusCode(OK.value())
         .body("costly_projects", hasItem(1))
         .body(matchesJsonSchemaInClasspath("contracts/aggregations/NoOfProjectsCostGT.json"))
    ;

  }

  @Test
  @EnabledIf(expression = enabledTest, loadContext = true)
  @DisplayName("findCostsGroupByStartDateForProjectsCostGreaterThan")
  public void findCostsGroupByStartDateForProjectsCostGreaterThan() {

    RestAssuredWebTestClient

         .given()
         .webTestClient(mockedWebClient)
         .queryParam("projectCost", 1000)

         .when()
         .get(TEMPL_AGGREG_DATE)

         .then()
         .log()
         .everything()

         .statusCode(OK.value())
         .body("_id", hasItem(project2.getStartDate()))
         .body("total", hasItem((int) project2.getEstimatedCost()))
         .body(matchesJsonSchemaInClasspath("contracts/aggregations/CostsGroupByStartDate.json"))
    ;

  }


}