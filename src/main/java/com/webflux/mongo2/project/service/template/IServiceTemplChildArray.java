package com.webflux.mongo2.project.service.template;


import com.webflux.mongo2.project.entity.Project;
import com.webflux.mongo2.project.entity.ProjectChild;
import com.webflux.mongo2.task.Task;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IServiceTemplChildArray {

  Mono<Project> AddCritTemplArray(String id, String country);


  Mono<ProjectChild> AddCritTemplChild(String id, Mono<Task> task);


  Mono<ProjectChild> UpdateCritTemplChild(
       String id, String idch, String ownername);


  Flux<ProjectChild> DeleteCritTemplChild(String id, String idch);


  Mono<Project> DeleteCritTemplArray(String id, String country);


  Mono<Project> updateCritTemplArray(String id, String country, String newcountry);


}