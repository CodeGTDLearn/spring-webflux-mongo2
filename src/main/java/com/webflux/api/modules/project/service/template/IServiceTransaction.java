package com.webflux.api.modules.project.service.template;


import com.webflux.api.modules.project.entity.Project;
import com.webflux.api.modules.task.Task;
import reactor.core.publisher.Mono;

public interface IServiceTransaction {
  Mono<Void> saveProjectAndTask(Mono<Project> projectMono, Mono<Task> taskMono);
}