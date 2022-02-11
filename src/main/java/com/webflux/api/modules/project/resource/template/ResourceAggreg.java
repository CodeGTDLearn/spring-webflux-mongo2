package com.webflux.api.modules.project.resource.template;

import com.webflux.api.modules.project.core.exceptions.ProjectExceptionsThrower;
import com.webflux.api.modules.project.core.dto.ResultByStartDateAndCost;
import com.webflux.api.modules.project.core.dto.ResultCount;
import com.webflux.api.modules.project.service.template.IServiceAggreg;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import static com.webflux.api.modules.project.core.routes.template.RoutesAggreg.*;
import static org.springframework.http.HttpStatus.OK;

// ==> EXCEPTIONS IN CONTROLLER:
// *** REASON: IN WEBFLUX, EXCEPTIONS MUST BE IN CONTROLLER - WHY?
//     - "Como stream pode ser manipulado por diferentes grupos de thread,
//     - caso um erro aconteça em uma thread que não é a que operou a controller,
//     - o ControllerAdvice não vai ser notificado "
//     - https://medium.com/nstech/programa%C3%A7%C3%A3o-reativa-com-spring-boot-webflux-e-mongodb-chega-de-sofrer-f92fb64517c3
@RestController
@AllArgsConstructor
@RequestMapping(TEMPL_ROOT_AGGREG)
public class ResourceAggreg {

  private final MediaType JSON = MediaType.APPLICATION_JSON;

  IServiceAggreg serviceAggreg;

  private final ProjectExceptionsThrower projectExceptionsThrower;

  @GetMapping(TEMPL_AGGREG_NO_GT)
  @ResponseStatus(OK)
  public Flux<ResultCount> findNoOfProjectsCostGreaterThan(
       @RequestParam Long projectCost) {

    return
         serviceAggreg
              .findNoOfProjectsCostGreaterThan(projectCost)
//              .switchIfEmpty(projectExceptionsThrower.throwProjectNotFoundException())
         ;

  }


  @GetMapping(TEMPL_AGGREG_DATE)
  @ResponseStatus(OK)
  public Flux<ResultByStartDateAndCost> findCostsGroupByStartDateForProjectsCostGreaterThan(
       @RequestParam Long projectCost) {

    return
         serviceAggreg
              .findCostsGroupByStartDateForProjectsCostGreaterThan(projectCost)
//              .switchIfEmpty(projectExceptionsThrower.throwProjectNotFoundException())
         ;

  }
}