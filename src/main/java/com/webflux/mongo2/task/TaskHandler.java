package com.webflux.mongo2.task;

import com.webflux.mongo2.task.Task;
import com.webflux.mongo2.task.service.ITaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

// HANDLER: Manage HTTP(Resquests/responses)
@Component("taskHandler")
public class TaskHandler {

  private final MediaType JSON = MediaType.APPLICATION_JSON;

  @Autowired
  ITaskService taskService;


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
}