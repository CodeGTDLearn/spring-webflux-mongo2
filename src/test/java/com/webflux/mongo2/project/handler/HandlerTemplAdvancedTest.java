package com.webflux.mongo2.project.handler;

import com.github.javafaker.Faker;
import com.webflux.mongo2.core.TestDbUtilsConfig;
import com.webflux.mongo2.project.Project;
import com.webflux.mongo2.project.ProjectChild;
import com.webflux.mongo2.project.service.IServiceCrud;
import com.webflux.mongo2.task.Task;
import com.webflux.mongo2.task.service.IServiceTask;
import config.annotations.MergedResource;
import config.databuilders.ProjectChildBuilder;
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

import static com.webflux.mongo2.config.routes.project.RoutesTempl.*;
import static config.databuilders.ProjectBuilder.projectWithID;
import static config.databuilders.TaskBuilder.taskWithID;
import static config.testcontainer.TcComposeConfig.TC_COMPOSE_SERVICE;
import static config.testcontainer.TcComposeConfig.TC_COMPOSE_SERVICE_PORT;
import static config.utils.RestAssureSpecs.*;
import static config.utils.TestUtils.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.List.of;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.HttpStatus.OK;

@Import({TestDbUtilsConfig.class})
@DisplayName("HandlerTemplAdvancedTest")
@MergedResource
class HandlerTemplAdvancedTest {

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

  @Autowired
  IServiceTask serviceTask;

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
  @DisplayName("AddCritTemplArray")
  public void AddCritTemplArray() {

    var countryAdded = "BR";

    RestAssuredWebTestClient

         .given()
         .webTestClient(mockedWebClient)
         .queryParam("id", project1.get_id())
         .queryParam("country", countryAdded)
         .body(project1)

         .when()
         .put(TEMPL_ADD_ARRAY_CRIT)

         .then()
         .log()
         .everything()

         .statusCode(OK.value())
         .body("_id", containsStringIgnoringCase(project1.get_id()))
         .body("countryList", hasItems(
              project1.getCountryList()
                      .get(0)
              , project1.getCountryList()
                        .get(1)
              , countryAdded))
         .body(matchesJsonSchemaInClasspath("contracts/project/updateChild.json"))
    ;
  }

  @Test
  @EnabledIf(expression = enabledTest, loadContext = true)
  @DisplayName("UpdateCritTemplArray")
  public void UpdateCritTemplArray() {


    var previousCountry = project1.getCountryList()
                                  .get(0);
    var newCountry = Faker.instance()
                          .country()
                          .countryCode2()
                          .toUpperCase();

    RestAssuredWebTestClient

         .given()
         .webTestClient(mockedWebClient)
         .queryParam("id", project1.get_id())
         .queryParam("country", previousCountry)
         .queryParam("newcountry", newCountry)
         .body(project1)

         .when()
         .put(TEMPL_UPD_ARRAY_CRIT)

         .then()
         .log()
         .everything()

         .statusCode(OK.value())
         .body("_id", containsStringIgnoringCase(project1.get_id()))
         .body("countryList", hasItems(
              newCountry
              , project1.getCountryList()
                        .get(1)))
         .body(matchesJsonSchemaInClasspath("contracts/project/updateChild.json"))
    ;
  }

  @Test
  @EnabledIf(expression = enabledTest, loadContext = true)
  @DisplayName("DeleteCritTemplArray")
  public void DeleteCritTemplArray() {

    RestAssuredWebTestClient

         .given()
         .webTestClient(mockedWebClient)
         .queryParam("id", project1.get_id())
         .queryParam("country", project1.getCountryList()
                                        .get(1))
         .body(project1)

         .when()
         .put(TEMPL_DEL_ARRAY_CRIT)

         .then()
         .log()
         .everything()

         .statusCode(OK.value())
         .body("_id", containsStringIgnoringCase(project1.get_id()))
         .body("countryList", hasItems(
              project1.getCountryList()
                      .get(0)))
         .body(matchesJsonSchemaInClasspath("contracts/project/updateChild.json"))
    ;
  }

  @Test
  @EnabledIf(expression = enabledTest, loadContext = true)
  @DisplayName("AddCritTemplChild")
  public void AddCritTemplChild() {

    var taskToAdd = taskWithID("4444",
                               "Mark ZuckLoki",
                               7000L
                              ).create();

    RestAssuredWebTestClient

         .given()
         .webTestClient(mockedWebClient)

         .queryParam("id", project1Child.get_id())
         .body(taskToAdd)

         .when()
         .put(TEMPL_ADD_CHILD_CRIT)

         .then()
         .log()
         .everything()

         .statusCode(OK.value())
         .body("_id", containsStringIgnoringCase(project1Child.get_id()))
         .body("tasks._id", hasItem(taskToAdd.get_id()))
         .body(matchesJsonSchemaInClasspath("contracts/project/updateChild.json"))
    ;
  }

  @Test
  @EnabledIf(expression = enabledTest, loadContext = true)
  @DisplayName("UpdateCritTemplChild")
  public void UpdateCritTemplChild() {

    var ownername = "Pauleta";

    RestAssuredWebTestClient

         .given()
         .webTestClient(mockedWebClient)
         .queryParam("id", project1Child.get_id())
         .queryParam("idch", project1Child.getTasks()
                                          .get(0)
                                          .get_id())
         .queryParam("ownername", ownername)
         .body(project1Child)

         .when()
         .put(TEMPL_UPD_CHILD_CRIT)

         .then()
         .log()
         .everything()

         .statusCode(OK.value())
         .body("_id", containsStringIgnoringCase(project1Child.get_id()))
         .body("tasks[0].ownername", containsStringIgnoringCase(ownername))
         .body(matchesJsonSchemaInClasspath("contracts/project/updateChild.json"))
    ;
  }

  @Test
  @EnabledIf(expression = enabledTest, loadContext = true)
  @DisplayName("DeleteCritTemplChild")
  public void DeleteCritTemplChild() {

    dbUtils.countAndExecuteFlux(serviceCrud.findAll(), 2);

    RestAssuredWebTestClient

         .given()
         .webTestClient(mockedWebClient)
         .queryParam("id", project1Child.get_id())
         .queryParam("idch", project1Child.getTasks()
                                          .get(0)
                                          .get_id())
         .body(project1Child)

         .when()
         .delete(TEMPL_DEL_CHILD_CRIT)

         .then()
         .log()
         .everything()

         .statusCode(OK.value())
    //         .body("_id", containsStringIgnoringCase(project1Child.get_id()))
    //         .body("countryList", hasItems(
    //              project1.getCountryList()
    //                      .get(0)
    //              , project1.getCountryList()
    //                        .get(1)
    //              , ownername))
    //         .body(matchesJsonSchemaInClasspath("contracts/project/updateChild.json"))
    ;

    dbUtils.countAndExecuteFlux(serviceCrud.findAll(), 2);
  }

  @Test
  @EnabledIf(expression = enabledTest, loadContext = true)
  @DisplayName("DeleteCritTemplMultCollections")
  public void DeleteCritTemplMultCollections() {

    RestAssuredWebTestClient.responseSpecification = responseSpecNoContentType();

    dbUtils.countAndExecuteFlux(serviceTask.findAll(), 1);

    RestAssuredWebTestClient

         .given()
         .webTestClient(mockedWebClient)
         .queryParam("idProject", project1Child.get_id())
         .queryParam("idTask", task1.get_id())

         .when()
         .delete(TEMPL_DEL_CRIT_MULT_COL)

         .then()
         .log()
         .everything()

         .statusCode(OK.value())
    ;

    dbUtils.countAndExecuteFlux(serviceTask.findAll(), 0);
  }

  @Test
  @EnabledIf(expression = enabledTest, loadContext = true)
  @DisplayName("deleteAllColletionsTemplate")
  public void deleteAllColletionsTemplate() {

    RestAssuredWebTestClient.responseSpecification = responseSpecNoContentType();

    RestAssuredWebTestClient

         .given()
         .webTestClient(mockedWebClient)

         .when()
         .delete(TEMPL_CLEAN_DB_CRIT_COL)

         .then()
         .log()
         .everything()

         .statusCode(OK.value())
    ;
  }

  @Test
  @EnabledIf(expression = enabledTest, loadContext = true)
  @DisplayName("checkCollectionsTemplate")
  public void checkCollectionsTemplate() {

    RestAssuredWebTestClient

         .given()
         .webTestClient(mockedWebClient)

         .when()
         .delete(TEMPL_CHECK_DB_CRIT_COL)

         .then()
         .log()
         .everything()

         .statusCode(OK.value())
    ;
  }
}