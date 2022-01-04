package com.webflux.mongo2.project.router.template;

import com.webflux.mongo2.project.handler.template.HandlerLookupProjection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static com.webflux.mongo2.core.routes.project.template.RoutesLookupProjection.TEMPL_LOOKUP_PROJ;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

//ROUTER:
// A) Define app routes/end-points
// B) EventLoop read this Router, and send the Message for the Handler
@Configuration
public class RouterLookupProjection {

  private final MediaType JSON = MediaType.APPLICATION_JSON;

  @Bean
  public RouterFunction<ServerResponse> routesLookupProjection(HandlerLookupProjection handler) {

    return RouterFunctions
         .route(GET(TEMPL_LOOKUP_PROJ).and(accept(JSON)),
                handler::findAllProjectTasks
               )
         ;
  }
}