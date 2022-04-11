package com.webflux.api.modules.project.core.exceptions;

import com.github.javafaker.Faker;
import com.webflux.api.core.config.profiles.ProfileGeneral;
import com.webflux.api.core.config.annotations.ResourceConfig;
import com.webflux.api.core.config.config.DbUtilsConfig;
import com.webflux.api.core.config.testcontainer.compose.TcCompose;
import com.webflux.api.core.config.testcontainer.compose.TcComposeConfig;
import com.webflux.api.core.config.utils.TestDbUtils;
import com.webflux.api.modules.project.entity.Project;
import com.webflux.api.modules.project.service.IServiceCrud;
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
import static com.webflux.api.modules.project.core.routes.RoutesCrud.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.List.of;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Tags(value = {
     @Tag("replicaset"),
     @Tag("standalone"),
     @Tag("testcontainer")
})
@Import({DbUtilsConfig.class})
@DisplayName("1.1 ResourceCrudExcTest")
@ResourceConfig
@ProfileGeneral
@TcCompose
public class ResourceCrudExcTest {

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

  @Autowired
  IServiceCrud serviceCrud;

  private Project project1;


  @BeforeAll
  static void beforeAll(TestInfo testInfo) {

    globalBeforeAll();
    globalTestMessage(testInfo.getDisplayName(), "class-start");

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

    Project project2 = projecNoID("B",
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
    Project projetoNoId = projecNoID("C",
                                     "2020-05-05",
                                     "2021-05-05",
                                     1000L,
                                     of("HOL", "CAN")
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
         //         .statusCode(NOT_FOUND.value())
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
         .body(matchesJsonSchemaInClasspath(
              "contracts/exceptions/project/updateValidNotEmptyError.json"))
    ;
  }

  @Test
  @EnabledIf(expression = enabledTest, loadContext = true)
  @DisplayName("UpdateValidNotEmpty")
  public void UpdateValidNameNotEmpty() {

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
         .body(matchesJsonSchemaInClasspath(
              "contracts/exceptions/project/updateValidNotEmptyError.json"))
    ;
  }

  @Test
  @EnabledIf(expression = enabledTest, loadContext = true)
  @DisplayName("BHWorks")
  public void bHWorks() {

    blockHoundTestCheck();
  }
}