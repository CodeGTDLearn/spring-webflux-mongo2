package com.webflux.mongo2.project.router;

import com.webflux.mongo2.project.handler.HandlerRepo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static com.webflux.mongo2.config.routes.project.RoutesRepo.*;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

//ROUTER:
// A) Define app routes/end-points
// B) EventLoop read this Router, and send the Message for the Handler
@Configuration
public class RouterRepo {

  private final MediaType JSON = MediaType.APPLICATION_JSON;

  @Bean
  public RouterFunction<ServerResponse> routesRepo(HandlerRepo handler) {

    return RouterFunctions
         .route(GET(REPO_BYNAME_NOT).and(accept(JSON)), handler::findByNameNot)
         .andRoute(GET(REPO_COST_GREATER).and(accept(JSON)),
                   handler::findByEstimCostGreatThan
                  )
         .andRoute(GET(REPO_COST_BETW).and(accept(JSON)), handler::findByEstimatedCostBetween)
         .andRoute(GET(REPO_BYNAME_LIKE).and(accept(JSON)), handler::findByNameLike)
         .andRoute(GET(REPO_BYNAME_REGEX).and(accept(JSON)), handler::findByNameRegex)
         .andRoute(GET(REPO_QUERY_BYNAME).and(accept(JSON)), handler::findByNameQuery)
         .andRoute(GET(REPO_QUERY_BYNAME_COST).and(accept(JSON)),
                   handler::findByNameAndCostQuery
                  )
         .andRoute(GET(REPO_QUERY_EST_COST_BET).and(accept(JSON)),
                   handler::findByEstCostBetwQuery
                  )
         .andRoute(GET(REPO_QUERY_NYNAME_REGEX).and(accept(JSON)),
                   handler::findByNameRegexQuery
                  )
         //         .andRoute(
         //              RequestPredicates.GET
         //              ("/project/template/aggregate/findNoOfProjectsCostGreaterThan")
         //                               .and(RequestPredicates.accept(MediaType
         //                               .APPLICATION_JSON)),
         //              handler::findNoOfProjectsCostGreaterThan
         //                  )
         //         .andRoute(RequestPredicates.GET(
         //                                         "/project/template/aggregate" +
         //                                              "/findCostsGroupByStartDateForProjectsCostGreaterThan")
         //                                    .and(RequestPredicates.accept(MediaType
         //                                    .APPLICATION_JSON)),
         //                   handler::findCostsGroupByStartDateForProjectsCostGreaterThan
         //                  )
         //
         //         .andRoute(RequestPredicates.GET
         //         ("/project/template/aggregate/findAllProjectTasks")
         //                                    .and(RequestPredicates.accept(MediaType
         //                                    .APPLICATION_JSON)),
         //                   handler::findAllProjectTasks
         //                  )
         //         .andRoute(RequestPredicates.POST("/project/savewithtx")
         //                                    .and(RequestPredicates.accept(MediaType
         //                                    .APPLICATION_JSON)),
         //                   handler::saveProjectAndTask
         //                  )

         //         .andRoute(POST(PROJ_CRID_CHUNK_SAVE).and(accept(JSON)),
         //         handler::chunkAndSaveProject)
         //         .andRoute(RequestPredicates.GET("/project/grid/load")
         //                                    .and(RequestPredicates.accept(MediaType
         //                                    .APPLICATION_JSON)),
         //                   handler::loadProjectFromGrid
         //                  )
         //         .andRoute(DELETE(PROJ_CRID_DEL).and(accept(JSON)),
         //         handler::deleteProjectFromGrid);
         ;
  }
}