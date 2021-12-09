package com.webflux.mongo2.project;

import com.webflux.mongo2.project.ProjectHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static com.webflux.mongo2.config.routes.ProjectRoutes.*;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

//ROUTER:
// A) Define app routes/end-points
// B) EventLoop read this Router, and send the Message for the Handler
@Configuration
public class ProjectRouter {

  private final MediaType JSON = MediaType.APPLICATION_JSON;


  @Bean
  public RouterFunction<ServerResponse> projectRoutes(ProjectHandler handler) {

    return RouterFunctions
         .route(POST(PROJ_CREATE).and(accept(JSON)),handler::createProject)

         .andRoute(GET(PROJ_ROOT).and(accept(JSON)),handler::findAll)
         .andRoute(GET(PROJ_ID).and(accept(JSON)),handler::findById)
         .andRoute(DELETE(PROJ_ID).and(accept(JSON)),handler::delete)
         .andRoute(PUT(PROJ_UPD).and(accept(JSON)),handler::update)

         .andRoute(GET(PROJ_BYNAME).and(accept(JSON)),handler::findByName)
         .andRoute(GET(PROJ_BYNAME_NOT).and(accept(JSON)),handler::findByNameNot)
         .andRoute(GET(PROJ_COST_GREATER).and(accept(JSON)),
                   handler::findByEstimCostGreatThan
                  )
         .andRoute(GET(PROJ_COST_BETW).and(accept(JSON)),handler::findByEstimatedCostBetween)
         .andRoute(GET(PROJ_BYNAME_LIKE).and(accept(JSON)),handler::findByNameLike)
         .andRoute(GET(PROJ_BYNAME_REGEX).and(accept(JSON)),handler::findByNameRegex)
         .andRoute(GET(PROJ_QUERY_BYNAME).and(accept(JSON)),handler::findByNameQuery)
         .andRoute(GET(PROJ_QUERY_BYNAME_COST).and(accept(JSON)),
                   handler::findByNameAndCostQuery
                  )
         .andRoute(GET(PROJ_QUERY_EST_COST_BET).and(accept(JSON)),
                   handler::findByEstCostBetwQuery
                  )
         .andRoute(GET(PROJ_QUERY_NYNAME_REGEX).and(accept(JSON)),
                   handler::findByNameRegexQuery
                  )
         .andRoute(GET(PROJ_TEMPL_BYNAME_QUERY).and(accept(JSON)),
                   handler::findProjByNameQueryTempl
                  )
         .andRoute(GET(PROJ_TEMPL_EST_COST_BET_QUERY).and(accept(JSON)),
                   handler::findByEstCostBetQueryTempl
                  )
         .andRoute(GET(PROJ_TEMPL_BYNAME_REG_QUERY).and(accept(JSON)),
                   handler::findByNameRegexQueryTempl
                  )
         .andRoute(POST(PROJ_TEMPL_UPCOST_CRIT).and(accept(JSON)),
                   handler::upsertCostWithCritTempl
                  )
         .andRoute(DELETE(PROJ_TEMPL_DEL_CRIT).and(accept(JSON)),
                   handler::deleteCriteriaTempl
                  );


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


  }
}


//@Configuration
//public class ProjectRouter {
//  @Bean
//  public RouterFunction<ServerResponse> routeProjects(ProjectHandler handler) {