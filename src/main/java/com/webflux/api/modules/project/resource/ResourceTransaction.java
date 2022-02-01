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
              .switchIfEmpty(projectExceptionsThrower.projectNotFoundException())
         ;
  }

}