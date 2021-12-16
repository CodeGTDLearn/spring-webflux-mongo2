package com.webflux.mongo2.project.handler;

import com.webflux.mongo2.project.Project;
import com.webflux.mongo2.project.service.IServiceRepo;
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
@Component("handlerRepo")
public class HandlerRepo {

  private final MediaType JSON = MediaType.APPLICATION_JSON;

  @Autowired
  IServiceRepo serviceRepo;

  public Mono<ServerResponse> findByNameNot(ServerRequest request) {
    String name = request.queryParam("name")
                         .get();

    return ok()
         .contentType(JSON)
         .body(
              serviceRepo.findByNameNot(name),
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
              serviceRepo.
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
         .body(serviceRepo.
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
         .body(serviceRepo.findByNameLike(name), Project.class)
         .log();
  }


  public Mono<ServerResponse> findByNameRegex(ServerRequest request) {
    String name = request.queryParam("name")
                         .get();

    String regex = "^" + name + "";

    return ok()
         .contentType(JSON)
         .body(serviceRepo.findByNameRegex(regex), Project.class)
         .log();
  }


  /*╔══════════════════════════════╗
    ║        ANNOTATED-QUERY       ║
    ╚══════════════════════════════╝*/
  public Mono<ServerResponse> findByNameQuery(ServerRequest request) {
    String name = request.queryParam("name")
                         .get();

    return ok()
         .contentType(JSON)
         .body(serviceRepo.findProjectByNameQuery(name), Project.class)
         .log();
  }


  public Mono<ServerResponse> findByNameAndCostQuery(ServerRequest request) {
    String name = request.queryParam("name")
                         .get();
    String cost = request.queryParam("cost")
                         .get();

    return ok()
         .contentType(JSON)
         .body(serviceRepo.findProjectByNameAndCostQuery(name, Long.valueOf(cost)),
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

         .body(serviceRepo.findByEstimatedCostBetweenQuery(Long.valueOf(from), Long.valueOf(to)),
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

         .body(serviceRepo.findByNameRegexQuery(regex), Project.class)
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