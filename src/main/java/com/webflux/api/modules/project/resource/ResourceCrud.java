package com.webflux.api.modules.project.resource;

import com.webflux.api.core.exception.GlobalExceptionThrower;
import com.webflux.api.modules.project.core.exceptions.ProjectExceptionsThrower;
import com.webflux.api.modules.project.entity.Project;
import com.webflux.api.modules.project.service.IServiceCrud;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

import static com.webflux.api.modules.project.core.routes.RoutesCrud.*;
import static org.springframework.http.HttpStatus.*;

// ==> EXCEPTIONS IN CONTROLLER:
// *** REASON: IN WEBFLUX, EXCEPTIONS MUST BE IN CONTROLLER - WHY?
//     - "Como stream pode ser manipulado por diferentes grupos de thread,
//     - caso um erro aconteça em uma thread que não é a que operou a controller,
//     - o ControllerAdvice não vai ser notificado "
//     - https://medium.com/nstech/programa%C3%A7%C3%A3o-reativa-com-spring-boot-webflux-e-mongodb-chega-de-sofrer-f92fb64517c3
@RestController
@AllArgsConstructor
@RequestMapping(PROJ_ROOT_CRUD)
public class ResourceCrud {

  private final MediaType JSON = MediaType.APPLICATION_JSON;
  private final ProjectExceptionsThrower exceptions;
  private final GlobalExceptionThrower globalException;
  private IServiceCrud serviceCrud;

  @PostMapping(CRUD_SAVE)
  @ResponseStatus(CREATED)
  public Mono<Project> save(@Valid @RequestBody Project project) {

    return
         serviceCrud
              .save(project)
              .switchIfEmpty(exceptions.throwProjectNameEmptyException())
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
              .switchIfEmpty(exceptions.throwProjectNameEmptyException())
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
              .switchIfEmpty(globalException.throwGlobalException())
         ;
  }

  @DeleteMapping(CRUD_ID)
  @ResponseStatus(NO_CONTENT)
  public Mono<Void> delete(@PathVariable String projectId) {

    return
         serviceCrud
              .findById(projectId)
              .switchIfEmpty(exceptions.throwProjectNotFoundException())
              .flatMap(item -> serviceCrud.deleteById(item.get_id()));
  }

  @GetMapping(CRUD_BYNAME)
  @ResponseStatus(OK)
  public Flux<Project> findByName(@RequestParam String projectName) {

    return
         serviceCrud
              .findByName(projectName)
              .switchIfEmpty(exceptions.throwProjectNotFoundException())
              ;
  }

}