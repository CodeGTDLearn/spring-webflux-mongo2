package com.webflux.mongo2.task.router;

import com.webflux.mongo2.task.handler.HandlerTask;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static com.webflux.mongo2.config.routes.task.RoutesTask.*;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

//ROUTER:
// A) Define app routes/end-points
// B) EventLoop read this Router,
// and send the Message for the Handler
// Handler, via service, will access the DB
@Configuration
public class RouterTask {

  private final MediaType JSON = MediaType.APPLICATION_JSON;


  @Bean
  public RouterFunction<ServerResponse> taskRoutes(HandlerTask handler) {
    return RouterFunctions
         .route(POST(TASK_CREATE).and(accept(JSON)),handler::createTask)
         .andRoute(GET(TASK_FIND_ALL).and(accept(JSON)), handler::findAllTasks)
//         .andRoute(GET(TASK_DELETE).and(accept(JSON)), handler::findAllTasks)

         ;
  }
}