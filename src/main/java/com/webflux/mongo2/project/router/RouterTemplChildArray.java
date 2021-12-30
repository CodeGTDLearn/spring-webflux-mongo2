package com.webflux.mongo2.project.router;

import com.webflux.mongo2.project.handler.template.HandlerTemplChildArray;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static com.webflux.mongo2.config.routes.project.RoutesTemplChildArray.*;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

//ROUTER:
// A) Define app routes/end-points
// B) EventLoop read this Router, and send the Message for the Handler
@Configuration
public class RouterTemplChildArray {

  private final MediaType JSON = MediaType.APPLICATION_JSON;

  @Bean
  public RouterFunction<ServerResponse> routesTemplChildArray(HandlerTemplChildArray handler) {

    return RouterFunctions
         .route(PUT(TEMPL_ADD_ARRAY_CRIT).and(accept(JSON)),
                handler::AddCritTemplArray
               )
         .andRoute(PUT(TEMPL_UPD_ARRAY_CRIT).and(accept(JSON)),
                   handler::updateCritTemplArray
                  )
         .andRoute(PUT(TEMPL_DEL_ARRAY_CRIT).and(accept(JSON)),
                   handler::DeleteCritTemplArray
                  )
         .andRoute(PUT(TEMPL_ADD_CHILD_CRIT).and(accept(JSON)),
                   handler::AddCritTemplChild
                  )
         .andRoute(PUT(TEMPL_UPD_CHILD_CRIT).and(accept(JSON)),
                   handler::UpdateCritTemplChild
                  )
         .andRoute(DELETE(TEMPL_DEL_CHILD_CRIT).and(accept(JSON)),
                   handler::DeleteCritTemplChild
                  )
         ;
  }
}