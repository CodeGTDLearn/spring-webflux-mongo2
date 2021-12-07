//package com.webflux.mongo2.router;
//
//import com.webflux.mongo2.project.handler.ProjectHandler;
//import com.webflux.mongo2.task.handler.TaskHandler;
//import com.webflux.mongo2.project.router.ProjectRouter;
//import com.webflux.mongo2.task.router.TaskRouter;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.reactive.function.server.RouterFunction;
//import org.springframework.web.reactive.function.server.ServerResponse;
//
////ROUTER:
//// A) Define app routes/end-points
//// B) EventLoop read this Router,
//// and send the Message for the Handler
//// Handler, via service, will access the DB
//@Configuration
//public class GlobalRouter {
//
//  @Autowired
//  ProjectRouter projectRouter;
//
//  @Autowired
//  TaskRouter taskRouter;
//
//
//  @Bean
//  public RouterFunction<ServerResponse> projectRoutes(ProjectHandler projectHandler) {
//    projectRouter.projectRoutes(projectHandler);
//    return projectRouter.projectRoutes(projectHandler);
//  }
//
//
//  @Bean
//  public RouterFunction<ServerResponse> taskRoutes(TaskHandler taskHandler) {
//    taskRouter.taskRoutes(taskHandler);
//    return taskRouter.taskRoutes(taskHandler);
//  }
//}