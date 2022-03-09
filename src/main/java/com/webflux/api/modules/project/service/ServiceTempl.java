package com.webflux.api.modules.project.service;

import com.webflux.api.modules.project.entity.Project;
import com.webflux.api.modules.project.repo.Templ;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service("serviceTempl")
@AllArgsConstructor
public class ServiceTempl implements IServiceTempl {


  Templ templ;

  /*╔══════════════════════════════════╗
    ║ REACTIVE-MONGO-TEMPLATE-CRITERIA ║
    ╚══════════════════════════════════╝*/
  @Override
  public Flux<Project> findProjectByNameQueryWithCriteriaTemplate(String projectName) {

    return templ.findProjectByNameQueryWithCriteriaTemplate(projectName);
  }

  @Override
  public Flux<Project> findByEstimatedCostBetweenQueryWithCriteriaTemplate(
       Long projectCostFrom,
       Long projectCostTo) {

    return templ.findByEstimatedCostBetweenQueryWithCriteriaTemplate(projectCostFrom,
                                                                     projectCostTo
                                                                    );
  }

  @Override
  public Flux<Project> findByNameRegexQueryWithCriteriaTemplate(String regexpProjectName) {

    return templ.findByNameRegexQueryWithCriteriaTemplate(regexpProjectName);
  }

  @Override
  public Mono<Void> UpdateCostWithCritTemplUpsert(String projectId, Long projectCost) {

    return templ.UpdateCostWithCritTemplUpsert(projectId, projectCost);
  }

  @Override
  public Mono<Void> deleteWithCriteriaTemplate(String projectId) {

    return templ.deleteWithCriteriaTemplate(projectId);
  }

}