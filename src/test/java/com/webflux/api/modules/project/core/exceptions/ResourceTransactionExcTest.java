package com.webflux.api.modules.project.core.exceptions;

import com.webflux.api.core.TestDbUtilsConfig;
import com.webflux.api.modules.project.entity.Project;
import com.webflux.api.modules.project.service.IServiceCrud;
import com.webflux.api.modules.task.Task;
import com.webflux.api.modules.task.service.IServiceTask;
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

import static com.webflux.api.modules.project.core.routes.RoutesCrud.PROJ_ROOT_CRUD;
import static com.webflux.api.modules.project.core.routes.RoutesTransaction.REPO_TRANSACT;
import static config.databuilders.ProjectBuilder.projecNoID;
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
import static java.util.List.of;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Import({TestDbUtilsConfig.class})
@DisplayName("ResourceTransactionExcTest")
@MergedResource
class ResourceTransactionExcTest {

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
  IServiceTask taskService;

  private Project projetoWithId;


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

    globalTestMessage(testInfo.getTestMethod()
                              .toString(), "method-start");

    Project project1 = projecNoID("C",
                                  "2020-05-05",
                                  "2021-05-05",
                                  1000L,
                                  of("UK", "USA")
                                 ).create();

    projetoWithId = projectWithID("C",
                                  "2020-05-05",
                                  "2021-05-05",
                                  1000L,
                                  of("HOL", "CAN")
                                 ).create();

    List<Project> projectList = asList(project1, projetoWithId);
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
  @DisplayName("saveProjectAndTaskTransactionExc")
  public void saveProjectAndTaskTransactionExc() {

    dbUtils.countAndExecuteFlux(serviceCrud.findAll(), 2);
    dbUtils.countAndExecuteFlux(taskService.findAll(), 1);

    RestAssuredWebTestClient.responseSpecification = noContentTypeAndVoidResponses();

    var newProjectName = "xxxxx";

    var task2 = taskWithID("3",
                           "Mark",
                           1000L
                          ).create();

        task2.setName("xx"); // transation is activated because taskName is less than 3 character


    RestAssuredWebTestClient
         .given()
         .webTestClient(mockedWebClient)

         .body(task2)
         .queryParam("projectId", projetoWithId.get_id())
         .queryParam("newProjectName", newProjectName)
         // .queryParam("completeStackTrace", true)

         .when()
         .post(REPO_TRANSACT)

         .then()
         .log()
         .everything()

         .statusCode(NOT_FOUND.value())
         .body("detail", equalTo("Task name is empty or not have minimal chracteres. It is not allowed!!!!"))
         .body(matchesJsonSchemaInClasspath("contracts/exceptions/project/transaction.json"))
    ;

    dbUtils.countAndExecuteFlux(serviceCrud.findAll(), 2);
    dbUtils.countAndExecuteFlux(taskService.findAll(), 1);
  }


  @Test
  @EnabledIf(expression = enabledTest, loadContext = true)
  @DisplayName("BHWorks")
  public void bHWorks() {

    bhWorks();
  }
}