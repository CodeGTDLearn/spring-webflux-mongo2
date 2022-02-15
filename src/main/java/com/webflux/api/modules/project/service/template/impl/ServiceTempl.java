package com.webflux.api.modules.project.service.template.impl;

import com.webflux.api.modules.project.entity.Project;
import com.webflux.api.modules.project.repo.template.RepoTempl;
import com.webflux.api.modules.project.service.template.IServiceTempl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service("serviceTempl")
@AllArgsConstructor
public class ServiceTempl implements IServiceTempl {


  RepoTempl repoTempl;

  /*╔══════════════════════════════════╗
    ║ REACTIVE-MONGO-TEMPLATE-CRITERIA ║
    ╚══════════════════════════════════╝*/
  @Override
  public Flux<Project> findProjectByNameQueryWithCriteriaTemplate(String projectName) {

    return repoTempl.findProjectByNameQueryWithCriteriaTemplate(projectName);
  }

  @Override
  public Flux<Project> findByEstimatedCostBetweenQueryWithCriteriaTemplate(
       Long projectCostFrom,
       Long projectCostTo) {

    return repoTempl.findByEstimatedCostBetweenQueryWithCriteriaTemplate(projectCostFrom, projectCostTo);
  }

  @Override
  public Flux<Project> findByNameRegexQueryWithCriteriaTemplate(String regexpProjectName) {

    return repoTempl.findByNameRegexQueryWithCriteriaTemplate(regexpProjectName);
  }

  @Override
  public Mono<Void> UpdateCostWithCritTemplUpsert(String projectId, Long projectCost) {

    return repoTempl.UpdateCostWithCritTemplUpsert(projectId, projectCost);
  }

  @Override
  public Mono<Void> deleteWithCriteriaTemplate(String projectId) {

    return repoTempl.deleteWithCriteriaTemplate(projectId);
  }

}