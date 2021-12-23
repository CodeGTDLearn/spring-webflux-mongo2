package com.webflux.mongo2.project.service;

import com.webflux.mongo2.project.Project;
import com.webflux.mongo2.project.repo.TemplLecture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service("serviceTemplLecture")
public class ServiceTemplLecture implements IServiceTemplLecture {

  @Autowired
  TemplLecture templLecture;

  /*╔══════════════════════════════════╗
    ║ REACTIVE-MONGO-TEMPLATE-CRITERIA ║
    ╚══════════════════════════════════╝*/
  @Override
  public Flux<Project> findProjectByNameQueryWithCriteriaTemplate(String name) {

    return templLecture.findProjectByNameQueryWithCriteriaTemplate(name);
  }

  @Override
  public Flux<Project> findByEstimatedCostBetweenQueryWithCriteriaTemplate(Long from, Long to) {

    return templLecture.findByEstimatedCostBetweenQueryWithCriteriaTemplate(from, to);
  }

  @Override
  public Flux<Project> findByNameRegexQueryWithCriteriaTemplate(String regexp) {

    return templLecture.findByNameRegexQueryWithCriteriaTemplate(regexp);
  }

  @Override
  public Mono<Void> UpdateCostWithCritTemplUpsert(String id, Long cost) {

    return templLecture.UpdateCostWithCritTemplUpsert(id, cost);
  }

  @Override
  public Mono<Void> deleteWithCriteriaTemplate(String id) {

    return templLecture.deleteWithCriteriaTemplate(id);
  }

}