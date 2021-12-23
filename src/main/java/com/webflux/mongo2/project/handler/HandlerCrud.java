package com.webflux.mongo2.project.handler;

import com.webflux.mongo2.project.Project;
import com.webflux.mongo2.project.service.IServiceCrud;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.util.annotation.NonNull;

import static org.springframework.web.reactive.function.server.ServerResponse.notFound;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

// HANDLER: Manage HTTP(Resquests/responses)
//HANDLER:
// A) HANDLER receive the message from ROUTERED
// B) and, send this message for SERVICE
@Component("handlerCrud")
public class HandlerCrud {

  private final MediaType JSON = MediaType.APPLICATION_JSON;

  @Autowired
  IServiceCrud serviceCrud;


  /*╔══════════════════════════════╗
    ║   REACTIVE-MONGO-REPOSITORY  ║
    ╚══════════════════════════════╝*/
  @NonNull
  public Mono<ServerResponse> createProject(ServerRequest request) {

    // convert/abstracting JSON to Entity
    final Mono<Project> project = request.bodyToMono(Project.class);

    return
         project
              .flatMap(serviceCrud::save)
              .flatMap(data ->
                            ok()
                                 .contentType(JSON)
                                 .bodyValue(data))
         //              .onErrorResume(error -> {
         //                if (error instanceof OptimisticLockingFailureException) {
         //                  return ServerResponse.status(BAD_REQUEST)
         //                                       .build();
         //                }
         //                return ServerResponse.status(INTERNAL_SERVER_ERROR)
         //                                     .build();
         //              })
         ;
  }

  @NonNull
  public Mono<ServerResponse> update(ServerRequest request) {

    // convert/abstracting JSON to Entity
    final Mono<Project> project = request.bodyToMono(Project.class);

    return
         project
              .flatMap(serviceCrud::save)
              .flatMap(data ->
                            ok()
                                 .contentType(JSON)
                                 .bodyValue(data))
         //              .onErrorResume(error -> {
         //                if (error instanceof OptimisticLockingFailureException) {
         //                  return ServerResponse.status(BAD_REQUEST)
         //                                       .build();
         //                }
         //                return ServerResponse.status(INTERNAL_SERVER_ERROR)
         //                                     .build();
         //              })
         ;
  }

  @NonNull
  public Mono<ServerResponse> findAll(ServerRequest request) {

    return
         ok()
              .contentType(JSON)
              .body(serviceCrud.findAll(), Project.class);
  }

  @NonNull
  public Mono<ServerResponse> findById(ServerRequest request) {

    String id = request.pathVariable("id");

    return
         serviceCrud
              .findById(id)
              .flatMap(data -> ok()
                   .contentType(JSON)
                   .bodyValue(data))
              .switchIfEmpty(notFound()
                                  .build());
  }

  @NonNull
  public Mono<ServerResponse> delete(ServerRequest request) {

    String id = request.pathVariable("id");
    // STYLE 01 - DELETE_BY_ID USING FLATMAP
    return
         serviceCrud
              .deleteById(id)
              .flatMap(data -> ServerResponse.ok()
                                             .contentType(JSON)
                                             .bodyValue(data))
              .switchIfEmpty(ServerResponse.notFound()
                                           .build());

    // STYLE 02 - DELETE_BY_ID BASICS
    //    return
    //         ok()
    //              .contentType(JSON)
    //              .body(
    //                   projectService.deleteById(id),Void.class
    //                   )
    //              .log();
  }

  /*╔══════════════════════════════╗
  ║    AUTO-GENERATED-QUERIES    ║
  ╚══════════════════════════════╝*/
  @NonNull
  public Mono<ServerResponse> findByName(ServerRequest request) {

    String name = request.queryParam("name")
                         .get();

    return ok()

         .contentType(JSON)

         .body(serviceCrud.findByName(name), Project.class)
         .log();
  }
}