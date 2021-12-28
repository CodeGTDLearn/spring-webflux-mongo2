
package com.webflux.mongo2.project.service;

import com.webflux.mongo2.project.Project;
import com.webflux.mongo2.project.ProjectChild;
import com.webflux.mongo2.project.repo.TemplAdvanced;
import com.webflux.mongo2.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service("serviceTemplAdvanced")
public class ServiceTemplAdvanced implements IServiceTemplAdvanced {

  @Autowired
  TemplAdvanced templAdvanced;

  /*╔══════════════════════════════════╗
    ║ REACTIVE-MONGO-TEMPLATE-CRITERIA ║
    ╚══════════════════════════════════╝*/

  @Override
  public Mono<Project> AddCritTemplArray(String id, String country) {

    return templAdvanced.AddCritTemplArray(id, country);
  }

  @Override
  public Mono<Project> updateCritTemplArray(String id, String country, String newcountry) {

    return templAdvanced.updateCritTemplArray(id, country,newcountry);
  }

  @Override
  public Mono<Project> DeleteCritTemplArray(String id, String country) {

    return templAdvanced.DeleteCritTemplArray(id, country);
  }

  @Override
  public Mono<ProjectChild> AddCritTemplChild(String id, Mono<Task> task) {

    return templAdvanced.AddCritTemplChild(id, task);
  }

  @Override
  public Mono<ProjectChild> UpdateCritTemplChild(
       String id,
       String idch,
       String ownername) {

    return templAdvanced.UpdateCritTemplChild(id, idch, ownername);
  }


  @Override
  public Flux<ProjectChild> DeleteCritTemplChild(String id, String idch) {

    return templAdvanced
         .DeleteCritTemplChild(id, idch)
         .thenMany(
              templAdvanced
                   .findAllTemplate()
                   .flatMap(Flux::just)
                  )
         ;
  }

  @Override
  public Mono<ProjectChild> DeleteCritTemplMultCollections(
       String projectId,
       String taskId) {

    return templAdvanced.DeleteCritTemplMultCollections(projectId, taskId);
  }

  @Override
  public Mono<Void> deleteAllCollectionsTemplate() {

    return templAdvanced.deleteAllCollectionsTemplate();
  }

  @Override
  public Flux<String> checkCollectionsTemplate() {

    return templAdvanced.checkCollectionsTemplate();
  }


}