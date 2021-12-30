
package com.webflux.mongo2.project.service.template;

import com.webflux.mongo2.project.entity.Project;
import com.webflux.mongo2.project.entity.ProjectChild;
import com.webflux.mongo2.project.repo.template.TemplChildArray;
import com.webflux.mongo2.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service("serviceTemplChildArray")
public class ServiceTemplChildArray implements IServiceTemplChildArray {

  @Autowired
  TemplChildArray templChildArray;

  /*╔══════════════════════════════════╗
    ║ REACTIVE-MONGO-TEMPLATE-CRITERIA ║
    ╚══════════════════════════════════╝*/

  @Override
  public Mono<Project> AddCritTemplArray(String id, String country) {

    return templChildArray.AddCritTemplArray(id, country);
  }

  @Override
  public Mono<Project> updateCritTemplArray(String id, String country, String newcountry) {

    return templChildArray.updateCritTemplArray(id, country, newcountry);
  }

  @Override
  public Mono<Project> DeleteCritTemplArray(String id, String country) {

    return templChildArray.DeleteCritTemplArray(id, country);
  }

  @Override
  public Mono<ProjectChild> AddCritTemplChild(String id, Mono<Task> task) {

    return templChildArray.AddCritTemplChild(id, task);
  }

  @Override
  public Mono<ProjectChild> UpdateCritTemplChild(
       String id,
       String idch,
       String ownername) {

    return templChildArray.UpdateCritTemplChild(id, idch, ownername);
  }


  @Override
  public Flux<ProjectChild> DeleteCritTemplChild(String id, String idch) {

    return templChildArray
         .DeleteCritTemplChild(id, idch)
         .thenMany(
              templChildArray
                   .findAllTemplProjectChild()
                   .flatMap(Flux::just)
                  )
         ;
  }


}