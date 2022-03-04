package com.webflux.api.modules.project.resource;

import com.webflux.api.core.config.annotations.MergedResourceTcompose;
import com.webflux.api.core.config.testcontainer.compose.TcComposeConfig;
import com.webflux.api.core.config.utils.TestDbUtils;
import com.webflux.api.core.config.testconfigs.TestDbUtilsConfig;
import com.webflux.api.modules.project.entity.Project;
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

import java.util.List;

import static com.webflux.api.core.config.databuilders.ProjectBuilder.projecNoID;
import static com.webflux.api.core.config.databuilders.TaskBuilder.taskWithID;
import static com.webflux.api.core.config.utils.BlockhoundUtils.blockHoundTestCheck;
import static com.webflux.api.core.config.utils.RestAssureSpecs.requestSpecsSetPath;
import static com.webflux.api.core.config.utils.RestAssureSpecs.responseSpecs;
import static com.webflux.api.core.config.utils.TestUtils.*;
import static com.webflux.api.modules.project.core.routes.RoutesRepo.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.List.of;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.HttpStatus.OK;

@Import({TestDbUtilsConfig.class})
@DisplayName("ResourceRepoTest")
@MergedResourceTcompose
class ResourceRepoTest {

  // STATIC-@Container: one service for ALL tests -> SUPER FASTER
  // NON-STATIC-@Container: one service for EACH test
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

  private Project project1;
  private Project project2;


  @BeforeAll
  static void beforeAll(TestInfo testInfo) {

//    globalComposeServiceContainerMessage(compose,
//                                         compose.get,
//                                         TC_COMPOSE_SERVICE_PORT
//                                        );
    globalBeforeAll();
    globalTestMessage(testInfo.getDisplayName(), "class-start");
    RestAssuredWebTestClient.reset();
    RestAssuredWebTestClient.requestSpecification =
         requestSpecsSetPath("http://localhost:8080" + REPO_ROOT);
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

    List<Project> projectList = asList(project1, project2);
    Flux<Project> projectFlux = dbUtils.saveProjectList(projectList);

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
  @EnabledIf(expression = enabledTest, loadContext = true)
  @DisplayName("BHWorks")
  public void bHWorks() {

    blockHoundTestCheck();
  }

  @Test
  @EnabledIf(expression = enabledTest, loadContext = true)
  @DisplayName("FindByNameNot")
  public void FindByNameNot() {

    RestAssuredWebTestClient

         .given()
         .webTestClient(mockedWebClient)
         .queryParam("projectName", project1.getName())

         .when()
         .get(REPO_BYNAME_NOT)

         .then()
         .log()
         .everything()

         .statusCode(OK.value())
         .body("name", containsInAnyOrder(project2.getName()))
         .body("countryList[0]", hasItems(
              project2.getCountryList()
                      .get(0)
              , project2.getCountryList()
                        .get(1)))
         .body(matchesJsonSchemaInClasspath("contracts/project/project.json"))
    ;
  }

  @Test
  @EnabledIf(expression = enabledTest, loadContext = true)
  @DisplayName("FindByEstimCostGreatThan")
  public void FindByEstimCostGreatThan() {

    RestAssuredWebTestClient

         .given()
         .webTestClient(mockedWebClient)
         .queryParam("projectCost", project2.getEstimatedCost() - 100)

         .when()
         .get(REPO_COST_GREATER)

         .then()
         .log()
         .everything()

         .statusCode(OK.value())
         .body("name", containsInAnyOrder(project2.getName()))
         .body("countryList[0]", hasItems(
              project2.getCountryList()
                      .get(0)
              , project2.getCountryList()
                        .get(1)))
         .body(matchesJsonSchemaInClasspath("contracts/project/project.json"))
    ;
  }

  @Test
  @EnabledIf(expression = enabledTest, loadContext = true)
  @DisplayName("FindByEstimatedCostBetween")
  public void FindByEstimatedCostBetween() {

    RestAssuredWebTestClient

         .given()
         .webTestClient(mockedWebClient)
         .queryParam("projectCostFrom", 500)
         .queryParam("projectCostTo", 1200)

         .when()
         .get(REPO_COST_BETW)

         .then()
         .log()
         .everything()

         .statusCode(OK.value())
         .body("name", containsInAnyOrder(project1.getName()))
         .body("countryList[0]", hasItems(
              project1.getCountryList()
                      .get(0)
              , project1.getCountryList()
                        .get(1)))
         .body(matchesJsonSchemaInClasspath("contracts/project/project.json"))
    ;
  }

  @Test
  @EnabledIf(expression = enabledTest, loadContext = true)
  @DisplayName("FindByEstimatedCostBetween")
  public void FindByEstimatedCostBetweenList() {

    RestAssuredWebTestClient

         .given()
         .webTestClient(mockedWebClient)
         .queryParam("projectCostFrom", 500)
         .queryParam("projectCostTo", 2100)

         .when()
         .get(REPO_COST_BETW)

         .then()
         .log()
         .everything()

         .statusCode(OK.value())
         .body("name", hasItems(project1.getName(), project2.getName()))
         .body(matchesJsonSchemaInClasspath("contracts/project/projects.json"))
    ;
  }

  @Test
  @EnabledIf(expression = enabledTest, loadContext = true)
  @DisplayName("FindByNameLike")
  public void FindByNameLike() {

    RestAssuredWebTestClient

         .given()
         .webTestClient(mockedWebClient)
         .queryParam("projectName", project1.getName()
                                            .substring(0, 3))

         .when()
         .get(REPO_BYNAME_LIKE)

         .then()
         .log()
         .everything()

         .statusCode(OK.value())
         .body("name", containsInAnyOrder(project1.getName()))
         .body("countryList[0]", hasItems(
              project1.getCountryList()
                      .get(0)
              , project1.getCountryList()
                        .get(1)))
         .body(matchesJsonSchemaInClasspath("contracts/project/project.json"))
    ;
  }

  @Test
  @EnabledIf(expression = enabledTest, loadContext = true)
  @DisplayName("FindByNameRegex")
  public void FindByNameRegex() {

    RestAssuredWebTestClient

         .given()
         .webTestClient(mockedWebClient)
         .queryParam("projectName", project1.getName()
                                            .substring(0, 3))

         .when()
         .get(REPO_BYNAME_REGEX)

         .then()
         .log()
         .everything()

         .statusCode(OK.value())
         .body("name", containsInAnyOrder(project1.getName()))
         .body("countryList[0]", hasItems(
              project1.getCountryList()
                      .get(0)
              , project1.getCountryList()
                        .get(1)))
         .body(matchesJsonSchemaInClasspath("contracts/project/project.json"))
    ;
  }

  @Test
  @EnabledIf(expression = enabledTest, loadContext = true)
  @DisplayName("FindByNameQuery")
  public void FindByNameQuery() {

    RestAssuredWebTestClient

         .given()
         .webTestClient(mockedWebClient)
         .queryParam("projectName", project1.getName())

         .when()
         .get(REPO_QUERY_BYNAME)

         .then()
         .log()
         .everything()

         .statusCode(OK.value())
         .body("name", containsInAnyOrder(project1.getName()))
         .body("countryList[0]", hasItems(
              project1.getCountryList()
                      .get(0)
              , project1.getCountryList()
                        .get(1)))
         .body(matchesJsonSchemaInClasspath("contracts/project/project.json"))
    ;
  }

  @Test
  @EnabledIf(expression = enabledTest, loadContext = true)
  @DisplayName("FindByNameAndCostQuery")
  public void FindByNameAndCostQuery() {

    RestAssuredWebTestClient

         .given()
         .webTestClient(mockedWebClient)
         .queryParam("projectName", project1.getName())
         .queryParam("projectCost", project1.getEstimatedCost())

         .when()
         .get(REPO_QUERY_BYNAME_COST)

         .then()
         .log()
         .everything()

         .statusCode(OK.value())
         .body("name", containsInAnyOrder(project1.getName()))
         .body("countryList[0]", hasItems(
              project1.getCountryList()
                      .get(0)
              , project1.getCountryList()
                        .get(1)))
         .body(matchesJsonSchemaInClasspath("contracts/project/project.json"))
    ;
  }

  @Test
  @EnabledIf(expression = enabledTest, loadContext = true)
  @DisplayName("FindByEstCostBetwQuery")
  public void FindByEstCostBetwQuery() {

    RestAssuredWebTestClient

         .given()
         .webTestClient(mockedWebClient)
         .queryParam("projectCostFrom", 500)
         .queryParam("projectCostTo", 2200)

         .when()
         .get(REPO_QUERY_EST_COST_BET)

         .then()
         .log()
         .everything()

         .statusCode(OK.value())
         .body("name", hasItems(project2.getName(), project1.getName()))
         .body("estimatedCost[0]", equalTo((int) project2.getEstimatedCost()))
         .body("estimatedCost[1]", equalTo((int) project1.getEstimatedCost()))

         .body(matchesJsonSchemaInClasspath("contracts/project/projects.json"))
    ;
  }

  @Test
  @EnabledIf(expression = enabledTest, loadContext = true)
  @DisplayName("FindByNameRegexQuery")
  public void FindByNameRegexQuery() {

    RestAssuredWebTestClient

         .given()
         .webTestClient(mockedWebClient)
         .queryParam("regexpProjectName", project1.getName()
                                                  .substring(0, 3))

         .when()
         .get(REPO_QUERY_NYNAME_REGEX)

         .then()
         .log()
         .everything()

         .statusCode(OK.value())
         .body("name", containsInAnyOrder(project1.getName()))
         .body("estimatedCost", hasItem((int) project1.getEstimatedCost()))

         .body(matchesJsonSchemaInClasspath("contracts/project/projectOnlyNameAndCost.json"))
    ;
  }
}