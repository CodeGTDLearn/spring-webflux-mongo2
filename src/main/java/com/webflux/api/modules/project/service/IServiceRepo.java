package com.webflux.api.modules.project.service;


import com.webflux.api.modules.project.entity.Project;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IServiceRepo {
  Mono<Project> save(Project project);

  Flux<Project> findAll();

  Mono<Project> findById(String id);

  Mono<Void> deleteById(String id);

  Flux<Project> findByNameNot(String name);

  Flux<Project> findByEstimatedCostGreaterThan(Long cost);

  Flux<Project> findByEstimatedCostBetween(Long from,Long to);

  Flux<Project> findByNameLike(String name);

  Flux<Project> findByNameRegex(String name);

  Flux<Project> findProjectByNameQuery(String name);

  Flux<Project> findProjectByNameAndCostQuery(String name,Long cost);

  Flux<Project> findByEstimatedCostBetweenQuery(Long from,Long to);

  Flux<Project> findByNameRegexQuery(String regexp);

  //  public Mono<Void> chunkAndSaveProject(Project p);

  //  public Mono<Project> loadProjectFromGrid(String projectId);

  //  public Mono<Void> deleteProjectFromGrid(String projectId);
}