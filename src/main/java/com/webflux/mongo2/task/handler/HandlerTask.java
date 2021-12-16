package com.webflux.mongo2.task.handler;

import com.webflux.mongo2.task.Task;
import com.webflux.mongo2.task.service.IServiceTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

// HANDLER: Manage HTTP(Resquests/responses)
@Component("taskHandler")
public class HandlerTask {

  private final MediaType JSON = MediaType.APPLICATION_JSON;

  @Autowired
  IServiceTask taskService;


  public Mono<ServerResponse> createTask(ServerRequest request) {
    final Mono<Task> task = request.bodyToMono(Task.class);

    return
         task
              .flatMap(taskService::createTask)
              .flatMap(data ->
                            ServerResponse.ok()
                                          .contentType(JSON)
                                          .bodyValue(data));
  }

  public Mono<ServerResponse> findAllTasks(ServerRequest request) {
    return
         ok()
              .contentType(JSON)
              .body(taskService.findAll(), Task.class);
  }

//  public Mono<ServerResponse> delete(ServerRequest request) {
//
//    String id = request.pathVariable("id");
//
//        return
//             ok()
//                  .contentType(JSON)
//                  .body(
//                       taskService.deleteById(id),Void.class
//                       )
//                  .log();
//  }
}