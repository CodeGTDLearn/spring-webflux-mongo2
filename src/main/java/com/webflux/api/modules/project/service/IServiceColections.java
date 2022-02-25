package com.webflux.api.modules.project.service;


import com.webflux.api.modules.project.entity.ProjectChild;
import com.webflux.api.modules.task.entity.Task;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IServiceColections {


  Mono<ProjectChild> DeleteTemplMultCollections(
       String projectId,
       String taskIdToDelete);


  Mono<Void> deleteAllCollectionsTemplate();


  Flux<String> checkCollectionsTemplate();


  Mono<ProjectChild> addTemplMultCollections(Mono<Task> task);


  Mono<ProjectChild> updateTemplMultCollections(Mono<Task> task);
}