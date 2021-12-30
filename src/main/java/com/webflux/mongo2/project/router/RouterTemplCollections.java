package com.webflux.mongo2.project.router;

import com.webflux.mongo2.project.handler.template.HandlerTemplCollections;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static com.webflux.mongo2.config.routes.project.RoutesTemplCollections.*;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

//ROUTER:
// A) Define app routes/end-points
// B) EventLoop read this Router, and send the Message for the Handler
@Configuration
public class RouterTemplCollections {

  private final MediaType JSON = MediaType.APPLICATION_JSON;

  @Bean
  public RouterFunction<ServerResponse> routesTemplCollections(HandlerTemplCollections handler) {

    return RouterFunctions
         .route(DELETE(TEMPL_DEL_CRIT_MULT_COL).and(accept(JSON)),
                   handler::deleteTemplMultCollections
                  )
         .andRoute(POST(TEMPL_ADD_CRIT_MULT_COL).and(accept(JSON)),
                   handler::addTemplMultCollections
                  )
         .andRoute(PUT(TEMPL_UPD_CRIT_MULT_COL).and(accept(JSON)),
                   handler::updateTemplMultCollections
                  )
         .andRoute(DELETE(TEMPL_CLEAN_DB_CRIT_COL).and(accept(JSON)),
                   handler::deleteAllCollectionsTemplate
                  )
         .andRoute(DELETE(TEMPL_CHECK_DB_CRIT_COL).and(accept(JSON)),
                   handler::checkCollectionsTemplate
                  )
         ;
  }
}