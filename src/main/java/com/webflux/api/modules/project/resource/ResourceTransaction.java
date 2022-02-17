package com.webflux.api.modules.project.resource;

import com.webflux.api.modules.project.core.exceptions.ProjectExceptionsThrower;
import com.webflux.api.modules.project.core.exceptions.types.ProjectNameIsEmptyException;
import com.webflux.api.modules.project.core.exceptions.types.ProjectNotFoundException;
import com.webflux.api.modules.project.entity.Project;
import com.webflux.api.modules.project.service.IServiceTransaction;
import com.webflux.api.modules.task.Task;
import com.webflux.api.modules.task.core.exceptions.TaskExceptionsThrower;
import com.webflux.api.modules.task.core.exceptions.types.TaskNameIsEmptyException;
import com.webflux.api.modules.task.core.exceptions.types.TaskNameLessThanThreeException;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import static com.webflux.api.modules.project.core.routes.RoutesTransaction.REPO_ROOT_TRANSACT;
import static com.webflux.api.modules.project.core.routes.RoutesTransaction.REPO_TRANSACT;
import static config.databuilders.TaskBuilder.taskNoID;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;

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

  private final ProjectExceptionsThrower projectThrower;
  private final TaskExceptionsThrower taskThrower;
  private IServiceTransaction serviceTransaction;


  @PostMapping(REPO_TRANSACT)
  @ResponseStatus(CREATED)
  public Mono<Task> createProjectTransaction(
       @RequestParam
//       @NotEmpty
//       @NotNull
            String taskNameInitial,
//       @Valid
       @RequestBody
            Project project
       ) {

    Task initialTask = taskNoID("3",
                                "Mark",
                                1000L
                               ).create();

    initialTask.setName(taskNameInitial);

    // @formatter:off
    return
         serviceTransaction
          .createProjectTransaction(project, initialTask)
          .onErrorResume(error ->
            switch (error) {
             case ProjectNameIsEmptyException ignored -> projectThrower.throwProjectNameIsEmptyException();
             case ProjectNotFoundException ignored -> projectThrower.throwProjectNotFoundException();
             case TaskNameIsEmptyException ignored -> taskThrower.throwTaskNameIsEmptyException();
             case TaskNameLessThanThreeException ignored -> taskThrower.throwTaskNameLessThanThreeException();
             default -> Mono.error(new ResponseStatusException(NOT_FOUND));
          })
         ;
    // @formatter:on
  }
}