package com.webflux.api.modules.project.resource;

import com.github.javafaker.Faker;
import com.webflux.api.core.config.annotations.ResourceTcContainerForTransactions;
import com.webflux.api.core.config.testconfigs.TestCoreConfig;
import com.webflux.api.core.config.utils.TestDbUtils;
import com.webflux.api.modules.project.entity.Project;
import com.webflux.api.modules.project.service.IServiceCrud;
import com.webflux.api.modules.task.entity.Task;
import com.webflux.api.modules.task.service.IServiceTask;
import io.restassured.module.webtestclient.RestAssuredWebTestClient;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.EnabledIf;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import java.util.List;

import static com.webflux.api.core.config.databuilders.ProjectBuilder.projecNoID;
import static com.webflux.api.core.config.databuilders.ProjectBuilder.projectWithID;
import static com.webflux.api.core.config.databuilders.TaskBuilder.taskWithID;
import static com.webflux.api.core.config.utils.BlockhoundUtils.blockHoundTestCheck;
import static com.webflux.api.core.config.utils.RestAssureSpecs.requestSpecsSetPath;
import static com.webflux.api.core.config.utils.RestAssureSpecs.responseSpecs;
import static com.webflux.api.core.config.utils.TestUtils.*;
import static com.webflux.api.modules.project.core.routes.RoutesCrud.PROJ_ROOT_CRUD;
import static com.webflux.api.modules.project.core.routes.RoutesTransaction.REPO_TRANSACT_CLASSIC;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.List.of;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpStatus.CREATED;



/*
  ╔══════════════════════════════════════════════════════════════════════╗
  ║                         SILAEV + TRANSACTIONS                        ║
  ╠══════════════════════════════════════════════════════════════════════╣
  ║ MongoDBContainer does replicaset init automatically:                 ║
  ║ a) add a static field with MongoDBContainer                          ║
  ║ b) run it in @BeforeAll and                                          ║
  ║ c) create a 'STATIC CLASS INITIALIZER' to set spring.data.mongodb.uri║
  ║ d) define @ContextConfiguration with 'static class Initializer'      ║
  ╚══════════════════════════════════════════════════════════════════════╝
*/
@Import({TestCoreConfig.class})
@Slf4j
@DisplayName("6.0 ResourceTransactionTest")
@ResourceTcContainerForTransactions
public class ResourceTransactionTest {
  /*
╔════════════════════════════════════════════════════════════╗
║              TEST-TRANSACTIONS + TEST-CONTAINERS           ║
╠════════════════════════════════════════════════════════════╣
║ a) TRANSACTIONS IN MONGO-DB DEPENDS ON THE REPLICASET      ║
║    - MEANING: TRANSACTIONS ONLY WILL WORK WITH REPLICASET  ║
║                                                            ║
║ b) MongoDBContainer provides REPLICASET automatically      ║
║    - MEANING:                                              ║
║      B.1) TESTS MUST BE DONE WITH "MongoDBContainer"       ║
║      B.2) DO NOT USE TEST-CONTAINER-DOCKER-COMPOSE-MODULE  ║
╚════════════════════════════════════════════════════════════╝
*/
  // STATIC-@Container: one service for ALL tests -> SUPER FASTER
  // NON-STATIC-@Container: one service for EACH test

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

//    closeTcContainer();
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

    Project projetoWithId = projectWithID("C",
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

    dbUtils.cleanTestDb();
  }


  @Test
  @EnabledIf(expression = enabledTest, loadContext = true)
  @DisplayName("createProjectTransaction")
  public void createProjectTransaction() {

    dbUtils.countAndExecuteFlux(serviceCrud.findAll(), 2);
    dbUtils.countAndExecuteFlux(taskService.findAll(), 1);

    var newTaskName = Faker.instance()
                           .name()
                           .firstName();

    Project project = projectWithID("C",
                                    "2020-05-05",
                                    "2021-05-05",
                                    1000L,
                                    of("UK", "USA")
                                   ).create();

    RestAssuredWebTestClient
         .given()
         .webTestClient(mockedWebClient)

         .body(project)
         .queryParam("taskNameInitial", newTaskName)

         .when()
         .post(REPO_TRANSACT_CLASSIC)

         .then()
         .log()
         .everything()

         .statusCode(CREATED.value())
         .body("name", equalTo(newTaskName))
         .body("projectId", equalTo(project.get_id()))
    //         .body(matchesJsonSchemaInClasspath("contracts/project/checkContentWithExc.json"))
    ;

    dbUtils.countAndExecuteFlux(serviceCrud.findAll(), 3);
    dbUtils.countAndExecuteFlux(taskService.findAll(), 2);
  }


  @Test
  @EnabledIf(expression = enabledTest, loadContext = true)
  @DisplayName("BHWorks")
  public void bHWorks() {

    blockHoundTestCheck();
  }

}