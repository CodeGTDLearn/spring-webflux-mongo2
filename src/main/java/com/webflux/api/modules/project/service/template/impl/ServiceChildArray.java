
package com.webflux.api.modules.project.service.template.impl;

import com.webflux.api.modules.project.entity.Project;
import com.webflux.api.modules.project.entity.ProjectChild;
import com.webflux.api.modules.project.repo.template.ChildArray;
import com.webflux.api.modules.project.service.template.IServiceChildArray;
import com.webflux.api.modules.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service("serviceChildArray")
public class ServiceChildArray implements IServiceChildArray {

  @Autowired
  ChildArray childArray;

  /*╔══════════════════════════════════╗
    ║ REACTIVE-MONGO-TEMPLATE-CRITERIA ║
    ╚══════════════════════════════════╝*/

  @Override
  public Mono<Project> AddCritTemplArray(String id, String country) {

    return childArray.AddCritTemplArray(id, country);
  }

  @Override
  public Mono<Project> updateCritTemplArray(String id, String country, String newcountry) {

    return childArray.updateCritTemplArray(id, country, newcountry);
  }

  @Override
  public Mono<Project> DeleteCritTemplArray(String id, String country) {

    return childArray.DeleteCritTemplArray(id, country);
  }

  @Override
  public Mono<ProjectChild> AddCritTemplChild(String id, Mono<Task> task) {

    return childArray.AddCritTemplChild(id, task);
  }

  @Override
  public Mono<ProjectChild> UpdateCritTemplChild(
       String id,
       String idch,
       String ownername) {

    return childArray.UpdateCritTemplChild(id, idch, ownername);
  }


  @Override
  public Flux<ProjectChild> DeleteCritTemplChild(String id, String idch) {

    return childArray
         .DeleteCritTemplChild(id, idch)
         .thenMany(
              childArray
                   .findAllTemplProjectChild()
                   .flatMap(Flux::just)
                  )
         ;
  }


}