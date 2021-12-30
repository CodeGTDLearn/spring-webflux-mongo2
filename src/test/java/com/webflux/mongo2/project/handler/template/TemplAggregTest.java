package com.webflux.mongo2.project.handler.template;

import com.webflux.mongo2.core.TestDbUtilsConfig;
import com.webflux.mongo2.project.entity.Project;
import com.webflux.mongo2.project.entity.ProjectChild;
import com.webflux.mongo2.project.service.IServiceCrud;
import com.webflux.mongo2.task.Task;
import config.annotations.MergedResource;
import config.databuilders.ProjectChildBuilder;
import config.testcontainer.TcComposeConfig;
import config.utils.TestDbUtils;
import io.restassured.module.webtestclient.RestAssuredWebTestClient;
import org.hamcrest.Matchers;
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

import static com.webflux.mongo2.config.routes.project.RoutesTemplAggreg.TEMPL_AGGREG_DATE;
import static com.webflux.mongo2.config.routes.project.RoutesTemplAggreg.TEMPL_AGGREG_NO_GT;
import static config.databuilders.ProjectBuilder.projectWithID;
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
@DisplayName("TemplTest")
@MergedResource
class TemplAggregTest {

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
  IServiceCrud serviceCrud;

  private Project project1, project2, project3;
  private Task task1, task2;
  private ProjectChild project1Child;
  private List<Project> projectList;
  private List<ProjectChild> projectChildList;


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

    project1 = projectWithID("C",
                             "2020-05-05",
                             "2021-05-05",
                             1000L,
                             of("UK", "USA")
                            ).create();

    project2 = projectWithID("B",
                             "2020-06-06",
                             "2021-06-06",
                             2000L,
                             of("UK", "USA")
                            ).create();

    project3 = projectWithID("B",
                             "2020-07-07",
                             "2021-07-07",
                             3000L,
                             of("UK", "USA")
                            ).create();

    projectList = asList(project1, project2);
    Flux<Project> projectFlux = dbUtils.saveProjectList(projectList);

    dbUtils.countAndExecuteFlux(projectFlux, 2);

    task1 = taskWithID("3",
                       "Mark",
                       1000L
                      ).create();
    task2 = taskWithID("4",
                       "Mark Zuck",
                       7000L
                      ).create();
    Flux<Task> taskFlux = dbUtils.saveTaskList(singletonList(task1));

    dbUtils.countAndExecuteFlux(taskFlux, 1);

    project1Child = ProjectChildBuilder.projectChildWithID("D",
                                                           "2022-07-07",
                                                           "2023-07-07",
                                                           4000L,
                                                           Arrays.asList(task1, task2)
                                                          )
                                       .create();
    projectChildList = List.of(project1Child);
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
         .queryParam("cost", 1000)

         .when()
         .get(TEMPL_AGGREG_NO_GT)

         .then()
         .log()
         .everything()

         .statusCode(OK.value())
         .body("." , Matchers.containsString("1"))
    ;

  }

  @Test
  @EnabledIf(expression = enabledTest, loadContext = true)
  @DisplayName("findCostsGroupByStartDateForProjectsCostGreaterThan")
  public void findCostsGroupByStartDateForProjectsCostGreaterThan() {

    RestAssuredWebTestClient

         .given()
         .webTestClient(mockedWebClient)
         .queryParam("cost", 1000)

         .when()
         .get(TEMPL_AGGREG_DATE)

         .then()
         .log()
         .everything()
/* ERRO:
JSON path total doesn't match.
Expected: 2000f
  Actual: <[2000]>
 */
         .statusCode(OK.value())
         .body("total", equalTo(project2.getEstimatedCost()))
         .body(matchesJsonSchemaInClasspath("contracts/aggregations/CostsGroupByStartDate.json"))
    ;

  }


}