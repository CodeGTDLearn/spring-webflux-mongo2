package com.webflux.mongo2.project.service.template;


import com.webflux.mongo2.project.entity.ProjectChild;
import com.webflux.mongo2.task.Task;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IServiceTemplCollections {


  Mono<ProjectChild> DeleteTemplMultCollections(
       String projectId,
       String taskIdToDelete);


  Mono<Void> deleteAllCollectionsTemplate();


  Flux<String> checkCollectionsTemplate();


  Mono<ProjectChild> addTemplMultCollections(Mono<Task> task);


  Mono<ProjectChild> updateTemplMultCollections(Mono<Task> task);
}