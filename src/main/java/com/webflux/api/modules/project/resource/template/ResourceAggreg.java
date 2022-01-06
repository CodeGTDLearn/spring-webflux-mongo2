package com.webflux.api.modules.project.resource.template;

import com.webflux.api.core.exception.modules.project.ProjectExceptions;
import com.webflux.api.modules.project.dto.ResultByStartDateAndCost;
import com.webflux.api.modules.project.dto.ResultCount;
import com.webflux.api.modules.project.service.template.IServiceAggreg;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import static com.webflux.api.core.routes.modules.project.template.RoutesAggreg.*;
import static org.springframework.http.HttpStatus.OK;


@RestController
@AllArgsConstructor
@RequestMapping(TEMPL_ROOT_AGGREG)
public class ResourceAggreg {

  private final MediaType JSON = MediaType.APPLICATION_JSON;

  IServiceAggreg serviceAggreg;

  private final ProjectExceptions projectExceptions;

  @GetMapping(TEMPL_AGGREG_NO_GT)
  @ResponseStatus(OK)
  public Flux<ResultCount> findNoOfProjectsCostGreaterThan(@RequestParam Long cost) {

    return
         serviceAggreg
              .findNoOfProjectsCostGreaterThan(cost)
              .switchIfEmpty(projectExceptions.projectNotFoundException())
         ;

  }


  @GetMapping(TEMPL_AGGREG_DATE)
  @ResponseStatus(OK)
  public Flux<ResultByStartDateAndCost> findCostsGroupByStartDateForProjectsCostGreaterThan(@RequestParam Long cost) {

    return
         serviceAggreg
              .findCostsGroupByStartDateForProjectsCostGreaterThan(cost)
              .switchIfEmpty(projectExceptions.projectNotFoundException())
         ;

  }
}