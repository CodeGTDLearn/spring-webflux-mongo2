
package com.webflux.api.modules.project.service.template.impl;

import com.webflux.api.modules.project.entity.Project;
import com.webflux.api.modules.project.entity.ProjectChild;
import com.webflux.api.modules.project.repo.template.RepoChildArray;
import com.webflux.api.modules.project.service.template.IServiceChildArray;
import com.webflux.api.modules.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service("serviceChildArray")
public class ServiceChildArray implements IServiceChildArray {

  @Autowired
  RepoChildArray repoChildArray;

  /*╔══════════════════════════════════╗
    ║ REACTIVE-MONGO-TEMPLATE-CRITERIA ║
    ╚══════════════════════════════════╝*/

  @Override
  public Mono<Project> addCritTemplArray(
       String projectId,
       String country) {

    return repoChildArray.AddCritTemplArray(projectId, country);
  }

  @Override
  public Mono<Project> updateCritTemplArray(
       String projectId,
       String country,
       String newcountry) {

    return repoChildArray.updateCritTemplArray(projectId, country, newcountry);
  }

  @Override
  public Mono<Boolean> existTheTaskInProjectChild(
       String projectId,
       String taskId) {
    return repoChildArray.existTheTaskInProjectChild(projectId,taskId);
  }

  @Override
  public Mono<Project> deleteCritTemplArray(
       String projectId,
       String country) {

    return repoChildArray.DeleteCritTemplArray(projectId, country);
  }

  @Override
  public Mono<ProjectChild> addCritTemplChild(
       String projectId,
       Mono<Task> task) {

    return repoChildArray.AddCritTemplChild(projectId, task);
  }

  @Override
  public Mono<ProjectChild> updateCritTemplChild(
       String projectId,
       String taskIdToUpdate,
       String ownername) {

    return repoChildArray.UpdateCritTemplChild(projectId, taskIdToUpdate, ownername);
  }


  @Override
  public Flux<ProjectChild> deleteCritTemplChild(
       String projectId,
       String taskIdToDelete) {

    return repoChildArray
         .DeleteCritTemplChild(projectId, taskIdToDelete)
         .thenMany(
              repoChildArray
                   .findAllTemplProjectChild()
                   .flatMap(Flux::just)
                  )
         ;
  }


}