
package com.webflux.api.modules.project.service.template.impl;

import com.webflux.api.modules.project.entity.ProjectChild;
import com.webflux.api.modules.project.repo.template.Colections;
import com.webflux.api.modules.project.service.template.IServiceColections;
import com.webflux.api.modules.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service("serviceColections")
public class ServiceColections implements IServiceColections {

  @Autowired
  Colections colections;

  /*╔══════════════════════════════════╗
    ║ REACTIVE-MONGO-TEMPLATE-CRITERIA ║
    ╚══════════════════════════════════╝*/

  @Override
  public Mono<ProjectChild> addTemplMultCollections(Mono<Task> task) {

    return colections.addTemplMultCollections(task);
  }

  @Override
  public Mono<ProjectChild> updateTemplMultCollections(Mono<Task> task) {

    return colections.updateTemplMultCollections(task);
  }

  @Override
  public Mono<ProjectChild> DeleteTemplMultCollections(
       String projectId,
       String taskId) {

    return colections.deleteTemplMultCollections(projectId, taskId);
  }


  @Override
  public Mono<Void> deleteAllCollectionsTemplate() {

    return colections.dropCollectionsTemplate();
  }

  @Override
  public Flux<String> checkCollectionsTemplate() {

    return colections.checkCollectionsTemplate();
  }


}