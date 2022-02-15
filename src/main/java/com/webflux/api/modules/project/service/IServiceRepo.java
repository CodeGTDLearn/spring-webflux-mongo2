package com.webflux.api.modules.project.service;


import com.webflux.api.modules.project.entity.Project;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IServiceRepo {
  Mono<Project> save(Project project);


  Flux<Project> findAll();


  Mono<Project> findById(String projectId);


  Mono<Void> deleteById(String projectId);


  Flux<Project> findByNameNot(String projectName);


  Flux<Project> findByEstimatedCostGreaterThan(Long projectCost);


  Flux<Project> findByEstimatedCostBetween(Long projectCostFrom, Long projectCostTo);


  Flux<Project> findByNameLike(String projectName);


  Flux<Project> findByNameRegex(String projectName);


  Flux<Project> findProjectByNameQuery(String projectName);


  Flux<Project> findProjectByNameAndCostQuery(String projectName, Long projectCost);


  Flux<Project> findByEstimatedCostBetweenQuery(Long projectCostFrom, Long projectCostTo);


  Flux<Project> findByNameRegexQuery(String regexpProjectName);

  //  public Mono<Void> chunkAndSaveProject(Project p);

  //  public Mono<Project> loadProjectFromGrid(String projectId);

  //  public Mono<Void> deleteProjectFromGrid(String projectId);
}