package com.webflux.api.modules.project.resource;

import com.webflux.api.modules.project.core.exceptions.ProjectExceptionsThrower;
import com.webflux.api.modules.project.entity.Project;
import com.webflux.api.modules.project.service.IServiceTransaction;
import com.webflux.api.modules.task.Task;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

import static com.webflux.api.modules.project.core.routes.RoutesTransaction.REPO_ROOT_TRANSACT;
import static com.webflux.api.modules.project.core.routes.RoutesTransaction.REPO_TRANSACT;
import static org.springframework.http.HttpStatus.CREATED;

// ==> EXCEPTIONS IN CONTROLLER:
// *** REASON: IN WEBFLUX, EXCEPTIONS MUST BE IN CONTROLLER - WHY?
//     - "Como stream pode ser manipulado por diferentes grupos de thread,
//     - caso um erro aconteça em uma thread que não é a que operou a controller,
//     - o ControllerAdvice não vai ser notificado "
//     - https://medium.com/nstech/programa%C3%A7%C3%A3o-reativa-com-spring-boot-webflux-e-mongodb-chega-de-sofrer-f92fb64517c3
@RestController
@RequestMapping(REPO_ROOT_TRANSACT)
@AllArgsConstructor
public class ResourceTransaction {

  private final MediaType JSON = MediaType.APPLICATION_JSON;
  private final ProjectExceptionsThrower projectExceptionsThrower;
  private IServiceTransaction serviceTransaction;

  @PostMapping(REPO_TRANSACT)
  @ResponseStatus(CREATED)
  @Transactional
  public Mono<Void> saveTransaction(
       @Valid @RequestBody Project project,
       @Valid @RequestBody Task task) {

    return
         serviceTransaction
              .saveProjectAndTask(project, task)
              .switchIfEmpty(projectExceptionsThrower.throwProjectNameIsEmptyException())
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

}