package com.webflux.mongo2.project.handler;

import com.webflux.mongo2.project.entity.Project;
import com.webflux.mongo2.project.service.template.IServiceTransaction;
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
@Component("handlerTransaction")
public class HandlerTransaction {

  private final MediaType JSON = MediaType.APPLICATION_JSON;

  @Autowired
  IServiceTransaction serviceTransaction;

  @NonNull
  public Mono<ServerResponse> saveProjectAndTask(ServerRequest request) {

    Project p = new Project();
    p.set_id("6");
    p.setName("Project6");

    Task t = new Task();
    t.set_id("10");
    t.setProjectId("6");


    return ok()
         .contentType(JSON)
         .body(
              serviceTransaction
                   .saveProjectAndTask(Mono.just(p), Mono.just(t)), Void.class)
         .log();
  }

}