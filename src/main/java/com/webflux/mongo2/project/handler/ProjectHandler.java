package com.webflux.mongo2.project.handler;

import com.webflux.mongo2.project.Project;
import com.webflux.mongo2.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

// HANDLER: Manage HTTP(Resquests/responses)
@Component
public class ProjectHandler {

  private final MediaType JSON = MediaType.APPLICATION_JSON;

  @Autowired
  com.webflux.mongo2.project.service.IProjectService IProjectService;


  public Mono<ServerResponse> createProject(ServerRequest request) {

    final Mono<Project> project = request.bodyToMono(Project.class); // convert/abstracting JSON to Entity

    return
         project
              .flatMap(IProjectService::createProject)
              .flatMap(data ->
                            ServerResponse.ok()
                                          .contentType(JSON)
                                          .bodyValue(data));/*.onErrorResume(error -> { if
                                                  (error instanceof
								  OptimisticLockingFailureException){ return
										  ServerResponse.status(HttpStatus.BAD_REQUEST).build(); 
								}
										 return
										  ServerResponse.status(500).build(); });*/

  }


  public Mono<ServerResponse> createTask(ServerRequest request) {

    final Mono<Task> task = request.bodyToMono(Task.class);

    return
         task
              .flatMap(IProjectService::createTask)
              .flatMap(data ->
                            ServerResponse.ok()
                                          .contentType(JSON)
                                          .bodyValue(data));
  }


  public Mono<ServerResponse> findAll(ServerRequest request) {
    return ServerResponse.ok()
                         .contentType(JSON)
                         .body(IProjectService.findAll(),Project.class);

  }


  public Mono<ServerResponse> findById(ServerRequest request) {

    String id = request.pathVariable("id");

    return IProjectService.findById(id)
                          .flatMap(data -> ServerResponse.ok()
                                                         .contentType(JSON)
                                                         .bodyValue(data))
                          .switchIfEmpty(ServerResponse.notFound()
                                                       .build());

  }


  public Mono<ServerResponse> delete(ServerRequest request) {

    String id = request.pathVariable("id");

    return ok()

         .contentType(JSON)

         .body(IProjectService.deleteById(id),Void.class)
         .log();

  }


  public Mono<ServerResponse> findByName(ServerRequest request) {
    String name = request.queryParam("name")
                         .get();

    return ok()

         .contentType(JSON)

         .body(IProjectService.findByName(name),Project.class)
         .log();
  }


  public Mono<ServerResponse> findByNameNot(ServerRequest request) {
    String name = request.queryParam("name")
                         .get();

    return ok()

         .contentType(JSON)

         .body(IProjectService.findByNameNot(name),Project.class)
         .log();
  }


  public Mono<ServerResponse> findByEstimCostGreatThan(ServerRequest request) {
    String cost = request.queryParam("cost")
                         .get();

    return ok()

         .contentType(JSON)

         .body(IProjectService.findByEstimatedCostGreaterThan(Long.valueOf(cost)),Project.class)
         .log();
  }


  public Mono<ServerResponse> findByEstimatedCostBetween(ServerRequest request) {
    String from = request.queryParam("from")
                         .get();
    String to = request.queryParam("to")
                       .get();
    return ok()

         .contentType(JSON)

         .body(IProjectService.findByEstimatedCostBetween(Long.valueOf(from),Long.valueOf(to)),
               Project.class
              )
         .log();
  }


  public Mono<ServerResponse> findByNameLike(ServerRequest request) {
    String name = request.queryParam("name")
                         .get();

    return ok()

         .contentType(JSON)

         .body(IProjectService.findByNameLike(name),Project.class)
         .log();
  }


  public Mono<ServerResponse> findByNameRegex(ServerRequest request) {
    String name = request.queryParam("name")
                         .get();
    String regex = "^" + name + "";
    return ok()

         .contentType(JSON)

         .body(IProjectService.findByNameRegex(regex),Project.class)
         .log();
  }


  public Mono<ServerResponse> findByNameQuery(ServerRequest request) {
    String name = request.queryParam("name")
                         .get();

    return ok()

         .contentType(JSON)

         .body(IProjectService.findProjectByNameQuery(name),Project.class)
         .log();
  }


  public Mono<ServerResponse> findByNameAndCostQuery(ServerRequest request) {
    String name = request.queryParam("name")
                         .get();
    String cost = request.queryParam("cost")
                         .get();
    return ok()

         .contentType(JSON)

         .body(IProjectService.findProjectByNameAndCostQuery(name,Long.valueOf(cost)),Project.class)
         .log();
  }


  public Mono<ServerResponse> findByEstCostBetwQuery(ServerRequest request) {
    String from = request.queryParam("from")
                         .get();
    String to = request.queryParam("to")
                       .get();
    return ok()

         .contentType(JSON)

         .body(IProjectService.findByEstimatedCostBetweenQuery(Long.valueOf(from),Long.valueOf(to)),
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

         .body(IProjectService.findByNameRegexQuery(regex),Project.class)
         .log();
  }


  public Mono<ServerResponse> findProjByNameQueryTempl(ServerRequest request) {
    String name = request.queryParam("name")
                         .get();
    return ok()

         .contentType(JSON)

         .body(IProjectService.findProjectByNameQueryWithTemplate(name),Project.class)
         .log();
  }


  public Mono<ServerResponse> findByEstCostBetQueryTempl(ServerRequest request) {
    String from = request.queryParam("from")
                         .get();
    String to = request.queryParam("to")
                       .get();
    return ok()

         .contentType(JSON)

         .body(IProjectService.findByEstimatedCostBetweenQueryWithTemplate(Long.parseLong(from),
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

         .body(IProjectService.findByNameRegexQueryWithTemplate(regex),Project.class)
         .log();


  }


  public Mono<ServerResponse> upsertCostWithCritTempl(ServerRequest request) {
    String id = request.queryParam("id")
                       .get();
    String cost = request.queryParam("cost")
                         .get();
    return ok()

         .contentType(JSON)

         .body(IProjectService.upsertCostWithCriteriaTemplate(id,Long.valueOf(cost)),Void.class)
         .log();


  }


  public Mono<ServerResponse> deleteCriteriaTempl(ServerRequest request) {
    String id = request.queryParam("id")
                       .get();

    return ok()

         .contentType(JSON)

         .body(IProjectService.deleteWithCriteriaTemplate(id),Void.class)
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