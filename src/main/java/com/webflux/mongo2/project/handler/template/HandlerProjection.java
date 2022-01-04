package com.webflux.mongo2.project.handler.template;

import com.webflux.mongo2.project.dto.ProjectDto;
import com.webflux.mongo2.project.service.template.IServiceProjection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.util.annotation.NonNull;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

// HANDLER: Manage HTTP(Resquests/responses)
//HANDLER:
// A) HANDLER receive the message from ROUTERED
// B) and, send this message for SERVICE
@Component("handlerProjection ")
public class HandlerProjection {

  private final MediaType JSON = MediaType.APPLICATION_JSON;

  @Autowired
  IServiceProjection serviceProjection;

  @NonNull
  public Mono<ServerResponse> findAllProjectDto(ServerRequest request) {

    return ok()
         .contentType(JSON)
         .body(
              serviceProjection.findAllProjectDto(),
              ProjectDto.class
              )
         .log();

  }


}