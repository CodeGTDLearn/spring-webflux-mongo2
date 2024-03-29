package com.webflux.api.modules.project.resource;

import com.webflux.api.modules.project.core.exceptions.ProjectExceptionsThrower;
import com.webflux.api.modules.project.core.exceptions.types.ProjectNotFoundException;
import com.webflux.api.modules.project.entity.Project;
import com.webflux.api.modules.project.service.IServiceRepo;
import com.webflux.api.modules.project.service.IServiceTransaction;
import com.webflux.api.modules.task.core.exceptions.TaskExceptionsThrower;
import com.webflux.api.modules.task.core.exceptions.types.TaskNameIsEmptyException;
import com.webflux.api.modules.task.core.exceptions.types.TaskNameLessThanThreeException;
import com.webflux.api.modules.task.entity.Task;
import com.webflux.api.modules.task.repo.ITaskRepo;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

import static com.webflux.api.core.TaskBuilder.taskNoID;
import static com.webflux.api.modules.project.core.routes.RoutesTransaction.*;
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
  private final IServiceRepo serviceRepo;
  private final ITaskRepo taskRepo;
  private final IServiceTransaction serviceTransaction;


  @PostMapping(REPO_TRANSACT_CHK_EXC)
  @ResponseStatus(CREATED)
  public Mono<Task> checkContentWithExc(
       @RequestParam
       String taskNameInitial,

       @Valid
       @RequestBody
       Project project) {

    Task initialTask =
         taskNoID("3",
                  "Mark",
                  1000L
         ).create();

    initialTask.setName(taskNameInitial);

    // @formatter:off
    return
         Mono.just(project)
             .flatMap(proj2 -> {
               if (proj2.getName().isEmpty()) return taskThrower.throwTaskNameIsEmptyException();
               if (proj2.getName().length() < 3) return taskThrower.throwTaskNameLessThanThreeException();
               return Mono.just(proj2);
             })
             .flatMap(proj3 -> serviceTransaction.checkContentWithExc(proj3,initialTask))
         ;
    // @formatter:on
  }

  /*
 ╔════════════════════════════════════════════════════════════════════════════════╗
 ║                          EXCEPTIONS  +  TRANSACTIONS                           ║
 ╠════════════════════════════════════════════════════════════════════════════════╣
 ║ A) SERVICE: TRIGGER THE EXCEPTIONS (NO 'ON-ERROR-RESUME')                      ║
 ║ B) CONTROLLER: CATCH+HANDLE THE EXCEPTIONS TRIGGERED (USING ON-ERROR-RESUME)   ║
 ╚════════════════════════════════════════════════════════════════════════════════╝*/
  @Transactional
  @PostMapping(REPO_TRANSACT_CLASSIC)
  @ResponseStatus(CREATED)
  public Mono<Task> transactionsClassic(
       @RequestParam
       String taskNameInitial,

       @Valid
       @RequestBody Project project) {

    Task task =
         taskNoID("3",
                  "Mark",
                  1000L
         ).create();

    task.setName(taskNameInitial);

    // @formatter:off
        return
             serviceTransaction
                  .transactionsClassic(project, task)

                  // ON-ERROR-RESUME: CATCH+HANDLE THOSE EXCEPTIONS
                  .onErrorResume(error -> {
                                   if (error instanceof ProjectNotFoundException) {
                                     return projectThrower.throwProjectNotFoundException();
                                   }
                                   if (error instanceof TaskNameIsEmptyException) {
                                     return taskThrower.throwTaskNameIsEmptyException();
                                   }
                                   if (error instanceof TaskNameLessThanThreeException) {
                                     return taskThrower.throwTaskNameLessThanThreeException();
                                   }
                                   return Mono.error(new ResponseStatusException(NOT_FOUND));
                                 }
                                )
             ;
    // @formatter:on
  }
}