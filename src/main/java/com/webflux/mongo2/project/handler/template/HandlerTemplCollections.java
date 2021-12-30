package com.webflux.mongo2.project.handler.template;

import com.webflux.mongo2.project.entity.ProjectChild;
import com.webflux.mongo2.project.service.template.IServiceTemplCollections;
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
@Component("handlerTemplCollections")
public class HandlerTemplCollections {

  private final MediaType JSON = MediaType.APPLICATION_JSON;

  @Autowired
  IServiceTemplCollections serviceTemplCollections;

  @NonNull
  public Mono<ServerResponse> deleteTemplMultCollections(ServerRequest request) {

    String idProject = request.queryParam("idProject")
                              .orElseThrow();

    String idTask = request.queryParam("idTask")
                           .orElseThrow();

    return ok()

         .contentType(JSON)

         .body(serviceTemplCollections
                    .DeleteTemplMultCollections(idProject, idTask),
               ProjectChild.class
              )
         .log();


  }

  @NonNull
  public Mono<ServerResponse> addTemplMultCollections(ServerRequest request) {

    final Mono<Task> task = request.bodyToMono(Task.class);

    return ok()
         .contentType(JSON)
         .body(serviceTemplCollections.addTemplMultCollections(task), ProjectChild.class)
         .log();

  }

  @NonNull
  public Mono<ServerResponse> updateTemplMultCollections(ServerRequest request) {

    final Mono<Task> task = request.bodyToMono(Task.class);

    return ok()
         .contentType(JSON)
         .body(serviceTemplCollections.updateTemplMultCollections(task), ProjectChild.class)
         .log();

  }

  @NonNull
  public Mono<ServerResponse> deleteAllCollectionsTemplate(ServerRequest request) {

    return serviceTemplCollections
         .deleteAllCollectionsTemplate()
         .flatMap(s -> ServerResponse.ok()
                                     .bodyValue(Mono.just(Void.class)));
  }

  @NonNull
  public Mono<ServerResponse> checkCollectionsTemplate(ServerRequest request) {

    return ok()
         .contentType(JSON)
         .body(serviceTemplCollections.checkCollectionsTemplate(), String.class)
         .log();
  }

}