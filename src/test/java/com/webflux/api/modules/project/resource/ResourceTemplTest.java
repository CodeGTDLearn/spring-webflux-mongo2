package com.webflux.api.modules.project.resource;

import com.webflux.api.core.config.annotations.ResourceConfig;
import com.webflux.api.core.config.config.DbUtilsConfig;
import com.webflux.api.core.config.databuilders.ProjectChildBuilder;
import com.webflux.api.core.config.testcontainer.compose.TcComposeConfig;
import com.webflux.api.core.config.utils.TestDbUtils;
import com.webflux.api.modules.project.entity.Project;
import com.webflux.api.modules.project.entity.ProjectChild;
import com.webflux.api.modules.project.service.IServiceCrud;
import com.webflux.api.modules.task.entity.Task;
import io.restassured.module.webtestclient.RestAssuredWebTestClient;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.EnabledIf;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.DockerComposeContainer;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static com.webflux.api.core.config.databuilders.ProjectBuilder.projecNoID;
import static com.webflux.api.core.config.databuilders.ProjectBuilder.projectWithID;
import static com.webflux.api.core.config.databuilders.TaskBuilder.taskWithID;
import static com.webflux.api.core.config.utils.RestAssureSpecs.*;
import static com.webflux.api.core.config.utils.TestUtils.*;
import static com.webflux.api.modules.project.core.routes.template.RoutesTempl.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.List.of;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasItems;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@Tags(value = {
     @Tag("replicaset"),
     @Tag("standalone"),
     @Tag("testcontainer")
})
@Import({DbUtilsConfig.class})
@DisplayName("4.0 ResourceTemplTest")
@ResourceConfig
//@ActiveProfiles("test-dev-std")
//@ActiveProfiles("test-dev-tc-comp")
//@TcCompose
public class ResourceTemplTest {

  //@Container
  private static final DockerComposeContainer<?> compose = new TcComposeConfig().getContainer();
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

  private Project project1;
  private List<Project> projectList;


  @BeforeAll
  static void beforeAll(TestInfo testInfo) {

    globalBeforeAll();
    globalTestMessage(testInfo.getDisplayName(), "class-start");

    RestAssuredWebTestClient.reset();
    RestAssuredWebTestClient.requestSpecification =
         requestSpecsSetPath("http://localhost:8080" + TEMPL_ROOT);
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

    // ----- PROJECT TEST ELEMENTS SAVED IN DB ---------------------------------
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

    projectList = asList(project1, project2);
    Flux<Project> projectFlux = dbUtils.saveProjectList(projectList);
    dbUtils.countAndExecuteFlux(projectFlux, 2);

    // ----- TASK TEST ELEMENTS SAVED IN DB ------------------------------------
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

    // ----- PROJECT-CHILD TEST ELEMENTS SAVED IN DB ---------------------------
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
  @DisplayName("FindProjByNameQueryCritTempl")
  public void FindProjByNameQueryCritTempl() {

    RestAssuredWebTestClient

         .given()
         .webTestClient(mockedWebClient)
         .queryParam("projectName", project1.getName())

         .when()
         .get(TEMPL_BYNAME)

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
  @DisplayName("FindByEstCostBetQueryCritTempl")
  public void FindByEstCostBetQueryCritTempl() {

    RestAssuredWebTestClient

         .given()
         .webTestClient(mockedWebClient)
         .queryParam("projectCostFrom", 500)
         .queryParam("projectCostTo", 1500)

         .when()
         .get(TEMPL_EST_COST_BET)

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
  @DisplayName("FindByNameRegexQueryCritTempl")
  public void FindByNameRegexQueryCritTempl() {

    RestAssuredWebTestClient

         .given()
         .webTestClient(mockedWebClient)
         .queryParam("regexpProjectName", project1.getName()
                                                  .substring(0, 3))

         .when()
         .get(TEMPL_BYNAME_REG)

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
  @DisplayName("UpdateCostWithCritTemplUpsert")
  public void UpdateCostWithCritTemplUpsert() {

    RestAssuredWebTestClient.responseSpecification = noContentTypeAndVoidResponses();

    var project4 = projectWithID("B",
                                 "2020-07-07",
                                 "2021-07-07",
                                 3000L,
                                 of("UK", "USA")
                                ).create();
    projectList = of(project4);
    Flux<Project> projectFlux = dbUtils.saveProjectList(projectList);
    dbUtils.countAndExecuteFlux(projectFlux, 1);


    RestAssuredWebTestClient

         .given()
         .webTestClient(mockedWebClient)
         .queryParam("projectId", project4.get_id())
         .queryParam("projectCost", 5000)
         .body(project4)

         .when()
         .put(TEMPL_UPSERT_CRIT)

         .then()
         .log()
         .everything()

         .statusCode(OK.value())
    ;

    StepVerifier
         .create(serviceCrud.findById(project4.get_id()))
         .expectSubscription()
         .expectNextMatches(proj -> proj.getEstimatedCost() == 5000L)
         .verifyComplete();


    StepVerifier
         .create(serviceCrud.findById(project4.get_id()))
         .expectSubscription()
         .expectNextMatches(project -> project.get_id()
                                              .equals(project4.get_id()))
         .verifyComplete();
  }

  @Test
  @EnabledIf(expression = enabledTest, loadContext = true)
  @DisplayName("DeleteCritTempl")
  public void DeleteCritTempl() {

    RestAssuredWebTestClient.responseSpecification = noContentTypeAndVoidResponses();

    dbUtils.countAndExecuteFlux(serviceCrud.findAll(), 2);

    RestAssuredWebTestClient

         .given()
         .webTestClient(mockedWebClient)
         .queryParam("projectId", project1.get_id())

         .when()
         .delete(TEMPL_DEL_CRIT)

         .then()
         .log()
         .everything()

         .statusCode(NO_CONTENT.value())
    ;

    dbUtils.countAndExecuteFlux(serviceCrud.findAll(), 1);
  }

}