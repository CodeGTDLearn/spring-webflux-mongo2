package com.webflux.mongo2.project.handler.template;

import com.webflux.mongo2.project.entity.Project;
import com.webflux.mongo2.project.service.template.IServiceTempl;
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
@Component("handlerTempl")
public class HandlerTempl {

  private final MediaType JSON = MediaType.APPLICATION_JSON;

  @Autowired
  IServiceTempl serviceTempl;

  @NonNull
  public Mono<ServerResponse> findProjByNameQueryCritTempl(ServerRequest request) {

    String name = request.queryParam("name")
                         .orElseThrow();
    return ok()

         .contentType(JSON)

         .body(serviceTempl.findProjectByNameQueryWithCriteriaTemplate(name), Project.class)
         .log();
  }

  @NonNull
  public Mono<ServerResponse> findByEstCostBetQueryCritTempl(ServerRequest request) {

    String from = request.queryParam("from")
                         .orElseThrow();
    String to = request.queryParam("to")
                       .orElseThrow();
    return ok()

         .contentType(JSON)

         .body(
              serviceTempl.findByEstimatedCostBetweenQueryWithCriteriaTemplate(
                   Long.parseLong(from),
                   Long.parseLong(to)
                                                                              ),
              Project.class
              )
         .log();


  }

  @NonNull
  public Mono<ServerResponse> findByNameRegexQueryCritTempl(ServerRequest request) {

    String name = request.queryParam("name")
                         .orElseThrow();

    String regex = "^" + name + "";
    return ok()

         .contentType(JSON)

         .body(serviceTempl.findByNameRegexQueryWithCriteriaTemplate(regex), Project.class)
         .log();
  }

  @NonNull
  public Mono<ServerResponse> UpdateCostWithCritTemplUpsert(ServerRequest request) {

    String id = request.queryParam("id")
                       .orElseThrow();

    String cost = request.queryParam("cost")
                         .orElseThrow();

    return ok()

         .contentType(JSON)

         .body(serviceTempl.UpdateCostWithCritTemplUpsert(id, Long.valueOf(cost)),
               Void.class
              )
         .log();


  }

  @NonNull
  public Mono<ServerResponse> deleteCritTempl(ServerRequest request) {

    String id = request.queryParam("id")
                       .orElseThrow();

    return ok()

         .contentType(JSON)

         .body(serviceTempl.deleteWithCriteriaTemplate(id), Void.class)
         .log();


  }
}