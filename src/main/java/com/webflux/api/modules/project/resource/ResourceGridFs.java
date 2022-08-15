//package com.webflux.api.modules.project.resource;
//
//import com.webflux.api.modules.project.core.exceptions.ProjectExceptionsThrower;
//import com.webflux.api.modules.project.entity.Project;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.reactive.function.server.ServerRequest;
//import org.springframework.web.reactive.function.server.ServerResponse;
//import reactor.core.publisher.Mono;
//
//import static com.webflux.api.modules.project.core.routes.RoutesRepo.REPO_ROOT;
//import static org.springframework.web.reactive.function.server.ServerResponse.ok;
//
////@RestController
////@RequiredArgsConstructor
////@RequestMapping(REPO_ROOT)
//public class ResourceGridFs {
//
//  private final ProjectExceptionsThrower projectExceptionsThrower;
//
//      public Mono<ServerResponse> chunkAndSaveProject(ServerRequest request) {
//
//      Project p = new Project();
//      p.set_id("20");
//      p.setName("ProjectGrid");
//
//      return ok()
//
//           .contentType(JSON)
//
//           .body(projectService.chunkAndSaveProject(p),Void.class)
//           .log();
//    }
//
//
//    public Mono<ServerResponse> loadProjectFromGrid(ServerRequest request) {
//      String pid = request.queryParam("pid")
//                          .orElseThrow();
//
//
//      return ok()
//
//           .contentType(JSON)
//
//           .body(projectService.loadProjectFromGrid(pid),Project.class)
//           .log();
//    }
//
//
//    public Mono<ServerResponse> deleteProjectFromGrid(ServerRequest request) {
//      String pid = request.queryParam("pid")
//                          .orElseThrow();
//
//
//      return ok()
//
//           .contentType(JSON)
//
//           .body(projectService.deleteProjectFromGrid(pid), Project.class)
//           .log();
//    }
//
//}