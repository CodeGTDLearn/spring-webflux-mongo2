package com.webflux.mongo2.project.handler;

import com.webflux.mongo2.project.Project;
import com.webflux.mongo2.project.ProjectChild;
import com.webflux.mongo2.project.service.IServiceTemplAdvanced;
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
@Component("handlerTemplAdvanced")
public class HandlerTemplAdvanced {

  private final MediaType JSON = MediaType.APPLICATION_JSON;

  @Autowired
  IServiceTemplAdvanced serviceTemplAdvanced;

  @NonNull
  public Mono<ServerResponse> AddCritTemplArray(ServerRequest request) {

    String id = request.queryParam("id")
                       .orElseThrow();

    String country = request.queryParam("country")
                            .orElseThrow();

    return ok()

         .contentType(JSON)

         .body(serviceTemplAdvanced.AddCritTemplArray(id, country),
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

         .body(serviceTemplAdvanced.updateCritTemplArray(id, country, newcountry),
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

         .body(serviceTemplAdvanced.DeleteCritTemplArray(id, country),
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

         .body(serviceTemplAdvanced.AddCritTemplChild(id, task),
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

         .body(serviceTemplAdvanced.UpdateCritTemplChild(id, idch, ownername),
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

         .body(serviceTemplAdvanced.DeleteCritTemplChild(id, idch),
               ProjectChild.class
              )
         .log();
  }

  @NonNull
  public Mono<ServerResponse> DeleteCritTemplMultCollections(ServerRequest request) {

    String idProject = request.queryParam("idProject")
                              .orElseThrow();

    String idTask = request.queryParam("idTask")
                           .orElseThrow();

    return ok()

         .contentType(JSON)

         .body(serviceTemplAdvanced
                    .DeleteCritTemplMultCollections(idProject, idTask),
               ProjectChild.class
              )
         .log();


  }

  @NonNull
  public Mono<ServerResponse> deleteAllCollectionsTemplate(ServerRequest request) {

    return serviceTemplAdvanced
         .deleteAllCollectionsTemplate()
         .flatMap(s -> ServerResponse.ok()
                                     .bodyValue(Mono.just(Void.class)));
  }

  @NonNull
  public Mono<ServerResponse> checkCollectionsTemplate(ServerRequest request) {

    return ok()
         .contentType(JSON)
         .body(serviceTemplAdvanced.checkCollectionsTemplate(), String.class)
         .log();
  }

}