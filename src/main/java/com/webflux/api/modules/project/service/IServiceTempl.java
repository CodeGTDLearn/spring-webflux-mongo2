package com.webflux.api.modules.project.service;


import com.webflux.api.modules.project.entity.Project;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IServiceTempl {
  Flux<Project> findProjectByNameQueryWithCriteriaTemplate(String projectName);


  Flux<Project> findByEstimatedCostBetweenQueryWithCriteriaTemplate(
       Long projectCostFrom,
       Long projectCostTo);


  Flux<Project> findByNameRegexQueryWithCriteriaTemplate(String regexpProjectName);


  Mono<Void> UpdateCostWithCritTemplUpsert(String projectId, Long projectCost);


  Mono<Void> deleteWithCriteriaTemplate(String projectId);

}