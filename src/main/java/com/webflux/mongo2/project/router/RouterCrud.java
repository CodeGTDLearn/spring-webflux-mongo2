package com.webflux.mongo2.project.router;

import com.webflux.mongo2.project.handler.HandlerCrud;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static com.webflux.mongo2.config.routes.project.RoutesCrud.*;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

//ROUTER:
// A) Define app routes/end-points
// B) EventLoop read this Router, and send the Message for the Handler
@Configuration
public class RouterCrud {

  private final MediaType JSON = MediaType.APPLICATION_JSON;

  @Bean
  public RouterFunction<ServerResponse> routesCrud(HandlerCrud handler) {

    return RouterFunctions
         .route(POST(CRUD_CREATE).and(accept(JSON)), handler::createProject)
         .andRoute(GET(CRUD_ROOT).and(accept(JSON)), handler::findAll)
         .andRoute(GET(CRUD_ID).and(accept(JSON)), handler::findById)
         .andRoute(DELETE(CRUD_ID).and(accept(JSON)), handler::delete)
         .andRoute(PUT(CRUD_UPD).and(accept(JSON)), handler::update)
         ;
  }
}