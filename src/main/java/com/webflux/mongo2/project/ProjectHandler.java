package com.webflux.mongo2.project;

import com.webflux.mongo2.project.service.IProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.ServerResponse.notFound;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

// HANDLER: Manage HTTP(Resquests/responses)
//HANDLER:
// A) HANDLER receive the message from ROUTERED
// B) and, send this message for SERVICE
@Component("projectHandler")
public class ProjectHandler {

  private final MediaType JSON = MediaType.APPLICATION_JSON;

  @Autowired
  IProjectService projectService;


  public Mono<ServerResponse> createProject(ServerRequest request) {

    // convert/abstracting JSON to Entity
    final Mono<Project> project = request.bodyToMono(Project.class);

    return
         project
              .flatMap(projectService::createProject)
              .flatMap(data ->
                            ok()
                                 .contentType(JSON)
                                 .bodyValue(data))
         //              .onErrorResume(error -> {
         //                if (error instanceof OptimisticLockingFailureException) {
         //                  return ServerResponse.status(BAD_REQUEST)
         //                                       .build();
         //                }
         //                return ServerResponse.status(INTERNAL_SERVER_ERROR)
         //                                     .build();
         //              })
         ;
  }


  public Mono<ServerResponse> update(ServerRequest request) {

    // convert/abstracting JSON to Entity
    final Mono<Project> project = request.bodyToMono(Project.class);

    return
         project
              .flatMap(projectService::createProject)
              .flatMap(data ->
                            ok()
                                 .contentType(JSON)
                                 .bodyValue(data))
         //              .onErrorResume(error -> {
         //                if (error instanceof OptimisticLockingFailureException) {
         //                  return ServerResponse.status(BAD_REQUEST)
         //                                       .build();
         //                }
         //                return ServerResponse.status(INTERNAL_SERVER_ERROR)
         //                                     .build();
         //              })
         ;
  }


  public Mono<ServerResponse> findAll(ServerRequest request) {
    return
         ok()
              .contentType(JSON)
              .body(projectService.findAll(), Project.class);
  }


  public Mono<ServerResponse> findById(ServerRequest request) {

    String id = request.pathVariable("id");

    return
         projectService
              .findById(id)
              .flatMap(data -> ok()
                   .contentType(JSON)
                   .bodyValue(data))
              .switchIfEmpty(notFound()
                                  .build());
  }


  public Mono<ServerResponse> delete(ServerRequest request) {

    String id = request.pathVariable("id");
    // STYLE 01 - DELETE_BY_ID USING FLATMAP
    return
         projectService
              .deleteById(id)
              .flatMap(data -> ServerResponse.ok()
                                             .contentType(JSON)
                                             .bodyValue(data))
              .switchIfEmpty(ServerResponse.notFound()
                                           .build());

    // STYLE 02 - DELETE_BY_ID BASICS
    //    return
    //         ok()
    //              .contentType(JSON)
    //              .body(
    //                   projectService.deleteById(id),Void.class
    //                   )
    //              .log();
  }


  public Mono<ServerResponse> findByName(ServerRequest request) {
    String name = request.queryParam("name")
                         .get();

    return ok()

         .contentType(JSON)

         .body(projectService.findByName(name),Project.class)
         .log();
  }


  public Mono<ServerResponse> findByNameNot(ServerRequest request) {
    String name = request.queryParam("name")
                         .get();

    return ok()
         .contentType(JSON)
         .body(
              projectService.findByNameNot(name),
              Project.class
              )
         .log();
  }


  public Mono<ServerResponse> findByEstimCostGreatThan(ServerRequest request) {
    String cost = request.queryParam("cost")
                         .get();

    return ok()
         .contentType(JSON)
         .body(
              projectService.
                   findByEstimatedCostGreaterThan(Long.valueOf(cost)), Project.class)
         .log();
  }


  public Mono<ServerResponse> findByEstimatedCostBetween(ServerRequest request) {
    String from = request.queryParam("from")
                         .get();
    String to = request.queryParam("to")
                       .get();
    return ok()
         .contentType(JSON)
         .body(projectService.
                    findByEstimatedCostBetween(Long.valueOf(from), Long.valueOf(to)),
               Project.class
              )
         .log();
  }


  public Mono<ServerResponse> findByNameLike(ServerRequest request) {
    String name = request.queryParam("name")
                         .get();

    return ok()
         .contentType(JSON)
         .body(projectService.findByNameLike(name), Project.class)
         .log();
  }


  public Mono<ServerResponse> findByNameRegex(ServerRequest request) {
    String name = request.queryParam("name")
                         .get();

    String regex = "^" + name + "";

    return ok()
         .contentType(JSON)
         .body(projectService.findByNameRegex(regex), Project.class)
         .log();
  }


  public Mono<ServerResponse> findByNameQuery(ServerRequest request) {
    String name = request.queryParam("name")
                         .get();

    return ok()
         .contentType(JSON)
         .body(projectService.findProjectByNameQuery(name), Project.class)
         .log();
  }


  public Mono<ServerResponse> findByNameAndCostQuery(ServerRequest request) {
    String name = request.queryParam("name")
                         .get();
    String cost = request.queryParam("cost")
                         .get();

    return ok()
         .contentType(JSON)
         .body(projectService.findProjectByNameAndCostQuery(name, Long.valueOf(cost)),
               Project.class
              )
         .log();
  }


  public Mono<ServerResponse> findByEstCostBetwQuery(ServerRequest request) {
    String from = request.queryParam("from")
                         .get();
    String to = request.queryParam("to")
                       .get();
    return ok()

         .contentType(JSON)

         .body(projectService.findByEstimatedCostBetweenQuery(Long.valueOf(from), Long.valueOf(to)),
               Project.class
              )
         .log();
  }


  public Mono<ServerResponse> findByNameRegexQuery(ServerRequest request) {
    String name = request.queryParam("name")
                         .get();
    String regex = "^" + name + "";
    return ok()

         .contentType(JSON)

         .body(projectService.findByNameRegexQuery(regex), Project.class)
         .log();
  }


  public Mono<ServerResponse> findProjByNameQueryTempl(ServerRequest request) {
    String name = request.queryParam("name")
                         .get();
    return ok()

         .contentType(JSON)

         .body(projectService.findProjectByNameQueryWithTemplate(name), Project.class)
         .log();
  }


  public Mono<ServerResponse> findByEstCostBetQueryTempl(ServerRequest request) {
    String from = request.queryParam("from")
                         .get();
    String to = request.queryParam("to")
                       .get();
    return ok()

         .contentType(JSON)

         .body(projectService.findByEstimatedCostBetweenQueryWithTemplate(Long.parseLong(from),
                                                                          Long.parseLong(to)
                                                                         ),
               Project.class
              )
         .log();


  }


  public Mono<ServerResponse> findByNameRegexQueryTempl(ServerRequest request) {
    String name = request.queryParam("name")
                         .get();
    String regex = "^" + name + "";
    return ok()

         .contentType(JSON)

         .body(projectService.findByNameRegexQueryWithTemplate(regex), Project.class)
         .log();


  }


  public Mono<ServerResponse> upsertCostWithCritTempl(ServerRequest request) {
    String id = request.queryParam("id")
                       .get();
    String cost = request.queryParam("cost")
                         .get();
    return ok()

         .contentType(JSON)

         .body(projectService.upsertCostWithCriteriaTemplate(id, Long.valueOf(cost)), Void.class)
         .log();


  }


  public Mono<ServerResponse> deleteCriteriaTempl(ServerRequest request) {
    String id = request.queryParam("id")
                       .get();

    return ok()

         .contentType(JSON)

         .body(projectService.deleteWithCriteriaTemplate(id), Void.class)
         .log();


  }


  //  public Mono<ServerResponse> findNoOfProjectsCostGreaterThan(ServerRequest request) {
  //    String cost = request.queryParam("cost")
  //                         .get();
  //    return ok()
  //
  //         .contentType(JSON)
  //
  //         .body(projectService.findNoOfProjectsCostGreaterThan(Long.valueOf(cost)),Long.class)
  //         .log();
  //
  //  }


  //  public Mono<ServerResponse> findCostsGroupByStartDateForProjectsCostGreaterThan
  //  (ServerRequest request) {
  //    String cost = request.queryParam("cost")
  //                         .get();
  //    return ok()
  //
  //         .contentType(JSON)
  //
  //         .body(projectService.findCostsGroupByStartDateForProjectsCostGreaterThan(
  //              Long.valueOf(cost)),ResultByStartDateAndCost.class)
  //         .log();
  //
  //  }


  //  public Mono<ServerResponse> findAllProjectTasks(ServerRequest request) {
  //
  //    return ok()
  //
  //         .contentType(JSON)
  //
  //         .body(projectService.findAllProjectTasks(),ResultProjectTasks.class)
  //         .log();
  //
  //  }


  //  public Mono<ServerResponse> saveProjectAndTask(ServerRequest request) {
  //
  //    Project p = new Project();
  //    p.set_id("6");
  //    p.setName("Project6");
  //
  //    Task t = new Task();
  //    t.set_id("10");
  //    t.setProjectId("6");
  //
  //
  //    return ok()
  //
  //         .contentType(JSON)
  //
  //         .body(projectService.saveProjectAndTask(Mono.just(p),Mono.just(t)),Void.class)
  //         .log();
  //  }


  //  public Mono<ServerResponse> chunkAndSaveProject(ServerRequest request) {
  //
  //    Project p = new Project();
  //    p.set_id("20");
  //    p.setName("ProjectGrid");
  //
  //    return ok()
  //
  //         .contentType(JSON)
  //
  //         .body(projectService.chunkAndSaveProject(p),Void.class)
  //         .log();
  //  }


  //  public Mono<ServerResponse> loadProjectFromGrid(ServerRequest request) {
  //    String pid = request.queryParam("pid")
  //                        .get();
  //
  //
  //    return ok()
  //
  //         .contentType(JSON)
  //
  //         .body(projectService.loadProjectFromGrid(pid),Project.class)
  //         .log();
  //  }


  //  public Mono<ServerResponse> deleteProjectFromGrid(ServerRequest request) {
  //    String pid = request.queryParam("pid")
  //                        .get();
  //
  //
  //    return ok()
  //
  //         .contentType(JSON)
  //
  //         .body(projectService.deleteProjectFromGrid(pid),Project.class)
  //         .log();
  //  }

}