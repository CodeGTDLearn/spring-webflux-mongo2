package com.webflux.api.modules.project.resource.template;

import com.webflux.api.modules.project.core.exceptions.ProjectExceptionsThrower;
import com.webflux.api.modules.project.entity.Project;
import com.webflux.api.modules.project.service.template.IServiceTempl;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.webflux.api.modules.project.core.routes.template.RoutesTempl.*;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

// ==> EXCEPTIONS IN CONTROLLER:
// *** REASON: IN WEBFLUX, EXCEPTIONS MUST BE IN CONTROLLER - WHY?
//     - "Como stream pode ser manipulado por diferentes grupos de thread,
//     - caso um erro aconteça em uma thread que não é a que operou a controller,
//     - o ControllerAdvice não vai ser notificado "
//     - https://medium.com/nstech/programa%C3%A7%C3%A3o-reativa-com-spring-boot-webflux-e-mongodb-chega-de-sofrer-f92fb64517c3
@RestController
@AllArgsConstructor
@RequestMapping(TEMPL_ROOT)
public class ResourceTempl {

  private final MediaType JSON = MediaType.APPLICATION_JSON;
  private final ProjectExceptionsThrower projectExceptionsThrower;
  IServiceTempl serviceTempl;

  @GetMapping(TEMPL_BYNAME)
  @ResponseStatus(OK)
  public Flux<Project> findProjectByNameQueryWithCriteriaTemplate(
       @RequestParam String projectName) {

    return
         serviceTempl
              .findProjectByNameQueryWithCriteriaTemplate(projectName)
//              .switchIfEmpty(projectExceptionsThrower.throwProjectNotFoundException())
         ;
  }

  @GetMapping(TEMPL_EST_COST_BET)
  @ResponseStatus(OK)
  public Flux<Project> findByEstimatedCostBetweenQueryWithCriteriaTemplate(
  @RequestParam Long projectCostFrom,
  @RequestParam Long projectCostTo) {

    return
         serviceTempl
              .findByEstimatedCostBetweenQueryWithCriteriaTemplate(projectCostFrom, projectCostTo)
//              .switchIfEmpty(projectExceptionsThrower.throwProjectNotFoundException())
         ;


  }

  @GetMapping(TEMPL_BYNAME_REG)
  @ResponseStatus(OK)
  public Flux<Project> findByNameRegexQueryWithCriteriaTemplate(
       @RequestParam String regexpProjectName) {

    return
         serviceTempl
              .findByNameRegexQueryWithCriteriaTemplate(regexpProjectName)
//              .switchIfEmpty(projectExceptionsThrower.throwProjectNotFoundException())
         ;
  }

  @PutMapping(TEMPL_UPSERT_CRIT)
  @ResponseStatus(OK)
  public Mono<Void> UpdateCostWithCritTemplUpsert(
       @RequestParam String projectId,
       @RequestParam Long projectCost) {

    return
         serviceTempl
              .UpdateCostWithCritTemplUpsert(projectId, projectCost)
//              .switchIfEmpty(projectExceptionsThrower.throwProjectNotFoundException())
         ;

  }

  @DeleteMapping(TEMPL_DEL_CRIT)
  @ResponseStatus(NO_CONTENT)
  public Mono<Void> deleteWithCriteriaTemplate(@RequestParam String projectId) {

    return serviceTempl.deleteWithCriteriaTemplate(projectId);


  }
}