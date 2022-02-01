package com.webflux.api.modules.project.repo.template;

import com.webflux.api.modules.project.entity.Project;
import org.springframework.beans.factory.annotation.Autowired;
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
public class RepoTempl {

  @Autowired
  ReactiveMongoTemplate template;

  /*╔══════════════════════════════════╗
    ║ REACTIVE-MONGO-TEMPLATE-CRITERIA ║
    ╚══════════════════════════════════╝*/

  public Flux<Project> findProjectByNameQueryWithCriteriaTemplate(String projectName) {

    Query query = new Query();
    query.addCriteria(Criteria.where("name")
                              .is(projectName));

    return template.find(query, Project.class);

  }


  public Flux<Project> findByEstimatedCostBetweenQueryWithCriteriaTemplate(
       Long projectCostFrom,
       Long projectCostTo) {

    Query query = new Query();

    query.with(Sort.by(Direction.ASC, "cost"));
    query.addCriteria(Criteria.where("cost")
                              .lt(projectCostTo)
                              .gt(projectCostFrom));

    return template.find(query, Project.class);

  }


  public Flux<Project> findByNameRegexQueryWithCriteriaTemplate(String regexpProjectName) {

    Query query = new Query();
    query.addCriteria(Criteria.where("name")
                              .regex(regexpProjectName));

    return template.find(query, Project.class);

  }


  public Mono<Void> UpdateCostWithCritTemplUpsert(String projectId, Long projectCost) {

    Query query = new Query();
    query.addCriteria(Criteria.where("_id")
                              .is(projectId));

    Update update = new Update();
    update.set("cost", projectCost);

    //upsert: If document is matched, update it, else create a new document by combining the
    // query and update object, it’s works like findAndModifyElseCreate()
    return template
         .upsert(query, update, Project.class)
         .then();

  }


  public Mono<Void> deleteWithCriteriaTemplate(String projectId) {

    Query query = new Query();
    query.addCriteria(Criteria.where("_id")
                              .is(projectId));

    return template.remove(query, Project.class)
                   .then();
  }

    public <T> Flux<T> aggreg(
         Aggregation aggregation,
         String collection,
         Class<T> classe) {

      return template.aggregate(aggregation, collection, classe);
    }
}