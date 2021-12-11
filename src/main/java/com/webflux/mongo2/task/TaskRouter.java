package com.webflux.mongo2.task;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static com.webflux.mongo2.config.routes.TaskRoutes.TASK_CREATE;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

//ROUTER:
// A) Define app routes/end-points
// B) EventLoop read this Router,
// and send the Message for the Handler
// Handler, via service, will access the DB
//@Component("taskRouter")
@Configuration
public class TaskRouter {

  private final MediaType JSON = MediaType.APPLICATION_JSON;


  @Bean
  public RouterFunction<ServerResponse> taskRoutes(TaskHandler handler) {
    return RouterFunctions
         .route(POST(TASK_CREATE).and(accept(JSON)),handler::createTask);
  }
}