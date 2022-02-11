package com.webflux.api.modules.project.resource.template;

import com.webflux.api.modules.project.core.exceptions.ProjectExceptionsThrower;
import com.webflux.api.modules.project.entity.Project;
import com.webflux.api.modules.project.entity.ProjectChild;
import com.webflux.api.modules.project.service.template.IServiceChildArray;
import com.webflux.api.modules.task.Task;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.webflux.api.modules.project.core.routes.template.RoutesChildArray.*;
import static org.springframework.http.HttpStatus.OK;

// ==> EXCEPTIONS IN CONTROLLER:
// *** REASON: IN WEBFLUX, EXCEPTIONS MUST BE IN CONTROLLER - WHY?
//     - "Como stream pode ser manipulado por diferentes grupos de thread,
//     - caso um erro aconteça em uma thread que não é a que operou a controller,
//     - o ControllerAdvice não vai ser notificado "
//     - https://medium.com/nstech/programa%C3%A7%C3%A3o-reativa-com-spring-boot-webflux-e-mongodb-chega-de-sofrer-f92fb64517c3
@RestController
@AllArgsConstructor
@RequestMapping(TEMPL_ROOT_CHILD)
public class ResourceChildArray {

  private final MediaType JSON = MediaType.APPLICATION_JSON;

  private final IServiceChildArray serviceChildArray;

  private final ProjectExceptionsThrower projectExceptionsThrower;

  @PutMapping(TEMPL_ADD_ARRAY_CRIT)
  @ResponseStatus(OK)
  public Mono<Project> AddCritTemplArray(@RequestParam String projectId,
                                         @RequestParam String country) {

    return
         serviceChildArray
              .addCritTemplArray(projectId, country)
//              .switchIfEmpty(projectExceptionsThrower.throwProjectNotFoundException())
         ;


  }

  @PutMapping(TEMPL_UPD_ARRAY_CRIT)
  @ResponseStatus(OK)
  public Mono<Project> updateCritTemplArray(@RequestParam String projectId,
                                            @RequestParam String country,
                                            @RequestParam String newcountry) {

    return
         serviceChildArray
              .updateCritTemplArray(projectId, country, newcountry)
//              .switchIfEmpty(projectExceptionsThrower.throwProjectNotFoundException())
         ;


  }

  @DeleteMapping(TEMPL_DEL_ARRAY_CRIT)
  @ResponseStatus(OK)
  public Mono<Project> DeleteCritTemplArray(@RequestParam String projectId,
                                            @RequestParam String country) {

    return
         serviceChildArray
              .deleteCritTemplArray(projectId, country)
//              .switchIfEmpty(projectExceptionsThrower.throwProjectNotFoundException())
         ;


  }


  @PutMapping(TEMPL_ADD_CHILD_CRIT)
  @ResponseStatus(OK)
  public Mono<ProjectChild> AddCritTemplChild(
       @RequestParam String projectId,
       @RequestBody Mono<Task> task) {

    return
         serviceChildArray
              .addCritTemplChild(projectId, task)
//              .switchIfEmpty(projectExceptionsThrower.throwProjectNotFoundException())
         ;

  }

  @PutMapping(TEMPL_UPD_CHILD_CRIT)
  @ResponseStatus(OK)
  public Mono<ProjectChild> UpdateCritTemplChild(@RequestParam String projectId,
                                                 @RequestParam String taskIdToUpdate,
                                                 @RequestParam String ownername) {

    return
         serviceChildArray
              .updateCritTemplChild(projectId, taskIdToUpdate, ownername)
//              .switchIfEmpty(projectExceptionsThrower.throwProjectNotFoundException())
         ;


  }

  @DeleteMapping(TEMPL_DEL_CHILD_CRIT)
  @ResponseStatus(OK)
  public Flux<ProjectChild> DeleteCritTemplChild(
       @RequestParam String projectId,
       @RequestParam String taskIdtoDelete) {

    return
         serviceChildArray
              .deleteCritTemplChild(projectId, taskIdtoDelete)
//              .switchIfEmpty(projectExceptionsThrower.throwProjectNotFoundException())
         ;
  }

  @GetMapping(TEMPL_CHK_CHILD_EXIST_CRIT)
  @ResponseStatus(OK)
  public Mono<Boolean> existTheTaskInProjectChild(@RequestParam String projectId,
                                                  @RequestParam String idTask) {
    return
         serviceChildArray
              .existTheTaskInProjectChild(projectId, idTask)
//              .switchIfEmpty(projectExceptionsThrower.throwProjectNotFoundException())
         ;


  }

}