package com.webflux.api.modules.project.resource;

import com.webflux.api.core.config.annotations.ResourceConfig;
import com.webflux.api.core.config.config.DbUtilsConfig;
import com.webflux.api.core.config.profiles.ProfileGeneral;
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
import static com.webflux.api.core.config.utils.RestAssureSpecs.*;
import static com.webflux.api.core.config.utils.TestUtils.*;
import static com.webflux.api.modules.project.core.routes.RoutesCrud.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.List.of;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.HttpStatus.*;

@Tags(value = {
     @Tag("replicaset"),
     @Tag("standalone"),
     @Tag("testcontainer")
})
@Import({DbUtilsConfig.class})
@DisplayName("1.0 ResourceCrudTest")
@ResourceConfig
@ProfileGeneral
@TcCompose
public class ResourceCrudTest {

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
  private Project project2;
  private Project projetoNoId;


  @BeforeAll
  static void beforeAll(TestInfo testInfo) {

    globalBeforeAll();
    globalTestMessage(testInfo.getDisplayName(), "class-start");
    //    globalComposeServiceContainerMessage(compose,
    //                                         TC_COMPOSE_SERVICE,
    //                                         TC_COMPOSE_SERVICE_PORT
    //                                        );
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

    projetoNoId = projecNoID("C",
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
  @DisplayName("saveWithID")
  public void saveWithID() {

    RestAssuredWebTestClient
         .given()
         .webTestClient(mockedWebClient)

         .body(project1)

         .when()
         .post(CRUD_SAVE)

         .then()
         .log()
         .everything()

         .statusCode(CREATED.value())
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
  @DisplayName("saveNoID")
  public void saveNoID() {

    RestAssuredWebTestClient
         .given()
         .webTestClient(mockedWebClient)

         .body(projetoNoId)

         .when()
         .post(CRUD_SAVE)

         .then()
         .log()
         .everything()

         .statusCode(CREATED.value())
         .body("name", equalTo(projetoNoId.getName()))
         .body("countryList", hasItems(
              projetoNoId.getCountryList()
                         .get(0),
              projetoNoId.getCountryList()
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
         .get(CRUD_FINDALL)

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

    RestAssuredWebTestClient.responseSpecification = noContentTypeAndVoidResponses();

    dbUtils.countAndExecuteFlux(serviceCrud.findAll(), 2);

    RestAssuredWebTestClient

         .given()
         .webTestClient(mockedWebClient)

         .when()
         .delete(CRUD_ID, project1.get_id())

         .then()
         .log()
         .everything()

         .statusCode(NO_CONTENT.value())
    ;

    dbUtils.countAndExecuteFlux(serviceCrud.findAll(), 1);
  }


  @Test
  @EnabledIf(expression = enabledTest, loadContext = true)
  @DisplayName("UpdateOptim")
  public void UpdateOptim() {
    // OPTMISTIC-LOCKING-UPDATE:
    // A) Uses the 'VERSION-ANNOTATION' in THE Entity
    // B) to prevent update-problems when happens 'CONCURRENT-UPDATES'
    // C) EXPLANATION:
    //  C.1) The ENTITY-VERSION in the UPDATING-OBJECT
    //  C.2) must be the same ENTITY-VERSION than the DB-OBJECT
    // DB-OBJECT-VERSION should be the same as the OBJECT-TO-BE-UPDATED
    var initialVersion = project1.getVersion();
    var updatedVersion = initialVersion + 1;

    var previousName = project1.getName();
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
         .queryParam("projectName", project1.getName())

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

    blockHoundTestCheck();
  }
}