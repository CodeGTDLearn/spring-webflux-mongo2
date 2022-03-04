package com.webflux.api.modules.project.resource;

import com.github.javafaker.Faker;
import com.webflux.api.core.config.testconfigs.TestDbUtilsConfig;
import com.webflux.api.modules.project.entity.Project;
import com.webflux.api.modules.project.entity.ProjectChild;
import com.webflux.api.modules.project.service.IServiceChildArray;
import com.webflux.api.modules.task.entity.Task;
import com.webflux.api.modules.task.service.IServiceTask;
import com.webflux.api.core.config.annotations.ResourceTcCompose;
import com.webflux.api.core.config.databuilders.ProjectChildBuilder;
import com.webflux.api.core.config.testcontainer.compose.TcComposeConfig;
import com.webflux.api.core.config.utils.TestDbUtils;
import io.restassured.module.webtestclient.RestAssuredWebTestClient;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.EnabledIf;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.junit.jupiter.Container;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static com.webflux.api.modules.project.core.routes.template.RoutesColections.*;
import static com.webflux.api.core.config.databuilders.ProjectBuilder.projecNoID;
import static com.webflux.api.core.config.databuilders.TaskBuilder.taskWithID;
import static com.webflux.api.core.config.utils.RestAssureSpecs.*;
import static com.webflux.api.core.config.utils.TestUtils.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.List.of;
import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.http.HttpStatus.OK;

@Import({TestDbUtilsConfig.class})
@DisplayName("ResourceColectionsTest")
@ResourceTcCompose
public
class ResourceColectionsTest {

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

  @Autowired
  IServiceTask serviceTask;

  @Autowired
  IServiceChildArray serviceChildArray;

  private Task task1, task2;
  private ProjectChild project1Child;


  @BeforeAll
  static void beforeAll(TestInfo testInfo) {

    globalBeforeAll();
    globalTestMessage(testInfo.getDisplayName(), "class-start");

    RestAssuredWebTestClient.reset();
    RestAssuredWebTestClient.requestSpecification =
         requestSpecsSetPath("http://localhost:8080" + TEMPL_ROOT_COL);
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

    Project project1 = projecNoID("C",
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

    List<Project> projectList = asList(project1, project2);
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

    project1Child =
         ProjectChildBuilder
              .projectChildWithID("D",
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
  @DisplayName("deleteTemplMultCollections")
  public void deleteTemplMultCollections() {

    RestAssuredWebTestClient.responseSpecification = noContentTypeAndVoidResponses();

    dbUtils.countAndExecuteFlux(serviceTask.findAll(), 1);

    String projId = project1Child.get_id();
    String taskId = task1.get_id();
    RestAssuredWebTestClient

         .given()
         .webTestClient(mockedWebClient)
         .queryParam("projectId", projId)
         .queryParam("taskIdToDelete", taskId)

         .when()
         .delete(TEMPL_DEL_CRIT_MULT_COL)

         .then()
         .log()
         .everything()

         .statusCode(OK.value())
    ;

    StepVerifier
         .create(serviceChildArray.existTheTaskInProjectChild(projId, taskId))
         .expectSubscription()
         .expectNext(false)
         .verifyComplete();


    dbUtils.countAndExecuteFlux(serviceTask.findAll(), 0);
  }

  @Test
  @EnabledIf(expression = enabledTest, loadContext = true)
  @DisplayName("addTemplMultCollections")
  public void addTemplMultCollections() {

    dbUtils.countAndExecuteFlux(serviceTask.findAll(), 1);

    var task3 = taskWithID(project1Child.get_id(),
                           "Mark Zuck",
                           7000L
                          ).create();

    RestAssuredWebTestClient

         .given()
         .webTestClient(mockedWebClient)

         .body(task3)

         .when()
         .post(TEMPL_ADD_CRIT_MULT_COL)

         .then()
         .log()
         .everything()

         .statusCode(OK.value())
         .body("_id", containsStringIgnoringCase(project1Child.get_id()))
         .body("tasks.name", hasItem(task3.getName()))
         .body("tasks.description", hasItem(task3.getDescription()))
         .body(matchesJsonSchemaInClasspath("contracts/project/updateChild.json"))
    ;

    dbUtils.countAndExecuteFlux(serviceTask.findAll(), 2);
  }

  @Test
  @EnabledIf(expression = enabledTest, loadContext = true)
  @DisplayName("updateTemplMultCollections")
  public void updateTemplMultCollections() {

    task2.setProjectId(project1Child.get_id());
    String name = Faker.instance()
                       .funnyName()
                       .name();
    task2.setName(name);

    RestAssuredWebTestClient

         .given()
         .webTestClient(mockedWebClient)

         .body(task2)

         .when()
         .put(TEMPL_UPD_CRIT_MULT_COL)

         .then()
         .log()
         .everything()

         .statusCode(OK.value())
         .body("_id", containsStringIgnoringCase(project1Child.get_id()))
         .body("tasks.name", hasItem(name))
         .body(matchesJsonSchemaInClasspath("contracts/project/updateChild.json"))
    ;
  }

  @Test
  @EnabledIf(expression = enabledTest, loadContext = true)
  @DisplayName("deleteAllColletionsTemplate")
  public void deleteAllColletionsTemplate() {

    RestAssuredWebTestClient.responseSpecification = noContentTypeAndVoidResponses();

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

    RestAssuredWebTestClient.responseSpecification = noContentTypeAndVoidResponses();
    RestAssuredWebTestClient

         .given()
         .webTestClient(mockedWebClient)

         .when()
         .get(TEMPL_CHECK_DB_CRIT_COL)

         .then()
         .log()
         .everything()

         .statusCode(OK.value())
    ;
  }
}