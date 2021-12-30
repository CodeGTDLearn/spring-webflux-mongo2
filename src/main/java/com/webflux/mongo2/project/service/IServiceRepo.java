package com.webflux.mongo2.project.service;


import com.webflux.mongo2.project.entity.Project;
import com.webflux.mongo2.task.Task;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IServiceRepo {
  Mono<Project> createProject(Project project);

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

  //  public Mono<Long> findNoOfProjectsCostGreaterThan(Long cost);

  //  public Flux<ResultByStartDateAndCost> findCostsGroupByStartDateForProjectsCostGreaterThan
  //  (Long cost);
  //
  //  public Flux<ResultProjectTasks> findAllProjectTasks();

  Mono<Void> saveProjectAndTask(Mono<Project> p,Mono<Task> t);

  //  public Mono<Void> chunkAndSaveProject(Project p);

  //  public Mono<Project> loadProjectFromGrid(String projectId);

  //  public Mono<Void> deleteProjectFromGrid(String projectId);
}