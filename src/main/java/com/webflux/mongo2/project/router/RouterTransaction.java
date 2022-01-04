package com.webflux.mongo2.project.router;

import com.webflux.mongo2.project.handler.HandlerTransaction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static com.webflux.mongo2.core.routes.project.RoutesTransaction.REPO_TRANSACT;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

//ROUTER:
// A) Define app routes/end-points
// B) EventLoop read this Router, and send the Message for the Handler
@Configuration
public class RouterTransaction {

  private final MediaType JSON = MediaType.APPLICATION_JSON;

  @Bean
  public RouterFunction<ServerResponse> routesTransaction(HandlerTransaction handler) {

    return RouterFunctions
         .route(POST(REPO_TRANSACT).and(accept(JSON)), handler::saveProjectAndTask)
         ;
  }
}