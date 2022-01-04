package com.webflux.mongo2.project.handler.template;

import com.webflux.mongo2.project.entity.Project;
import com.webflux.mongo2.project.entity.ProjectChild;
import com.webflux.mongo2.project.service.template.IServiceChildArray;
import com.webflux.mongo2.task.Task;
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
@Component("handlerChildArray")
public class HandlerChildArray {

  private final MediaType JSON = MediaType.APPLICATION_JSON;

  @Autowired
  IServiceChildArray serviceChildArray;

  @NonNull
  public Mono<ServerResponse> AddCritTemplArray(ServerRequest request) {

    String id = request.queryParam("id")
                       .orElseThrow();

    String country = request.queryParam("country")
                            .orElseThrow();

    return ok()

         .contentType(JSON)

         .body(serviceChildArray.AddCritTemplArray(id, country),
               Project.class
              )
         .log();


  }

  @NonNull
  public Mono<ServerResponse> updateCritTemplArray(ServerRequest request) {

    String id = request.queryParam("id")
                       .orElseThrow();

    String country = request.queryParam("country")
                            .orElseThrow();

    String newcountry = request.queryParam("newcountry")
                               .orElseThrow();

    return ok()

         .contentType(JSON)

         .body(serviceChildArray.updateCritTemplArray(id, country, newcountry),
               Project.class
              )
         .log();


  }

  @NonNull
  public Mono<ServerResponse> DeleteCritTemplArray(ServerRequest request) {

    String id = request.queryParam("id")
                       .orElseThrow();

    String country = request.queryParam("country")
                            .orElseThrow();

    return ok()

         .contentType(JSON)

         .body(serviceChildArray.DeleteCritTemplArray(id, country),
               Project.class
              )
         .log();


  }


  @NonNull
  public Mono<ServerResponse> AddCritTemplChild(ServerRequest request) {

    String id = request.queryParam("id")
                       .orElseThrow();

    final Mono<Task> task = request.bodyToMono(Task.class);

    return ok()

         .contentType(JSON)

         .body(serviceChildArray.AddCritTemplChild(id, task),
               ProjectChild.class
              )
         .log();

  }

  @NonNull
  public Mono<ServerResponse> UpdateCritTemplChild(ServerRequest request) {

    String id = request.queryParam("id")
                       .orElseThrow();

    String idch = request.queryParam("idch")
                         .orElseThrow();


    String ownername = request.queryParam("ownername")
                              .orElseThrow();

    return ok()

         .contentType(JSON)

         .body(serviceChildArray.UpdateCritTemplChild(id, idch, ownername),
               ProjectChild.class
              )
         .log();


  }

  @NonNull
  public Mono<ServerResponse> DeleteCritTemplChild(ServerRequest request) {

    String id = request.queryParam("id")
                       .orElseThrow();

    String idch = request.queryParam("idch")
                         .orElseThrow();

    return ok()

         .contentType(JSON)

         .body(serviceChildArray.DeleteCritTemplChild(id, idch),
               ProjectChild.class
              )
         .log();
  }

}