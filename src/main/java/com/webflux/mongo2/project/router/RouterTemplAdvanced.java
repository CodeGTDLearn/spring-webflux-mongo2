package com.webflux.mongo2.project.router;

import com.webflux.mongo2.project.handler.HandlerTemplAdvanced;
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
public class RouterTemplAdvanced {

  private final MediaType JSON = MediaType.APPLICATION_JSON;

  @Bean
  public RouterFunction<ServerResponse> routesAdvanced(HandlerTemplAdvanced handler) {

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
         .andRoute(DELETE(TEMPL_DEL_CRIT_MULT_COL).and(accept(JSON)),
                   handler::DeleteCritTemplMultCollections
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