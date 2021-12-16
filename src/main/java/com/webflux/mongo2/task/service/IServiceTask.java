package com.webflux.mongo2.task.service;


import com.webflux.mongo2.task.Task;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IServiceTask {
  Mono<Task> createTask(Task task);
  Flux<Task> findAll();
//  Mono<Void> deleteById(String id);
}