package com.webflux.mongo2.task.service;


import com.webflux.mongo2.task.entity.Task;
import reactor.core.publisher.Mono;

public interface ITaskService {
  Mono<Task> createTask(Task task);
}