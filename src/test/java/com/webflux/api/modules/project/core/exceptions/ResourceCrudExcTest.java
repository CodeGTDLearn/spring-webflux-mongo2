package com.webflux.api.modules.project.core.exceptions;

import com.github.javafaker.Faker;
import com.webflux.api.core.TestDbUtilsConfig;
import com.webflux.api.modules.project.entity.Project;
import com.webflux.api.modules.project.service.IServiceCrud;
import com.webflux.api.modules.task.Task;
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

import static com.webflux.api.modules.project.core.routes.RoutesCrud.*;
import static config.databuilders.ProjectBuilder.projecNoID;
import static config.databuilders.TaskBuilder.taskWithID;
import static config.testcontainer.TcComposeConfig.TC_COMPOSE_SERVICE;
import static config.testcontainer.TcComposeConfig.TC_COMPOSE_SERVICE_PORT;
import static config.utils.BlockhoundUtils.bhWorks;
import static config.utils.RestAssureSpecs.requestSpecsSetPath;
import static config.utils.RestAssureSpecs.responseSpecs;
import static config.utils.TestUtils.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.List.of;
import static org.springframework.http.HttpStatus.*;

@Import({TestDbUtilsConfig.class})
@DisplayName("ResourceCrudTest")
@MergedResource
class ResourceCrudExcTest {

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

  private Project project1, project2, project3, projetoNoId;
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
         requestSpecsSetPath("http://localhost:8080" + PROJ_ROOT_CRUD);
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

    project3 = projecNoID("B",
                          "2020-07-07",
                          "2021-07-07",
                          3000L,
                          of("UK", "USA")
                         ).create();
    projetoNoId = projecNoID("C",
                             "2020-05-05",
                             "2021-05-05",
                             1000L,
                             of("HOL", "CAN")
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
  @DisplayName("FindByIdExc")
  public void FindByIdExc() {

    RestAssuredWebTestClient

         .given()
         .webTestClient(mockedWebClient)

         .when()
         .get(CRUD_ID, Faker.instance()
                            .idNumber()
                            .invalid())

         .then()
         .log()
         .everything()

         .statusCode(NOT_FOUND.value())
         .body(matchesJsonSchemaInClasspath("contracts/exceptions/project/projectNotFound.json"))
    ;
  }

  @Test
  @EnabledIf(expression = enabledTest, loadContext = true)
  @DisplayName("deleteExc")
  public void deleteExc() {

    dbUtils.countAndExecuteFlux(serviceCrud.findAll(), 2);
    RestAssuredWebTestClient

         .given()
         .webTestClient(mockedWebClient)

         .when()
         .delete(CRUD_ID, Faker.instance()
                               .idNumber()
                               .invalid())

         .then()
         .log()
         .everything()

         .statusCode(NOT_FOUND.value())
         .body(matchesJsonSchemaInClasspath("contracts/exceptions/project/projectNotFound.json"))
    ;
    dbUtils.countAndExecuteFlux(serviceCrud.findAll(), 2);
  }

  @Test
  @EnabledIf(expression = enabledTest, loadContext = true)
  @DisplayName("UpdateNoProjectIdExc")
  public void UpdateNoProjectIdExc() {

    project1.set_id(Faker.instance()
                         .idNumber()
                         .invalid());

    RestAssuredWebTestClient
         .given()
         .webTestClient(mockedWebClient)

         .body(project1)

         .when()
         .put(CRUD_UPD)

         .then()
         .log()
         .everything()

         .statusCode(NOT_FOUND.value())
         .body(matchesJsonSchemaInClasspath("contracts/exceptions/project/projectNotFound.json"))
    ;
  }

  @Test
  @EnabledIf(expression = enabledTest, loadContext = true)
  @DisplayName("UpdateOptExc")
  public void UpdateOptExc() {
    // OPTIMISTIC-LOCKING-UPDATE:
    // A) Uses the 'VERSION-ANNOTATION' in THE Entity
    // B) to prevent update-problems when happens 'CONCURRENT-UPDATES'
    // C) EXPLANATION:
    //  C.1) The ENTITY-VERSION in the UPDATING-OBJECT
    //  C.2) must be the "SAME" ENTITY-VERSION as the DB-OBJECT
    //  DB-OBJECT-VERSION should be the same as the OBJECT-TO-BE-UPDATED
    project1.setVersion(1L);

    RestAssuredWebTestClient
         .given()
         .webTestClient(mockedWebClient)

         .body(project1)

         .when()
         .put(CRUD_UPD)

         .then()
         .log()
         .everything()

         .statusCode(BAD_REQUEST.value())
         .body(matchesJsonSchemaInClasspath("contracts/exceptions/project/UpdateOptExc.json"))
    ;
  }

  @Test
  @EnabledIf(expression = enabledTest, loadContext = true)
  @DisplayName("UpdateValid")
  public void UpdateValid() {

    project1.setName("");

    RestAssuredWebTestClient
         .given()
         .webTestClient(mockedWebClient)

         .body(project1)

         .when()
         .put(CRUD_UPD)

         .then()
         .log()
         .everything()

         .statusCode(BAD_REQUEST.value())
         .body(matchesJsonSchemaInClasspath("contracts/exceptions/project/updateValid.json"))
    ;
  }

  @Test
  @EnabledIf(expression = enabledTest, loadContext = true)
  @DisplayName("UpdateValidTransaction")
  public void UpdateValidTransaction() {

    project1.setName("");

    RestAssuredWebTestClient
         .given()
         .webTestClient(mockedWebClient)

         .body(project1)

         .when()
         .put(CRUD_UPD)

         .then()
         .log()
         .everything()
         .statusCode(BAD_REQUEST.value())
         .body(matchesJsonSchemaInClasspath("contracts/exceptions/project/updateValid.json"))
    ;
  }

  @Test
  @EnabledIf(expression = enabledTest, loadContext = true)
  @DisplayName("BHWorks")
  public void bHWorks() {

    bhWorks();
  }
}