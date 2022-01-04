package com.webflux.mongo2.project.handler.template;

import com.webflux.mongo2.project.dto.ResultByStartDateAndCost;
import com.webflux.mongo2.project.dto.ResultCount;
import com.webflux.mongo2.project.service.template.IServiceAggreg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.util.annotation.NonNull;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

// HANDLER: Manage HTTP(Resquests/responses)
//HANDLER:
// A) HANDLER receive the message from ROUTERED
// B) and, send this message for SERVICE
@Component("handlerAggreg")
public class HandlerAggreg {

  private final MediaType JSON = MediaType.APPLICATION_JSON;

  @Autowired
  IServiceAggreg serviceAggreg;

  @NonNull
  public Mono<ServerResponse> findNoOfProjectsCostGreaterThan(ServerRequest request) {

    String cost = request.queryParam("cost")
                         .orElseThrow();
    return ok()
         .contentType(JSON)
         .body(serviceAggreg.findNoOfProjectsCostGreaterThan(Long.valueOf(cost)), ResultCount.class)
         .log();

  }


  @NonNull
  public Mono<ServerResponse> findCostsGroupByStartDateForProjectsCostGreaterThan
       (ServerRequest request) {

    String cost = request.queryParam("cost")
                         .orElseThrow();
    return ok()
         .contentType(JSON)
         .body(serviceAggreg.findCostsGroupByStartDateForProjectsCostGreaterThan(
              Long.valueOf(cost)),
               ResultByStartDateAndCost.class)
         .log();

  }
}