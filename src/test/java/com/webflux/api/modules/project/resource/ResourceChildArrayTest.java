package com.webflux.api.modules.project.resource;

import com.github.javafaker.Faker;
import com.webflux.api.core.config.annotations.ResourceConfig;
import com.webflux.api.core.config.config.DbUtilsConfig;
import com.webflux.api.core.config.databuilders.ProjectChildBuilder;
import com.webflux.api.core.config.testcontainer.compose.TcCompose;
import com.webflux.api.core.config.testcontainer.compose.TcComposeConfig;
import com.webflux.api.core.config.utils.TestDbUtils;
import com.webflux.api.modules.project.entity.Project;
import com.webflux.api.modules.project.entity.ProjectChild;
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

import java.util.Arrays;
import java.util.List;

import static com.webflux.api.core.config.databuilders.ProjectBuilder.projecNoID;
import static com.webflux.api.core.config.databuilders.TaskBuilder.taskWithID;
import static com.webflux.api.core.config.utils.RestAssureSpecs.requestSpecsSetPath;
import static com.webflux.api.core.config.utils.RestAssureSpecs.responseSpecs;
import static com.webflux.api.core.config.utils.TestUtils.*;
import static com.webflux.api.core.config.utils.TestUtils.TestTitlesClass.*;
import static com.webflux.api.modules.project.core.routes.template.RoutesChildArray.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.List.of;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpStatus.OK;

@Tags(value = {
     @Tag("replicaset"),
     @Tag("standalone"),
     @Tag("testcontainer")
})
@Import({DbUtilsConfig.class})
@DisplayName("4.2 ResourceChildArrayTest")
@ResourceConfig
@TcCompose
public class ResourceChildArrayTest {

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
  private ProjectChild project1Child;


  @BeforeAll
  static void beforeAll(TestInfo testInfo) {

    globalBeforeAll();
    globalTestMessage(testInfo.getDisplayName(), TestTitlesClass.CLASS_START);

    RestAssuredWebTestClient.reset();
    RestAssuredWebTestClient.requestSpecification =
         requestSpecsSetPath("http://localhost:8080" + TEMPL_ROOT_CHILD);
    RestAssuredWebTestClient.responseSpecification = responseSpecs();
  }


  @AfterAll
  static void afterAll(TestInfo testInfo) {

    globalAfterAll();
    globalTestMessage(testInfo.getDisplayName(), CLASS_END);
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
                              .toString(), METHOD_START);

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

    List<Project> projectList = asList(project1, project2);
    Flux<Project> projectFlux = dbUtils.saveProjectList(projectList);

    dbUtils.countAndExecuteFlux(projectFlux, 2);

    Task task1 = taskWithID("3",
                            "Mark",
                            1000L
    ).create();
    Task task2 = taskWithID("4",
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
    List<ProjectChild> projectChildList = List.of(project1Child);
    Flux<ProjectChild> projectChildFlux = dbUtils.saveProjectChildList(projectChildList);
    dbUtils.countAndExecuteFlux(projectChildFlux, 1);

  }


  @AfterEach
  void tearDown(TestInfo testInfo) {

    globalTestMessage(testInfo.getTestMethod()
                              .toString(), METHOD_END);
  }


  @Test
  @EnabledIf(expression = enabledTest, loadContext = true)
  @DisplayName("AddCritTemplArray")
  public void AddCritTemplArray() {

    var countryAdded = "BR";

    RestAssuredWebTestClient

         .given()
         .webTestClient(mockedWebClient)
         .queryParam("projectId", project1.get_id())
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
         .queryParam("projectId", project1.get_id())
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
         .queryParam("projectId", project1.get_id())
         .queryParam("country", project1.getCountryList()
                                        .get(1))
         .body(project1)

         .when()
         .delete(TEMPL_DEL_ARRAY_CRIT)

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
    /* error: the object is isnerted but takes null in all fields except id
            {
            "_id": null,
            "projectId": "PPq4jvduh6p753p1v8zng26475",
            "name": null,
            "description": null,
            "ownername": null,
            "cost": 0,
            "version": null
        }
     */

    var taskToAdd = taskWithID("4444",
                               "Mark ZuckLoki",
                               7000L
    ).create();

    RestAssuredWebTestClient

         .given()
         .webTestClient(mockedWebClient)

         .queryParam("projectId", project1Child.get_id())
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
         .queryParam("projectId", project1Child.get_id())
         .queryParam("taskIdToUpdate", project1Child.getTasks()
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
         .queryParam("projectId", project1Child.get_id())
         .queryParam("taskIdtoDelete", project1Child.getTasks()
                                                    .get(0)
                                                    .get_id())
         .body(project1Child)

         .when()
         .delete(TEMPL_DEL_CHILD_CRIT)

         .then()
         .log()
         .everything()

         .statusCode(OK.value())
    ;

    dbUtils.countAndExecuteFlux(serviceCrud.findAll(), 2);
  }

  @Test
  @EnabledIf(expression = enabledTest, loadContext = true)
  @DisplayName("existTheTaskInProjectChild")
  public void existTheTaskInProjectChild() {

    String result =
         RestAssuredWebTestClient

              .given()
              .webTestClient(mockedWebClient)
              .queryParam("projectId", project1Child.get_id())
              .queryParam("idTask", project1Child.getTasks()
                                                 .get(0)
                                                 .get_id())
              .body(project1Child)

              .when()
              .get(TEMPL_CHK_CHILD_EXIST_CRIT)

              .then()
              .log()
              .everything()

              .statusCode(OK.value())
              .extract()
              .asString();

    assertEquals(result, "true");
  }

}