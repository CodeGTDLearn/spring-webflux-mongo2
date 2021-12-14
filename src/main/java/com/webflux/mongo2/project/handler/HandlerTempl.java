package com.webflux.mongo2.project.handler;

import com.webflux.mongo2.project.Project;
import com.webflux.mongo2.project.service.IServiceTempl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

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

  public Mono<ServerResponse> findProjByNameQueryTempl(ServerRequest request) {

    String name = request.queryParam("name")
                         .get();
    return ok()

         .contentType(JSON)

         .body(serviceTempl.findProjectByNameQueryWithTemplate(name), Project.class)
         .log();
  }


  public Mono<ServerResponse> findByEstCostBetQueryTempl(ServerRequest request) {

    String from = request.queryParam("from")
                         .get();
    String to = request.queryParam("to")
                       .get();
    return ok()

         .contentType(JSON)

         .body(serviceTempl.findByEstimatedCostBetweenQueryWithTemplate(Long.parseLong(from),
                                                                        Long.parseLong(to)
                                                                       ),
               Project.class
              )
         .log();


  }


  public Mono<ServerResponse> findByNameRegexQueryTempl(ServerRequest request) {

    String name = request.queryParam("name")
                         .get();
    String regex = "^" + name + "";
    return ok()

         .contentType(JSON)

         .body(serviceTempl.findByNameRegexQueryWithTemplate(regex), Project.class)
         .log();


  }


  public Mono<ServerResponse> upsertCostWithCritTempl(ServerRequest request) {

    String id = request.queryParam("id")
                       .get();

    String cost = request.queryParam("cost")
                         .get();

    return ok()

         .contentType(JSON)

         .body(serviceTempl.upsertCostWithCriteriaTemplate(id, Long.valueOf(cost)),
               Void.class
              )
         .log();


  }


  public Mono<ServerResponse> deleteCriteriaTempl(ServerRequest request) {

    String id = request.queryParam("id")
                       .get();

    return ok()

         .contentType(JSON)

         .body(serviceTempl.deleteWithCriteriaTemplate(id), Void.class)
         .log();


  }
}