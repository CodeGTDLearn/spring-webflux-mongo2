package com.webflux.api.modules.project.resource.template;

import com.webflux.api.modules.project.dto.ResultProjectTasks;
import com.webflux.api.modules.project.service.template.IServiceLookupProjection;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import static com.webflux.api.core.routes.modules.project.template.RoutesLookupProjection.*;
import static org.springframework.http.HttpStatus.OK;

@RestController
@AllArgsConstructor
@RequestMapping(TEMPL_ROOT_LOOKUP)
public class ResourceLookupProjection {

  private final MediaType JSON = MediaType.APPLICATION_JSON;

  IServiceLookupProjection serviceLookupProjection;

  @PostMapping(TEMPL_LOOKUP_PROJ)
  @ResponseStatus(OK)
  public Flux<ResultProjectTasks> findAllProjectTasks() {

    return
         serviceLookupProjection
              .findAllProjectTasks()
         ;

  }


}