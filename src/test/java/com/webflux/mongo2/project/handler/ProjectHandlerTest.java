package com.webflux.mongo2.project.handler;

import com.webflux.mongo2.core.TestDbUtilsConfig;
import com.webflux.mongo2.project.entity.Project;
import com.webflux.mongo2.task.entity.Task;
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

import static com.webflux.mongo2.config.routes.ProjectRoutes.*;
import static config.databuilders.ProjectBuilder.projectWithID;
import static config.databuilders.TaskBuilder.taskWithID;
import static config.testcontainer.TcComposeConfig.TC_COMPOSE_SERVICE;
import static config.testcontainer.TcComposeConfig.TC_COMPOSE_SERVICE_PORT;
import static config.utils.BlockhoundUtils.bhWorks;
import static config.utils.RestAssureSpecs.*;
import static config.utils.TestUtils.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.HttpStatus.OK;

@Import({TestDbUtilsConfig.class})
@DisplayName("ProjectHandlerTest")
@MergedResource
class ProjectHandlerTest {

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
    Flux<Project> projectFlux = dbUtils.saveProjectList(Arrays.asList(project1,project2));
    dbUtils.countAndExecuteProjectFlux(projectFlux,2);

    task1 = taskWithID("3",
                       "Mark",
                       1000L
                      ).create();
    Flux<Task> taskFlux = dbUtils.saveTaskList(singletonList(task1));
    dbUtils.countAndExecuteTaskFlux(taskFlux,1);
  }


  @AfterEach
  void tearDown(TestInfo testInfo) {
    globalTestMessage(testInfo.getTestMethod()
                              .toString(),"method-end");
  }


  @Test
  @EnabledIf(expression = enabledTest, loadContext = true)
  @DisplayName("CreateProject")
  void CreateProject() {
    RestAssuredWebTestClient
         .given()
         .webTestClient(mockedWebClient)

         .body(project1)

         .when()
         .post(PROJ_CREATE)

         .then()
         .log()
         .everything()

         .statusCode(OK.value())
         .body("_id",equalTo(project1.get_id()))
         .body("name",equalTo(project1.getName()))
         .body("countryList",hasItems(
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
  void FindAll() {
    RestAssuredWebTestClient

         .given()
         .webTestClient(mockedWebClient)

         .when()
         .get(PROJ_ROOT)

         .then()
         .log()
         .everything()

         .statusCode(OK.value())
         .body("size()",is(2))
         .body("$",hasSize(2))
         .body("name",hasItems(project1.getName(),project2.getName()))
         .body("name",hasItem(project1.getName()))
         .body("name",hasItem(project2.getName()))
         .body("countryList[0]",hasItems(project1.getCountryList()
                                                 .get(0),
                                         project2.getCountryList()
                                                 .get(1)
                                        ))
         .body(matchesJsonSchemaInClasspath("contracts/project/findAll.json"))
    ;
  }


  @Test
  @EnabledIf(expression = enabledTest, loadContext = true)
  @DisplayName("FindById")
  void FindById() {
    RestAssuredWebTestClient

         .given()
         .webTestClient(mockedWebClient)

         .when()
         .get(PROJ_ID,project1.get_id())

         .then()
         .log()
         .everything()

         .statusCode(OK.value())
         .body("name",equalTo(project1.getName()))
         .body("countryList",hasItems(
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
  void Delete() {
    RestAssuredWebTestClient.responseSpecification = responseSpecNoContentType();

    RestAssuredWebTestClient

         .given()
         .webTestClient(mockedWebClient)

         .when()
         .get(PROJ_ID,project1.get_id())

         .then()
         .log()
         .everything()

         .statusCode(OK.value())
    ;
  }


  @Test
  @EnabledIf(expression = enabledTest, loadContext = true)
  @DisplayName("UpdateOptimistic")
  void UpdateOptimistic() {
    // OPTMISTIC-UPDATE:
    // A) Uses the 'VERSION-ANNOTATION' in THE Entity
    // B) to prevent update-problems when happens 'CONCURRENT-UPDATES'
    // C) EXPLANATION:
    //  C.1) The ENTITY-VERSION in the current OBJECT
    //  C.2) must be different than the ENTITY-VERSION in the previous object
    var previousName = project1.getName();
    var initialOptmisticEntityVersion = project1.getVersion();
    var updatedOptmisticEntityVersion = initialOptmisticEntityVersion + 1;


    project1.setName("NewName");

    RestAssuredWebTestClient
         .given()
         .webTestClient(mockedWebClient)

         .body(project1)

         .when()
         .put(PROJ_UPD)

         .then()
         .log()
         .everything()

         .statusCode(OK.value())
         .body("name",equalTo(project1.getName()))
         .body("name",not(equalTo(previousName)))
         .body("version",hasToString (Long.toString(updatedOptmisticEntityVersion)))

         .body(matchesJsonSchemaInClasspath("contracts/project/findById.json"))
    ;
  }


  @Test
  @EnabledIf(expression = enabledTest, loadContext = true)
  @DisplayName("BHWorks")
  void bHWorks() {
    bhWorks();
  }
}