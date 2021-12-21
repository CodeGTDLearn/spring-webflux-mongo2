package com.webflux.mongo2.project.handler;

import com.webflux.mongo2.project.Project;
import com.webflux.mongo2.project.ProjectChild;
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

  public Mono<ServerResponse> findProjByNameQueryCritTempl(ServerRequest request) {

    String name = request.queryParam("name")
                         .get();
    return ok()

         .contentType(JSON)

         .body(serviceTempl.findProjectByNameQueryWithCriteriaTemplate(name), Project.class)
         .log();
  }

  public Mono<ServerResponse> findByEstCostBetQueryCritTempl(ServerRequest request) {

    String from = request.queryParam("from")
                         .get();
    String to = request.queryParam("to")
                       .get();
    return ok()

         .contentType(JSON)

         .body(
              serviceTempl.findByEstimatedCostBetweenQueryWithCriteriaTemplate(Long.parseLong(from),
                                                                               Long.parseLong(to)
                                                                              ),
              Project.class
              )
         .log();


  }

  public Mono<ServerResponse> findByNameRegexQueryCritTempl(ServerRequest request) {

    String name = request.queryParam("name")
                         .get();

    String regex = "^" + name + "";
    return ok()

         .contentType(JSON)

         .body(serviceTempl.findByNameRegexQueryWithCriteriaTemplate(regex), Project.class)
         .log();
  }

  public Mono<ServerResponse> UpdateCostWithCritTemplUpsert(ServerRequest request) {

    String id = request.queryParam("id")
                       .get();

    String cost = request.queryParam("cost")
                         .get();

    return ok()

         .contentType(JSON)

         .body(serviceTempl.UpdateCostWithCritTemplUpsert(id, Long.valueOf(cost)),
               Void.class
              )
         .log();


  }

  public Mono<ServerResponse> UpdateCountryListWithCritTemplUpsertArray(ServerRequest request) {

    String id = request.queryParam("id")
                       .get();

    String country = request.queryParam("country")
                            .get();

    return ok()

         .contentType(JSON)

         .body(serviceTempl.UpdateCountryListWithCritTemplUpsertArray(id, country),
               Project.class
              )
         .log();


  }

  public Mono<ServerResponse> UpdateCountryListWithCritTemplUpsertChild(ServerRequest request) {

    String id = request.queryParam("id")
                       .get();

    String ownername = request.queryParam("ownername")
                            .get();

    return ok()

         .contentType(JSON)

         .body(serviceTempl.UpdateCountryListWithCritTemplUpsertChild(id, ownername),
               ProjectChild.class
              )
         .log();


  }

  public Mono<ServerResponse> deleteCritTempl(ServerRequest request) {

    String id = request.queryParam("id")
                       .get();

    return ok()

         .contentType(JSON)

         .body(serviceTempl.deleteWithCriteriaTemplate(id), Void.class)
         .log();


  }

  public Mono<ServerResponse> deleteCritTemplMult(ServerRequest request) {

    String id = request.queryParam("id")
                       .get();

    return ok()

         .contentType(JSON)

         .body(serviceTempl.deleteWithCriteriaTemplateMult(id), Void.class)
         .log();


  }


}