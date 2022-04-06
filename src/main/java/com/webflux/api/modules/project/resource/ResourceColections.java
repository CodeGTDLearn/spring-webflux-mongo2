package com.webflux.api.modules.project.resource;

import com.webflux.api.modules.project.core.exceptions.ProjectExceptionsThrower;
import com.webflux.api.modules.project.entity.ProjectChild;
import com.webflux.api.modules.project.service.IServiceColections;
import com.webflux.api.modules.task.entity.Task;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.webflux.api.modules.project.core.routes.template.RoutesColections.*;
import static org.springframework.http.HttpStatus.OK;

// ==> EXCEPTIONS IN CONTROLLER:
// *** REASON: IN WEBFLUX, EXCEPTIONS MUST BE IN CONTROLLER - WHY?
//     - "Como stream pode ser manipulado por diferentes grupos de thread,
//     - caso um erro aconteça em uma thread que não é a que operou a controller,
//     - o ControllerAdvice não vai ser notificado "
//     - https://medium.com/nstech/programa%C3%A7%C3%A3o-reativa-com-spring-boot-webflux-e-mongodb-chega-de-sofrer-f92fb64517c3
@RestController
@AllArgsConstructor
@RequestMapping(TEMPL_ROOT_COL)
public class ResourceColections {


  private final ProjectExceptionsThrower projectExceptionsThrower;
  IServiceColections serviceColections;

  @ResponseStatus(OK)
  @DeleteMapping(TEMPL_DEL_CRIT_MULT_COL)
  public Mono<ProjectChild> DeleteTemplMultCollections(
       @RequestParam String projectId,
       @RequestParam String taskIdToDelete) {

    return serviceColections.DeleteTemplMultCollections(projectId, taskIdToDelete);
  }

  @PostMapping(TEMPL_ADD_CRIT_MULT_COL)
  @ResponseStatus(OK)
  public Mono<ProjectChild> addTemplMultCollections(@RequestBody Mono<Task> task) {

    return serviceColections.addTemplMultCollections(task);
  }

  @PutMapping(TEMPL_UPD_CRIT_MULT_COL)
  @ResponseStatus(OK)
  public Mono<ProjectChild> updateTemplMultCollections(@RequestBody Mono<Task> task) {

    return serviceColections.updateTemplMultCollections(task);
  }

  @DeleteMapping(TEMPL_CLEAN_DB_CRIT_COL)
  @ResponseStatus(OK)
  public Mono<Void> deleteAllCollectionsTemplate() {

    return serviceColections.deleteAllCollectionsTemplate();
  }

  @GetMapping(TEMPL_CHECK_DB_CRIT_COL)
  @ResponseStatus(OK)
  public Flux<String> checkCollectionsTemplate() {

    return serviceColections.checkCollectionsTemplate();
  }

}