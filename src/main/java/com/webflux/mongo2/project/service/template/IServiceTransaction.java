package com.webflux.mongo2.project.service.template;


import com.webflux.mongo2.project.entity.Project;
import com.webflux.mongo2.task.Task;
import reactor.core.publisher.Mono;

public interface IServiceTransaction {
  Mono<Void> saveProjectAndTask(Mono<Project> projectMono, Mono<Task> taskMono);
}