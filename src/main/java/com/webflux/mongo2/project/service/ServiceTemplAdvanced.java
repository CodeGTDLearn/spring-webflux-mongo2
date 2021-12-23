
package com.webflux.mongo2.project.service;

import com.webflux.mongo2.project.Project;
import com.webflux.mongo2.project.ProjectChild;
import com.webflux.mongo2.project.repo.TemplAdvanced;
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
  public Mono<Project> UpdateCritTemplArray(String id, String country) {

    return templAdvanced.UpdateCritTemplArray(id, country);
  }

  @Override
  public Mono<ProjectChild> UpdateCritTemplChild(
       String id, String idch, String ownername) {

    return templAdvanced.UpdateCritTemplChild(id, idch, ownername);
  }


  @Override
  public Mono<Void> deleteWithCriteriaTemplateMult(String id) {

    return templAdvanced.deleteWithCriteriaTemplateMult(id);
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
  public Mono<Project> DeleteCritTemplArray(String id, String country) {

    return templAdvanced.DeleteCritTemplArray(id, country);
  }
}