package com.webflux.mongo2.project.service.template;


import com.webflux.mongo2.project.dto.ProjectDto;
import reactor.core.publisher.Flux;

public interface IServiceProjection {
  Flux<ProjectDto> findAllProjectDto();
}