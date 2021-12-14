package com.webflux.mongo2.project.service;


import com.webflux.mongo2.project.Project;
import com.webflux.mongo2.task.Task;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IServiceTempl {
  Flux<Project> findProjectByNameQueryWithTemplate(String name);

  Flux<Project> findByEstimatedCostBetweenQueryWithTemplate(Long from,Long to);

  Flux<Project> findByNameRegexQueryWithTemplate(String regexp);

  Mono<Void> upsertCostWithCriteriaTemplate(String id,Long cost);

  Mono<Void> deleteWithCriteriaTemplate(String id);
}