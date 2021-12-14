package com.webflux.mongo2.project.service;

import com.webflux.mongo2.project.Project;
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
  public Flux<Project> findProjectByNameQueryWithTemplate(String name) {

    return templ.findProjectByNameQueryWithTemplate(name);
  }


  @Override
  public Flux<Project> findByEstimatedCostBetweenQueryWithTemplate(Long from, Long to) {

    return templ.findByEstimatedCostBetweenQueryWithTemplate(from, to);
  }


  @Override
  public Flux<Project> findByNameRegexQueryWithTemplate(String regexp) {

    return templ.findByNameRegexQueryWithTemplate(regexp);
  }


  @Override
  public Mono<Void> upsertCostWithCriteriaTemplate(String id, Long cost) {

    return templ.upsertCostWithCriteriaTemplate(id, cost);
  }


  @Override
  public Mono<Void> deleteWithCriteriaTemplate(String id) {

    return templ.deleteWithCriteriaTemplate(id);
  }
}