package com.webflux.api.modules.project.service;


import com.webflux.api.modules.project.entity.Project;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IServiceCrud {
  Mono<Project> save(Project project);


  Mono<Project> update(Project project);


  Flux<Project> findAll();


  Mono<Project> findById(String projectId);


  Mono<Void> deleteById(String projectId);


  Flux<Project> findByName(String projectName);
}