package com.webflux.api.modules.project.resource.template;

import com.webflux.api.core.exception.modules.project.ProjectExceptions;
import com.webflux.api.modules.project.entity.Project;
import com.webflux.api.modules.project.service.template.IServiceTempl;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.webflux.api.core.routes.modules.project.template.RoutesColections.TEMPL_UPD_CRIT_MULT_COL;
import static com.webflux.api.core.routes.modules.project.template.RoutesTempl.*;
import static org.springframework.http.HttpStatus.OK;

@RestController
@AllArgsConstructor
@RequestMapping(TEMPL_ROOT)
public class ResourceTempl {

  private final MediaType JSON = MediaType.APPLICATION_JSON;
  private final ProjectExceptions projectExceptions;
  IServiceTempl serviceTempl;

  @GetMapping(TEMPL_BYNAME)
  @ResponseStatus(OK)
  public Flux<Project> findProjectByNameQueryWithCriteriaTemplate(@RequestParam String name) {

    return
         serviceTempl
              .findProjectByNameQueryWithCriteriaTemplate(name)
              .switchIfEmpty(projectExceptions.projectNotFoundException())
         ;
  }

  @GetMapping(TEMPL_EST_COST_BET)
  @ResponseStatus(OK)
  public Flux<Project> findByEstimatedCostBetweenQueryWithCriteriaTemplate(@RequestParam Long from,
                                                                           @RequestParam Long to) {

    return
         serviceTempl
              .findByEstimatedCostBetweenQueryWithCriteriaTemplate(from, to)
              .switchIfEmpty(projectExceptions.projectNotFoundException())
         ;


  }

  @GetMapping(TEMPL_BYNAME_REG)
  @ResponseStatus(OK)
  public Flux<Project> findByNameRegexQueryWithCriteriaTemplate(@RequestParam String regexp) {

    return
         serviceTempl
              .findByNameRegexQueryWithCriteriaTemplate(regexp)
              .switchIfEmpty(projectExceptions.projectNotFoundException())
         ;
  }

  @PutMapping(TEMPL_UPSERT_CRIT)
  @ResponseStatus(OK)
  public Mono<Void> UpdateCostWithCritTemplUpsert(@RequestParam String id,
                                                  @RequestParam Long cost) {

    return
         serviceTempl
              .UpdateCostWithCritTemplUpsert(id, cost)
              .switchIfEmpty(projectExceptions.projectNotFoundException())
         ;

  }

  @DeleteMapping(TEMPL_UPD_CRIT_MULT_COL)
  @ResponseStatus(OK)
  public Mono<Void> deleteWithCriteriaTemplate(@RequestParam String id) {

    return serviceTempl.deleteWithCriteriaTemplate(id);


  }
}