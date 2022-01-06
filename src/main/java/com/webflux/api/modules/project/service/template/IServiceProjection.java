package com.webflux.api.modules.project.service.template;


import com.webflux.api.modules.project.dto.ProjectDto;
import reactor.core.publisher.Flux;

public interface IServiceProjection {
  Flux<ProjectDto> findAllProjectDto();
}