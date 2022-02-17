package com.webflux.api.modules.project.service;


import com.webflux.api.modules.project.entity.Project;
import com.webflux.api.modules.task.Task;
import reactor.core.publisher.Mono;

public interface IServiceTransaction {
  Mono<Task> createProjectTransaction(Project project, Task task);
}