package com.webflux.mongo2.project.service.template;


import com.webflux.mongo2.project.entity.Project;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IServiceTempl {
  Flux<Project> findProjectByNameQueryWithCriteriaTemplate(String name);


  Flux<Project> findByEstimatedCostBetweenQueryWithCriteriaTemplate(Long from, Long to);


  Flux<Project> findByNameRegexQueryWithCriteriaTemplate(String regexp);


  Mono<Void> UpdateCostWithCritTemplUpsert(String id, Long cost);


  Mono<Void> deleteWithCriteriaTemplate(String id);

}