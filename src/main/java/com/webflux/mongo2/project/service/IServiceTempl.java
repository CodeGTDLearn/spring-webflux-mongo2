package com.webflux.mongo2.project.service;


import com.webflux.mongo2.project.Project;
import com.webflux.mongo2.project.ProjectChild;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IServiceTempl {
  Flux<Project> findProjectByNameQueryWithCriteriaTemplate(String name);


  Flux<Project> findByEstimatedCostBetweenQueryWithCriteriaTemplate(Long from, Long to);


  Flux<Project> findByNameRegexQueryWithCriteriaTemplate(String regexp);


  Mono<Void> UpdateCostWithCritTemplUpsert(String id, Long cost);

  Mono<Project> UpdateCountryListWithCritTemplUpsertArray(String id, String country);

  Mono<ProjectChild> UpdateCountryListWithCritTemplUpsertChild(String id, String ownername);

  Mono<Void> deleteWithCriteriaTemplate(String id);


  Mono<Void> deleteWithCriteriaTemplateMult(String id);
}