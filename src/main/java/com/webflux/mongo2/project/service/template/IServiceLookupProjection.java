package com.webflux.mongo2.project.service.template;


import com.webflux.mongo2.project.dto.ResultProjectTasks;
import reactor.core.publisher.Flux;

public interface IServiceLookupProjection {
  Flux<ResultProjectTasks> findAllProjectTasks();
}