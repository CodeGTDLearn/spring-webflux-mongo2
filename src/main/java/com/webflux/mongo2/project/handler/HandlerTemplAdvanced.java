package com.webflux.mongo2.project.handler;

import com.webflux.mongo2.project.Project;
import com.webflux.mongo2.project.ProjectChild;
import com.webflux.mongo2.project.service.IServiceTemplAdvanced;
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
@Component("handlerTemplAdvanced")
public class HandlerTemplAdvanced {

  private final MediaType JSON = MediaType.APPLICATION_JSON;

  @Autowired
  IServiceTemplAdvanced serviceTemplAdvanced;

  @NonNull
  public Mono<ServerResponse> UpdateCritTemplArray(ServerRequest request) {

    String id = request.queryParam("id")
                       .get();

    String country = request.queryParam("country")
                            .get();

    return ok()

         .contentType(JSON)

         .body(serviceTemplAdvanced.UpdateCritTemplArray(id, country),
               Project.class
              )
         .log();


  }

  @NonNull
  public Mono<ServerResponse> DeleteCritTemplArray(ServerRequest request) {

    String id = request.queryParam("id")
                       .get();

    String country = request.queryParam("country")
                            .get();

    return ok()

         .contentType(JSON)

         .body(serviceTemplAdvanced.DeleteCritTemplArray(id, country),
               Project.class
              )
         .log();


  }

  @NonNull
  public Mono<ServerResponse> UpdateCritTemplChild(ServerRequest request) {

    String id = request.queryParam("id")
                       .get();

    String idch = request.queryParam("idch")
                         .get();


    String ownername = request.queryParam("ownername")
                              .get();

    return ok()

         .contentType(JSON)

         .body(serviceTemplAdvanced.UpdateCritTemplChild(id, idch, ownername),
               ProjectChild.class
              )
         .log();


  }

  @NonNull
  public Mono<ServerResponse> deleteCritTemplMult(ServerRequest request) {

    String id = request.queryParam("id")
                       .get();

    return ok()

         .contentType(JSON)

         .body(serviceTemplAdvanced.deleteWithCriteriaTemplateMult(id), Void.class)
         .log();


  }

  @NonNull
  public Mono<ServerResponse> DeleteCritTemplChild(ServerRequest request) {

    String id = request.queryParam("id")
                       .get();

    String idch = request.queryParam("idch")
                         .get();

    return ok()

         .contentType(JSON)

         .body(serviceTemplAdvanced.DeleteCritTemplChild(id, idch),
               ProjectChild.class
              )
         .log();
  }
}