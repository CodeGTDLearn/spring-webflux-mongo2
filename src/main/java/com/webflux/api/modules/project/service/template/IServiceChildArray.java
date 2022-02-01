package com.webflux.api.modules.project.service.template;


import com.webflux.api.modules.project.entity.Project;
import com.webflux.api.modules.project.entity.ProjectChild;
import com.webflux.api.modules.task.Task;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IServiceChildArray {

  Mono<Project> addCritTemplArray(String projectId, String country);


  Mono<ProjectChild> addCritTemplChild(String projectId, Mono<Task> task);


  Mono<ProjectChild> updateCritTemplChild(
       String projectId,
       String taskIdToUpdate,
       String ownername);


  Flux<ProjectChild> deleteCritTemplChild(String projectId, String taskIdToDelete);


  Mono<Project> deleteCritTemplArray(String projectId, String country);


  Mono<Project> updateCritTemplArray(String projectId, String country, String newcountry);

  Mono<Boolean> existTheTaskInProjectChild(String projectId, String taskId);


}