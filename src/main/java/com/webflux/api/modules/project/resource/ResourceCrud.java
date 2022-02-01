package com.webflux.api.modules.project.resource;

import com.webflux.api.modules.project.core.exceptions.ProjectExceptionsThrower;
import com.webflux.api.modules.project.entity.Project;
import com.webflux.api.modules.project.service.IServiceCrud;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.webflux.api.modules.project.core.routes.RoutesCrud.*;
import static org.springframework.http.HttpStatus.*;

// HANDLER: Manage HTTP(Resquests/responses)
//HANDLER:
// A) HANDLER receive the message from ROUTERED
// B) and, send this message for SERVICE
@RestController
@AllArgsConstructor
@RequestMapping(PROJ_ROOT_CRUD)
public class ResourceCrud {

  private final MediaType JSON = MediaType.APPLICATION_JSON;
  private final ProjectExceptionsThrower projectExceptionsThrower;
  private IServiceCrud serviceCrud;

  @PostMapping(CRUD_CREATE)
  @ResponseStatus(CREATED)
  public Mono<Project> save(@RequestBody Project project) {

    return
         serviceCrud
              .save(project)
              .switchIfEmpty(projectExceptionsThrower.projectNameEmptyMessage())
         ;

    //              .onErrorResume(error -> {
    //                if (error instanceof OptimisticLockingFailureException) {
    //                  return ServerResponse.status(BAD_REQUEST)
    //                                       .build();
    //                }
    //                return ServerResponse.status(INTERNAL_SERVER_ERROR)
    //                                     .build();
    //              })

  }

  @PutMapping(CRUD_UPD)
  @ResponseStatus(OK)
  public Mono<Project> update(@RequestBody Project project) {

    return
         serviceCrud
              .findById(project.get_id())
              .switchIfEmpty(projectExceptionsThrower.projectNameEmptyMessage())
              .then(serviceCrud.update(project))
         ;
  }

  @GetMapping(CRUD_FINDALL)
  @ResponseStatus(OK)
  public Flux<Project> findAll() {

    return serviceCrud.findAll();
  }

  @GetMapping(CRUD_ID)
  @ResponseStatus(OK)
  public Mono<Project> findById(@PathVariable String projectId) {

    return
         serviceCrud
              .findById(projectId)
              .switchIfEmpty(projectExceptionsThrower.projectNotFoundException())
         ;
  }

  @DeleteMapping(CRUD_ID)
  @ResponseStatus(NO_CONTENT)
  public Mono<Void> delete(@PathVariable String projectId) {

    return
         serviceCrud
              .findById(projectId)
              .switchIfEmpty(projectExceptionsThrower.projectNotFoundException())
              .flatMap(item -> serviceCrud.deleteById(item.get_id()));
  }

  @GetMapping(CRUD_BYNAME)
  @ResponseStatus(OK)
  public Flux<Project> findByName(@RequestParam String projectName) {

    return
         serviceCrud
              .findByName(projectName)
              .switchIfEmpty(projectExceptionsThrower.projectNotFoundException())
              ;
  }
}