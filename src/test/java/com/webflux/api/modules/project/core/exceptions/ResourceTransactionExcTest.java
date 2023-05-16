package com.webflux.api.modules.project.core.exceptions;

import com.github.javafaker.Faker;
import com.webflux.api.core.config.annotations.ResourceConfig;
import com.webflux.api.core.config.config.ReplicasetConfig;
import com.webflux.api.core.config.testcontainer.container.TcContainerReplicaset;
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
import static com.webflux.api.core.config.testcontainer.container.TcContainerConfig.closeTcContainer;
import static com.webflux.api.core.config.utils.BlockhoundUtils.blockHoundTestCheck;
import static com.webflux.api.core.config.utils.RestAssureSpecs.requestSpecsSetPath;
import static com.webflux.api.core.config.utils.RestAssureSpecs.responseSpecs;
import static com.webflux.api.core.config.utils.TestUtils.TestTitlesClass.*;
import static com.webflux.api.core.config.utils.TestUtils.*;
import static com.webflux.api.modules.project.core.routes.RoutesCrud.PROJ_ROOT_CRUD;
import static com.webflux.api.modules.project.core.routes.RoutesTransaction.REPO_TRANSACT_CLASSIC;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.List.of;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;

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
/*
 ╔═════════════════════════════════════════════════════════════════╗
 ║                    ANOTACAO DE TCCONTAINER                      ║
 ╠═════════════════════════════════════════════════════════════════╣
 ║ A) Sera inocua, se imports staticos, de sua classe de extensao  ║
 ║    da anotacao, forem feitos                                    ║
 ║ B) Tal classe de extensao, possui "STATIC-AUTOMATIC-STARTING",  ║
 ║    assim seu "importe" ja a iniciara, independente da anotacao  ║
 ╚═════════════════════════════════════════════════════════════════╝
*/
@Tags(value = {
     @Tag("replicaset"),
     @Tag("no-standalone"),
     @Tag("testcontainer")})
@Import({ReplicasetConfig.class})
@Slf4j
@DisplayName("6.1 ResourceTransactionExcTest")
@ResourceConfig
@TcContainerReplicaset // TEST TRANSACTIONS
public class ResourceTransactionExcTest {
/*╔════════════════════════════════════════════════════════════╗
  ║              TEST-TRANSACTIONS + TEST-CONTAINERS           ║
  ╠════════════════════════════════════════════════════════════╣
  ║ a) TRANSACTIONS IN MONGO-DB DEPENDS ON THE REPLICASET      ║
  ║    - MEANING: TRANSACTIONS ONLY WILL WORK WITH REPLICASET  ║
  ║                                                            ║
  ║ b) MongoDBContainer provides REPLICASET automatically      ║
  ║    - MEANING:                                              ║
  ║      B.1) TESTS MUST BE DONE WITH "MongoDBContainer"       ║
  ║      B.2) DO NOT USE TEST-CONTAINER-DOCKER-COMPOSE-MODULE  ║
  ╚════════════════════════════════════════════════════════════╝*/

  final static String enabledTest = "true";

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

    System.clearProperty("runTest");
    System.setProperty("runTest", enabledTest);

    globalBeforeAll();
    globalTestMessage(testInfo.getDisplayName(), CLASS_START);

    RestAssuredWebTestClient.reset();
    RestAssuredWebTestClient.requestSpecification = requestSpecsSetPath(
         "http://localhost:8080" + PROJ_ROOT_CRUD);
    RestAssuredWebTestClient.responseSpecification = responseSpecs();
  }


  @AfterAll
  static void afterAll(TestInfo testInfo) {

    closeTcContainer();
    globalAfterAll();
    globalTestMessage(testInfo.getDisplayName(), CLASS_END);
  }

  @BeforeEach
  void beforeEach(TestInfo testInfo) {

    globalTestMessage(testInfo.getTestMethod()
                              .toString(), METHOD_START);

    Project project1 = projecNoID("C", "2020-05-05", "2021-05-05", 1000L, of("UK", "USA")).create();

    Project projetoWithId = projectWithID("C", "2020-05-05", "2021-05-05", 1000L,
                                          of("HOL", "CAN")
    ).create();

    List<Project> projectList = asList(project1, projetoWithId);
    Flux<Project> projectFlux = dbUtils.saveProjectList(projectList);
    dbUtils.countAndExecuteFlux(projectFlux, 2);

    Task task1 = taskWithID("3", "Mark", 1000L).create();
    Flux<Task> taskFlux = dbUtils.saveTaskList(singletonList(task1));
    dbUtils.countAndExecuteFlux(taskFlux, 1);
  }

  @AfterEach
  void tearDown(TestInfo testInfo) {

    globalTestMessage(testInfo.getTestMethod()
                              .toString(), METHOD_END);
  }

  // STYLE 01: System.setProperty + Spring Expression Language (SpEL)
  @Test
  @EnabledIf(expression = "#{systemProperties[runTest] == 'true' && " + "!environment" +
                          ".acceptsProfiles('test-stand-alone')}", loadContext = true)
  @DisplayName("checkContentWithExc")
  public void checkContentWithExceptions() {

    var newTaskName = Faker.instance()
                           .name()
                           .firstName();

    Project project = projectWithID("C", "2020-05-05", "2021-05-05", 1000L,
                                    of("UK", "USA")
    ).create();

    project.setName("xx");
    newTaskName = "XX";

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

         .statusCode(NOT_ACCEPTABLE.value())
         .body(matchesJsonSchemaInClasspath(
              "contracts/transactions/checkContentWithExc.json"));

    dbUtils.countAndExecuteFlux(serviceCrud.findAll(), 2);
    dbUtils.countAndExecuteFlux(taskService.findAll(), 1);
  }

  // STYLE 01: System.setProperty + Spring Expression Language (SpEL)
  @Test
  @EnabledIf(expression = "#{systemProperties[runTest] == 'true' " + "&& !environment" +
                          ".acceptsProfiles('test-stand-alone')}", loadContext = true)
  @DisplayName("transactionsClassicExcTaskLessThree")
  public void transactionsClassicExcTaskLessThanThree() {

    var newTaskName = Faker.instance()
                           .name()
                           .firstName();

    Project project = projectWithID("C", "2020-05-05", "2021-05-05", 1000L,
                                    of("UK", "USA")
    ).create();

    project.setName("NOT-EMPTY");
    newTaskName = "XX";

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

         .statusCode(NOT_ACCEPTABLE.value())
         .body(matchesJsonSchemaInClasspath(
              "contracts/transactions/transactionsClassicExcTaskLessThanThree" +
              ".json"));

    dbUtils.countAndExecuteFlux(serviceCrud.findAll(), 2);
    dbUtils.countAndExecuteFlux(taskService.findAll(), 1);
  }

  // STYLE 02: ExcludeTags in Suite
  @Test
  @Tag("no-standalone")
  @EnabledIf(expression = enabledTest, loadContext = true)
  @DisplayName("transactionsClassicExcTaskEmpty")
  public void transactionsClassicExcTaskEmpty() {

    var newTaskName = Faker.instance()
                           .name()
                           .firstName();

    Project project = projectWithID("C", "2020-05-05", "2021-05-05", 1000L,
                                    of("UK", "USA")
    ).create();

    project.setName("NOT-EMPTY");
    newTaskName = "";

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

         .statusCode(NOT_ACCEPTABLE.value())
         .body(matchesJsonSchemaInClasspath(
              "contracts/transactions/transactionsClassicExcTaskEmpty.json"));

    dbUtils.countAndExecuteFlux(serviceCrud.findAll(), 2);
    dbUtils.countAndExecuteFlux(taskService.findAll(), 1);
  }

  // STYLE 02: ExcludeTags in Suite
  @Test
  @Tag("no-standalone")
  @EnabledIf(expression = enabledTest, loadContext = true)
  @DisplayName("transactionsClassicExcProjectEmpty")
  public void transactionsClassicExcProjectEmpty() {

    var newTaskName = Faker.instance()
                           .name()
                           .firstName();
/*
{
    "path": "/project/savewithtx",
    "status": 400,
    "reason": "Validation failure",
    "Global-Global-Atribute": "Global-General-Message",
    "Global-Dev-Atribute": "Global-Dev-Message"
}
 */
    Project project =
         projectWithID("C",
                       "2020-05-05",
                       "2021-05-05",
                       1000L,
                       of("UK", "USA")
         ).create();

    project.setName("");
    newTaskName = "";

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
         .statusCode(BAD_REQUEST.value())
         .body("detail", containsString("Project Name cannot be Empty - BeanValid!!!!"))
         .body("title", containsString("Bean Validations"))
         .body(matchesJsonSchemaInClasspath(
              "contracts/exceptions/beanValidation/NameNotEmpty.json"));

         ;

    dbUtils.countAndExecuteFlux(serviceCrud.findAll(), 2);
    dbUtils.countAndExecuteFlux(taskService.findAll(), 1);
  }

  @Test
  @EnabledIf(expression = enabledTest, loadContext = true)
  @DisplayName("BHWorks")
  public void bHWorks() {

    blockHoundTestCheck();
  }
}