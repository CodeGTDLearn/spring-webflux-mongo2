package com.webflux.mongo2.project.service;

import com.webflux.mongo2.project.Project;
import com.webflux.mongo2.project.ProjectChild;
import com.webflux.mongo2.project.repo.Templ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service("serviceTempl")
public class ServiceTempl implements IServiceTempl {

  @Autowired
  Templ templ;

  /*╔══════════════════════════════════╗
    ║ REACTIVE-MONGO-TEMPLATE-CRITERIA ║
    ╚══════════════════════════════════╝*/
  @Override
  public Flux<Project> findProjectByNameQueryWithCriteriaTemplate(String name) {

    return templ.findProjectByNameQueryWithCriteriaTemplate(name);
  }

  @Override
  public Flux<Project> findByEstimatedCostBetweenQueryWithCriteriaTemplate(Long from, Long to) {

    return templ.findByEstimatedCostBetweenQueryWithCriteriaTemplate(from, to);
  }

  @Override
  public Flux<Project> findByNameRegexQueryWithCriteriaTemplate(String regexp) {

    return templ.findByNameRegexQueryWithCriteriaTemplate(regexp);
  }

  @Override
  public Mono<Void> UpdateCostWithCritTemplUpsert(String id, Long cost) {

    return templ.UpdateCostWithCritTemplUpsert(id, cost);
  }


  @Override
  public Mono<Project> UpdateCountryListWithCritTemplUpsertArray(String id, String country) {

    return templ.UpdateCountryListWithCritTemplUpsertArray(id, country);
  }

  @Override
  public Mono<ProjectChild> UpdateCountryListWithCritTemplUpsertChild(String id, String ownername) {

    return templ.UpdateCountryListWithCritTemplUpsertChild(id, ownername);
  }


  @Override
  public Mono<Void> deleteWithCriteriaTemplate(String id) {

    return templ.deleteWithCriteriaTemplate(id);
  }

  @Override
  public Mono<Void> deleteWithCriteriaTemplateMult(String id) {

    return templ.deleteWithCriteriaTemplateMult(id);
  }
}