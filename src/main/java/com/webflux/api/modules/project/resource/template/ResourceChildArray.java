package com.webflux.api.modules.project.resource.template;

import com.webflux.api.core.exception.modules.project.ProjectExceptions;
import com.webflux.api.modules.project.entity.Project;
import com.webflux.api.modules.project.entity.ProjectChild;
import com.webflux.api.modules.project.service.template.IServiceChildArray;
import com.webflux.api.modules.task.Task;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.webflux.api.core.routes.modules.project.template.RoutesAggreg.TEMPL_AGGREG_DATE;
import static com.webflux.api.core.routes.modules.project.template.RoutesChildArray.*;
import static org.springframework.http.HttpStatus.OK;

@RestController
@AllArgsConstructor
@RequestMapping(TEMPL_ROOT_CHILD)
public class ResourceChildArray {

  private final MediaType JSON = MediaType.APPLICATION_JSON;

  private final IServiceChildArray serviceChildArray;

  private final ProjectExceptions projectExceptions;

  @PutMapping(TEMPL_ADD_ARRAY_CRIT)
  @ResponseStatus(OK)
  public Mono<Project> AddCritTemplArray(@RequestParam String id,
                                         @RequestParam String country) {

    return
         serviceChildArray
              .AddCritTemplArray(id, country)
              .switchIfEmpty(projectExceptions.projectNotFoundException())
         ;


  }

  @PutMapping(TEMPL_UPD_ARRAY_CRIT)
  @ResponseStatus(OK)
  public Mono<Project> updateCritTemplArray(@RequestParam String id,
                                            @RequestParam String country,
                                            @RequestParam String newcountry) {

    return
         serviceChildArray
              .updateCritTemplArray(id, country, newcountry)
              .switchIfEmpty(projectExceptions.projectNotFoundException())
         ;


  }

  @DeleteMapping(TEMPL_DEL_ARRAY_CRIT)
  @ResponseStatus(OK)
  public Mono<Project> DeleteCritTemplArray(@RequestParam String id,
                                            @RequestParam String country) {

    return
         serviceChildArray
              .DeleteCritTemplArray(id, country)
              .switchIfEmpty(projectExceptions.projectNotFoundException())
         ;


  }


  @PutMapping(TEMPL_ADD_CHILD_CRIT)
  @ResponseStatus(OK)
  public Mono<ProjectChild> AddCritTemplChild(@RequestParam String id,
                                              Mono<Task> task) {

    return
         serviceChildArray
              .AddCritTemplChild(id, task)
              .switchIfEmpty(projectExceptions.projectNotFoundException())
         ;

  }

  @PutMapping(TEMPL_UPD_CHILD_CRIT)
  @ResponseStatus(OK)
  public Mono<ProjectChild> UpdateCritTemplChild(@RequestParam String id,
                                                 @RequestParam String idch,
                                                 @RequestParam String ownername) {

    return
         serviceChildArray
              .UpdateCritTemplChild(id, idch, ownername)
              .switchIfEmpty(projectExceptions.projectNotFoundException())
         ;


  }

  @DeleteMapping(TEMPL_AGGREG_DATE)
  @ResponseStatus(OK)
  public Flux<ProjectChild> DeleteCritTemplChild(
       @RequestParam String id, @RequestParam String idch) {

    return
         serviceChildArray
              .DeleteCritTemplChild(id, idch)
              .switchIfEmpty(projectExceptions.projectNotFoundException())
         ;
  }

}