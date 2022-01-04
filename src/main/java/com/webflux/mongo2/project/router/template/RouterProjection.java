package com.webflux.mongo2.project.router.template;

import com.webflux.mongo2.project.handler.template.HandlerProjection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static com.webflux.mongo2.core.routes.project.template.RoutesProjection.TEMPL_PROJECTION;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

//ROUTER:
// A) Define app routes/end-points
// B) EventLoop read this Router, and send the Message for the Handler
@Configuration
public class RouterProjection {

  private final MediaType JSON = MediaType.APPLICATION_JSON;

  @Bean
  public RouterFunction<ServerResponse> routesProjection(HandlerProjection handler) {

    return RouterFunctions
         .route(GET(TEMPL_PROJECTION).and(accept(JSON)),
                handler::findAllProjectDto
               )
         ;
  }
}