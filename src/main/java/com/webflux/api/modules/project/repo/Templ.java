package com.webflux.api.modules.project.repo;

import com.webflux.api.modules.project.entity.Project;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository("repoTempl")
@RequiredArgsConstructor
public class Templ {


  private final ReactiveMongoTemplate reactiveMongoTemplate;

  /*╔══════════════════════════════════╗
    ║ REACTIVE-MONGO-TEMPLATE-CRITERIA ║
    ╚══════════════════════════════════╝*/

  public Flux<Project> findProjectByNameQueryWithCriteriaTemplate(String projectName) {

    Query query = new Query();
    query.addCriteria(Criteria.where("name")
                              .is(projectName));

    return reactiveMongoTemplate.find(query, Project.class);

  }


  public Flux<Project> findByEstimatedCostBetweenQueryWithCriteriaTemplate(
       Long projectCostFrom,
       Long projectCostTo) {

    Query query = new Query();

    query.with(Sort.by(Direction.ASC, "cost"));
    query.addCriteria(Criteria.where("cost")
                              .lt(projectCostTo)
                              .gt(projectCostFrom));

    return reactiveMongoTemplate.find(query, Project.class);

  }


  public Flux<Project> findByNameRegexQueryWithCriteriaTemplate(String regexpProjectName) {

    Query query = new Query();
    query.addCriteria(Criteria.where("name")
                              .regex(regexpProjectName));

    return reactiveMongoTemplate.find(query, Project.class);

  }


  public Mono<Void> UpdateCostWithCritTemplUpsert(String projectId, Long projectCost) {

    Query findProjectToUpdate = new Query();
    findProjectToUpdate
         .addCriteria(Criteria.where("_id")
                              .is(projectId));

    Update update = new Update();
    update.set("cost", projectCost);

    // UPSERT:
    // - If document is matched, update it,
    // - else create a new document by combining the findProjectToUpdate and update object,
    // - it’s works like findAndModifyElseCreate()
    return
         reactiveMongoTemplate
              .upsert(findProjectToUpdate, update, Project.class)
              .then();

  }


  public Mono<Void> deleteWithCriteriaTemplate(String projectId) {

    Query query = new Query();
    query.addCriteria(Criteria.where("_id")
                              .is(projectId));

    return reactiveMongoTemplate.remove(query, Project.class)
                                .then();
  }

  public <T> Flux<T> aggreg(
       Aggregation aggregation,
       String collection,
       Class<T> classe) {

    return reactiveMongoTemplate.aggregate(aggregation, collection, classe);
  }
}