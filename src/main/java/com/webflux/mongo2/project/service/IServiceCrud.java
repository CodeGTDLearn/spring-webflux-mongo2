package com.webflux.mongo2.project.service;


import com.webflux.mongo2.project.entity.Project;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IServiceCrud {
  Mono<Project> save(Project project);

  Mono<Project> update(Project project);

  Flux<Project> findAll();

  Mono<Project> findById(String id);

  Mono<Void> deleteById(String id);

  Flux<Project> findByName(String name);
}