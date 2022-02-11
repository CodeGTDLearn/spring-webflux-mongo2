package com.webflux.api.modules.project.resource;

import com.webflux.api.modules.project.core.exceptions.ProjectExceptionsThrower;
import com.webflux.api.modules.project.entity.Project;
import com.webflux.api.modules.project.service.template.IServiceTransaction;
import com.webflux.api.modules.task.Task;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

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
@AllArgsConstructor
@RequestMapping(REPO_ROOT_TRANSACT)
public class ResourceTransaction {

  private final MediaType JSON = MediaType.APPLICATION_JSON;

  IServiceTransaction serviceTransaction;
  private final ProjectExceptionsThrower projectExceptionsThrower;

  @PostMapping(REPO_TRANSACT)
  @ResponseStatus(CREATED)
  public Mono<Void> saveProjectAndTask(Mono<Project> projectMono,
                                       Mono<Task> taskMono){

    return
         serviceTransaction
              .saveProjectAndTask(projectMono, taskMono)
//              .switchIfEmpty(projectExceptionsThrower.throwProjectNotFoundException())
         ;
  }

}