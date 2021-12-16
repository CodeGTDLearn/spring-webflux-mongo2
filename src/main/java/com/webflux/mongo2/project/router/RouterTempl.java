package com.webflux.mongo2.project.router;

import com.webflux.mongo2.project.handler.HandlerTempl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static com.webflux.mongo2.config.routes.project.RoutesTempl.*;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

//ROUTER:
// A) Define app routes/end-points
// B) EventLoop read this Router, and send the Message for the Handler
@Configuration
public class RouterTempl {

  private final MediaType JSON = MediaType.APPLICATION_JSON;

  @Bean
  public RouterFunction<ServerResponse> routesTempl(HandlerTempl handler) {

    return RouterFunctions
         .route(GET(TEMPL_BYNAME).and(accept(JSON)),
                handler::findProjByNameQueryCritTempl
               )
         .andRoute(GET(TEMPL_EST_COST_BET).and(accept(JSON)),
                   handler::findByEstCostBetQueryCritTempl
                  )
         .andRoute(GET(TEMPL_BYNAME_REG).and(accept(JSON)),
                   handler::findByNameRegexQueryCritTempl
                  )
         .andRoute(PUT(TEMPL_UPSERT_CRIT).and(accept(JSON)),
                   handler::upsertCostWithCritTempl
                  )
         .andRoute(DELETE(TEMPL_DEL_CRIT).and(accept(JSON)),
                   handler::deleteCritTempl
                  )
         .andRoute(DELETE(TEMPL_DEL_CRIT_MULT).and(accept(JSON)),
                   handler::deleteCritTemplMult
                  )

         ;
  }
}