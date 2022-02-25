package com.webflux.api.modules.project.resource;

import com.webflux.api.modules.project.core.dto.ResultProjectTasks;
import com.webflux.api.modules.project.service.IServiceLookupProjection;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import static com.webflux.api.modules.project.core.routes.template.RoutesLookupProjection.TEMPL_LOOKUP_PROJ;
import static com.webflux.api.modules.project.core.routes.template.RoutesLookupProjection.TEMPL_ROOT_LOOKUP;
import static org.springframework.http.HttpStatus.OK;

// ==> EXCEPTIONS IN CONTROLLER:
// *** REASON: IN WEBFLUX, EXCEPTIONS MUST BE IN CONTROLLER - WHY?
//     - "Como stream pode ser manipulado por diferentes grupos de thread,
//     - caso um erro aconteça em uma thread que não é a que operou a controller,
//     - o ControllerAdvice não vai ser notificado "
//     - https://medium.com/nstech/programa%C3%A7%C3%A3o-reativa-com-spring-boot-webflux-e-mongodb-chega-de-sofrer-f92fb64517c3
@RestController
@AllArgsConstructor
@RequestMapping(TEMPL_ROOT_LOOKUP)
public class ResourceLookupProjection {

  private final MediaType JSON = MediaType.APPLICATION_JSON;

  IServiceLookupProjection serviceLookupProjection;

  @GetMapping(TEMPL_LOOKUP_PROJ)
  @ResponseStatus(OK)
  public Flux<ResultProjectTasks> findAllProjectTasks() {

    return
         serviceLookupProjection
              .findAllProjectTasks()
         ;

  }


}