package com.webflux.mongo2.project.handler;

import com.webflux.mongo2.core.TestDbUtilsConfig;
import com.webflux.mongo2.project.Project;
import com.webflux.mongo2.project.service.IServiceCrud;
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

import java.util.List;

import static com.webflux.mongo2.config.routes.project.RoutesCrud.*;
import static config.databuilders.ProjectBuilder.projectWithID;
import static config.databuilders.TaskBuilder.taskWithID;
import static config.testcontainer.TcComposeConfig.TC_COMPOSE_SERVICE;
import static config.testcontainer.TcComposeConfig.TC_COMPOSE_SERVICE_PORT;
import static config.utils.BlockhoundUtils.bhWorks;
import static config.utils.RestAssureSpecs.*;
import static config.utils.TestUtils.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.HttpStatus.OK;

@Import({TestDbUtilsConfig.class})
@DisplayName("HandlerCrudTest")
@MergedResource
class HandlerCrudTest {

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
  private Task task1;
  private List<Project> projectList;


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
                             1000L
                            ).create();

    project2 = projectWithID("B",
                             "2020-06-06",
                             "2021-06-06",
                             2000L
                            ).create();

    project3 = projectWithID("B",
                             "2020-07-07",
                             "2021-07-07",
                             3000L
                            ).create();

    projectList = asList(project1, project2);
    Flux<Project> projectFlux = dbUtils.saveProjectList(projectList);

    dbUtils.countAndExecuteFlux(projectFlux, 2);

    task1 = taskWithID("3",
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
  @DisplayName("save")
  public void save() {

    RestAssuredWebTestClient
         .given()
         .webTestClient(mockedWebClient)

         .body(project1)

         .when()
         .post(CRUD_CREATE)

         .then()
         .log()
         .everything()

         .statusCode(OK.value())
         .body("_id", equalTo(project1.get_id()))
         .body("name", equalTo(project1.getName()))
         .body("countryList", hasItems(
              project1.getCountryList()
                      .get(0),
              project1.getCountryList()
                      .get(1)
                                      ))

         .body(matchesJsonSchemaInClasspath("contracts/project/saveOrUpdate.json"))
    ;
  }


  @Test
  @EnabledIf(expression = enabledTest, loadContext = true)
  @DisplayName("FindAll")
  public void FindAll() {

    dbUtils.checkFluxListElements(
         serviceCrud.findAll()
                    .flatMap(Flux::just),
         asList(project1, project2)
                                 );

    RestAssuredWebTestClient

         .given()
         .webTestClient(mockedWebClient)

         .when()
         .get(PROJ_ROOT)

         .then()
         .log()
         .everything()

         .statusCode(OK.value())
         .body("size()", is(2))
         .body("$", hasSize(2))
         .body("name", hasItems(project1.getName(), project2.getName()))
         .body("name", hasItem(project1.getName()))
         .body("name", hasItem(project2.getName()))
         .body("countryList[0]", hasItems(project1.getCountryList()
                                                  .get(0),
                                          project2.getCountryList()
                                                  .get(1)
                                         ))
         .body(matchesJsonSchemaInClasspath("contracts/project/projects.json"))
    ;
  }


  @Test
  @EnabledIf(expression = enabledTest, loadContext = true)
  @DisplayName("FindById")
  public void FindById() {

    RestAssuredWebTestClient

         .given()
         .webTestClient(mockedWebClient)

         .when()
         .get(CRUD_ID, project1.get_id())

         .then()
         .log()
         .everything()

         .statusCode(OK.value())
         .body("name", equalTo(project1.getName()))
         .body("countryList", hasItems(
              project1.getCountryList()
                      .get(0),
              project1.getCountryList()
                      .get(1)
                                      ))
         .body(matchesJsonSchemaInClasspath("contracts/project/findbyid.json"))
    ;
  }


  @Test
  @EnabledIf(expression = enabledTest, loadContext = true)
  @DisplayName("Delete")
  public void Delete() {

    RestAssuredWebTestClient.responseSpecification = responseSpecNoContentType();

    RestAssuredWebTestClient

         .given()
         .webTestClient(mockedWebClient)

         .when()
         .get(CRUD_ID, project1.get_id())

         .then()
         .log()
         .everything()

         .statusCode(OK.value())
    ;
  }


  @Test
  @EnabledIf(expression = enabledTest, loadContext = true)
  @DisplayName("UpdateOptimistic")
  public void UpdateOptimisticLocking() {
    // OPTMISTIC-LOCKING-UPDATE:
    // A) Uses the 'VERSION-ANNOTATION' in THE Entity
    // B) to prevent update-problems when happens 'CONCURRENT-UPDATES'
    // C) EXPLANATION:
    //  C.1) The ENTITY-VERSION in the UPDATING-OBJECT
    //  C.2) must be the same ENTITY-VERSION than the DB-OBJECT
    var previousName = project1.getName();
    var initialVersion = project1.getVersion();
    var updatedVersion = initialVersion + 1;

    // DB-OBJECT-VERSION should be the same as the OBJECT-TO-BE-UPDATED
    project1.setName("NewName");

    RestAssuredWebTestClient
         .given()
         .webTestClient(mockedWebClient)

         .body(project1)

         .when()
         .put(CRUD_UPD)

         .then()
         .log()
         .everything()

         .statusCode(OK.value())
         .body("name", not(equalTo(previousName)))
         .body("version", hasToString(Long.toString(updatedVersion)))

         .body(matchesJsonSchemaInClasspath("contracts/project/saveOrUpdate.json"))
    ;
  }

  @Test
  @EnabledIf(expression = enabledTest, loadContext = true)
  @DisplayName("FindByName")
  public void FindByName() {

    RestAssuredWebTestClient

         .given()
         .webTestClient(mockedWebClient)
         .queryParam("name", project1.getName())

         .when()
         .get(CRUD_BYNAME)

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
  @DisplayName("BHWorks")
  public void bHWorks() {

    bhWorks();
  }
}