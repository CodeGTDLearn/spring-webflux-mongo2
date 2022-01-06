package com.webflux.api.modules.project.resource;

import com.webflux.api.core.exception.modules.project.ProjectExceptions;
import com.webflux.api.modules.project.entity.Project;
import com.webflux.api.modules.project.service.IServiceRepo;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import static com.webflux.api.core.routes.modules.project.RoutesRepo.*;
import static org.springframework.http.HttpStatus.OK;

@RestController
@AllArgsConstructor
@RequestMapping(REPO_ROOT)
public class ResourceRepo {

  private final MediaType JSON = MediaType.APPLICATION_JSON;

  private final ProjectExceptions projectExceptions;
  private final IServiceRepo serviceRepo;

  @GetMapping(REPO_BYNAME_NOT)
  @ResponseStatus(OK)
  Flux<Project> findByNameNot(@RequestParam String name) {

    return
         serviceRepo
              .findByNameNot(name)
              .switchIfEmpty(projectExceptions.projectNotFoundException())
         ;
  }

  @GetMapping(REPO_COST_GREATER)
  @ResponseStatus(OK)
  public Flux<Project> findByEstimCostGreatThan(@RequestParam Long cost) {

    return
         serviceRepo
              .findByEstimatedCostGreaterThan(cost)
              .switchIfEmpty(projectExceptions.projectNotFoundException())
         ;
  }

  @GetMapping(REPO_COST_BETW)
  @ResponseStatus(OK)
  public Flux<Project> findByEstimatedCostBetween(
       @RequestParam Long from, @RequestParam Long to) {

    return
         serviceRepo
              .findByEstimatedCostBetween(from, to)
              .switchIfEmpty(projectExceptions.projectNotFoundException())
         ;
  }

  @GetMapping(REPO_BYNAME_LIKE)
  @ResponseStatus(OK)
  public Flux<Project> findByNameLike(@RequestParam String name) {

    return
         serviceRepo
              .findByNameLike(name)
              .switchIfEmpty(projectExceptions.projectNotFoundException())
         ;
  }

  @GetMapping(REPO_BYNAME_REGEX)
  @ResponseStatus(OK)
  public Flux<Project> findByNameRegex(@RequestParam String name) {

    return
         serviceRepo
              .findByNameRegex(name)
              .switchIfEmpty(projectExceptions.projectNotFoundException())
         ;
  }


  /*╔══════════════════════════════╗
    ║        ANNOTATED-QUERY       ║
    ╚══════════════════════════════╝*/
  @GetMapping(REPO_QUERY_BYNAME)
  @ResponseStatus(OK)
  public Flux<Project> findProjectByNameQuery(@RequestParam String name) {

    return
         serviceRepo
              .findProjectByNameQuery(name)
              .switchIfEmpty(projectExceptions.projectNotFoundException())
         ;
  }

  @GetMapping(REPO_QUERY_BYNAME_COST)
  @ResponseStatus(OK)
  public Flux<Project> findProjectByNameAndCostQuery(
       @RequestParam String name, @RequestParam Long cost) {

    return
         serviceRepo
              .findProjectByNameAndCostQuery(name, cost)
              .switchIfEmpty(projectExceptions.projectNotFoundException())
         ;
  }

  @GetMapping(REPO_QUERY_EST_COST_BET)
  @ResponseStatus(OK)
  public Flux<Project> findByEstimatedCostBetweenQuery(
       @RequestParam Long from, @RequestParam Long to) {

    return
         serviceRepo
              .findByEstimatedCostBetweenQuery(from, to)
              .switchIfEmpty(projectExceptions.projectNotFoundException())
         ;
  }

  @GetMapping(REPO_QUERY_NYNAME_REGEX)
  @ResponseStatus(OK)
  public Flux<Project> findByNameRegexQuery(@RequestParam String name) {

    return
         serviceRepo
              .findByNameRegexQuery(name)
              .switchIfEmpty(projectExceptions.projectNotFoundException())
         ;
  }

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
  //                        .orElseThrow();
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
  //                        .orElseThrow();
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