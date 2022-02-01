package com.webflux.api.modules.project.service.template;


import com.webflux.api.modules.project.core.dto.ProjectDto;
import reactor.core.publisher.Flux;

public interface IServiceProjection {
  Flux<ProjectDto> findAllProjectDto();
}