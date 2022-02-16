package com.webflux.api.modules.project.resource;

import com.webflux.api.modules.project.core.exceptions.ProjectExceptionsThrower;
import com.webflux.api.modules.project.core.exceptions.types.ProjectNameInvalidException;
import com.webflux.api.modules.project.core.exceptions.types.ProjectNameIsEmptyException;
import com.webflux.api.modules.project.core.exceptions.types.ProjectNotFoundException;
import com.webflux.api.modules.project.entity.Project;
import com.webflux.api.modules.project.service.IServiceCrud;
import com.webflux.api.modules.project.service.IServiceTransaction;
import com.webflux.api.modules.task.Task;
import com.webflux.api.modules.task.core.exceptions.TaskExceptionsThrower;
import com.webflux.api.modules.task.core.exceptions.types.TaskNameIsEmptyException;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

import static com.webflux.api.modules.project.core.routes.RoutesTransaction.REPO_ROOT_TRANSACT;
import static com.webflux.api.modules.project.core.routes.RoutesTransaction.REPO_TRANSACT;
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

  private final MediaType JSON = MediaType.APPLICATION_JSON;
  private final ProjectExceptionsThrower projectExceptionsThrower;
  private final TaskExceptionsThrower taskExceptionsThrower;
  private IServiceTransaction serviceTransaction;
  private IServiceCrud serviceCrud;

  @Transactional
  @PostMapping(REPO_TRANSACT)
  @ResponseStatus(CREATED)
  public Mono<Task> saveProjectAndTaskTransaction(
       @RequestParam String projectId,
       @RequestParam String newProjectName,
       @Valid @RequestBody Task task) {

    // @formatter:off
    return
         serviceCrud
              .findById(projectId)

              // STEP 01 - THROW/BLOW-UP: EXCEPTIONS ARE THROW HERE (ON-ERROR-RESUME)
              .switchIfEmpty(projectExceptionsThrower.throwProjectNotFoundException())
              .flatMap(proj -> {
                if (newProjectName.isEmpty()) return projectExceptionsThrower.throwProjectNameIsEmptyException();
                if (task.getName().length() < 3) return taskExceptionsThrower.throwTaskNameIsEmptyException();
                proj.setName(newProjectName);
                return serviceTransaction.saveProjectAndTaskTransaction(proj, task);
              })

              // STEP 02 - HANDLE/MANAGING: EXCEPTIONS ARE HANDLED HERE (ON-ERROR-RESUME)
              .onErrorResume(error -> {
                if (error instanceof ProjectNotFoundException) {
                  return projectExceptionsThrower.throwProjectNotFoundException();
                }
                if (error instanceof ProjectNameIsEmptyException) {
                  return projectExceptionsThrower.throwProjectNameIsEmptyException();
                }
                if (error instanceof TaskNameIsEmptyException) {
                  return taskExceptionsThrower.throwTaskNameIsEmptyException();
                }

                // STEP 03 - BENA-VALIDATIONS: THROW ONLY GLOBAL-EXCEPTION
                return Mono.error(new ResponseStatusException(NOT_FOUND));
              })
         ;
    // @formatter:on
  }

  @Transactional
  @PostMapping(REPO_TRANSACT)
  @ResponseStatus(CREATED)
  public Mono<Task> saveProjectCheckProjectName(@RequestBody Project project) {

    // @formatter:off
    return
         serviceCrud
              .save(project)
              .flatMap(proj -> {
                if (project.getName().length() > 5 ) return projectExceptionsThrower.throwProjectNameInvalidException();
              })
              .onErrorResume(error -> {
                 if (error instanceof ProjectNameInvalidException) {
                  return projectExceptionsThrower.throwProjectNameInvalidException();
                }
                // STEP 03 - BENA-VALIDATIONS: THROW ONLY GLOBAL-EXCEPTION
                return Mono.error(new ResponseStatusException(NOT_FOUND));
              })
         ;
    // @formatter:on
  }
}