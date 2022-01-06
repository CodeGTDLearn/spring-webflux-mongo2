package com.webflux.api.modules.task.service;


import com.webflux.api.modules.task.Task;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IServiceTask {
  Mono<Task> save(Task task);
  Flux<Task> findAll();
//  Mono<Void> deleteById(String id);
}