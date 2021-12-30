
package com.webflux.mongo2.project.service.template;

import com.webflux.mongo2.project.entity.ProjectChild;
import com.webflux.mongo2.project.repo.template.TemplCollections;
import com.webflux.mongo2.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service("serviceTemplCollections")
public class ServiceTemplCollections implements IServiceTemplCollections {

  @Autowired
  TemplCollections templCollections;

  /*╔══════════════════════════════════╗
    ║ REACTIVE-MONGO-TEMPLATE-CRITERIA ║
    ╚══════════════════════════════════╝*/

  @Override
  public Mono<ProjectChild> addTemplMultCollections(Mono<Task> task) {

    return templCollections.addTemplMultCollections(task);
  }

  @Override
  public Mono<ProjectChild> updateTemplMultCollections(Mono<Task> task) {

    return templCollections.updateTemplMultCollections(task);
  }

  @Override
  public Mono<ProjectChild> DeleteTemplMultCollections(
       String projectId,
       String taskId) {

    return templCollections.deleteTemplMultCollections(projectId, taskId);
  }


  @Override
  public Mono<Void> deleteAllCollectionsTemplate() {

    return templCollections.dropCollectionsTemplate();
  }

  @Override
  public Flux<String> checkCollectionsTemplate() {

    return templCollections.checkCollectionsTemplate();
  }


}