package com.webflux.mongo2.project.handler;

import com.webflux.mongo2.project.Project;
import com.webflux.mongo2.project.service.IServiceTemplLecture;
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
@Component("handlerTemplLecture")
public class HandlerTemplLecture {

  private final MediaType JSON = MediaType.APPLICATION_JSON;

  @Autowired
  IServiceTemplLecture serviceTemplLecture;

  @NonNull
  public Mono<ServerResponse> findProjByNameQueryCritTempl(ServerRequest request) {

    String name = request.queryParam("name")
                         .orElseThrow();
    return ok()

         .contentType(JSON)

         .body(serviceTemplLecture.findProjectByNameQueryWithCriteriaTemplate(name), Project.class)
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
              serviceTemplLecture.findByEstimatedCostBetweenQueryWithCriteriaTemplate(
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

         .body(serviceTemplLecture.findByNameRegexQueryWithCriteriaTemplate(regex), Project.class)
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

         .body(serviceTemplLecture.UpdateCostWithCritTemplUpsert(id, Long.valueOf(cost)),
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

         .body(serviceTemplLecture.deleteWithCriteriaTemplate(id), Void.class)
         .log();


  }
}