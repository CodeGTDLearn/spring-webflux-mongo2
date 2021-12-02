package com.webflux.mongo2.project.service;


import com.webflux.mongo2.project.Project;
import com.webflux.mongo2.task.Task;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IProjectService {
  Mono<Project> createProject(Project project);

  Mono<Task> createTask(Task task);

  Flux<Project> findAll();

  Mono<Project> findById(String id);

  Mono<Void> deleteById(String id);

  Flux<Project> findByName(String name);

  Flux<Project> findByNameNot(String name);

  Flux<Project> findByEstimatedCostGreaterThan(Long cost);

  Flux<Project> findByEstimatedCostBetween(Long from,Long to);

  Flux<Project> findByNameLike(String name);

  Flux<Project> findByNameRegex(String name);

  Flux<Project> findProjectByNameQuery(String name);

  Flux<Project> findProjectByNameAndCostQuery(String name,Long cost);

  Flux<Project> findByEstimatedCostBetweenQuery(Long from,Long to);

  Flux<Project> findByNameRegexQuery(String regexp);

  Flux<Project> findProjectByNameQueryWithTemplate(String name);

  Flux<Project> findByEstimatedCostBetweenQueryWithTemplate(Long from,Long to);

  Flux<Project> findByNameRegexQueryWithTemplate(String regexp);

  Mono<Void> upsertCostWithCriteriaTemplate(String id,Long cost);

  Mono<Void> deleteWithCriteriaTemplate(String id);

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