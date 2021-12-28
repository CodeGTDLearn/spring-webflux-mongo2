package com.webflux.mongo2.project.service;


import com.webflux.mongo2.project.Project;
import com.webflux.mongo2.project.ProjectChild;
import com.webflux.mongo2.task.Task;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IServiceTemplAdvanced {

  Mono<Project> AddCritTemplArray(String id, String country);


  Mono<ProjectChild> AddCritTemplChild(String id, Mono<Task> task);


  Mono<ProjectChild> UpdateCritTemplChild(
       String id, String idch, String ownername);


  Mono<ProjectChild> DeleteCritTemplMultCollections(
       String projectId,
       String taskIdToDelete);


  Flux<ProjectChild> DeleteCritTemplChild(String id, String idch);


  Mono<Project> DeleteCritTemplArray(String id, String country);


  Mono<Void> deleteAllCollectionsTemplate();


  Flux<String> checkCollectionsTemplate();


  Mono<Project> updateCritTemplArray(String id, String country, String newcountry);
}