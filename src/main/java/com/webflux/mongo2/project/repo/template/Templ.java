package com.webflux.mongo2.project.repo.template;

import com.webflux.mongo2.project.entity.Project;
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

@Repository("templ")
public class Templ {

  @Autowired
  ReactiveMongoTemplate template;

  /*╔══════════════════════════════════╗
    ║ REACTIVE-MONGO-TEMPLATE-CRITERIA ║
    ╚══════════════════════════════════╝*/

  public Flux<Project> findProjectByNameQueryWithCriteriaTemplate(String name) {

    Query query = new Query();
    query.addCriteria(Criteria.where("name")
                              .is(name));

    return template.find(query, Project.class);

  }


  public Flux<Project> findByEstimatedCostBetweenQueryWithCriteriaTemplate(Long from, Long to) {

    Query query = new Query();

    query.with(Sort.by(Direction.ASC, "cost"));
    query.addCriteria(Criteria.where("cost")
                              .lt(to)
                              .gt(from));

    return template.find(query, Project.class);

  }


  public Flux<Project> findByNameRegexQueryWithCriteriaTemplate(String regexp) {

    Query query = new Query();
    query.addCriteria(Criteria.where("name")
                              .regex(regexp));

    return template.find(query, Project.class);

  }


  public Mono<Void> UpdateCostWithCritTemplUpsert(String id, Long cost) {

    Query query = new Query();
    query.addCriteria(Criteria.where("_id")
                              .is(id));

    Update update = new Update();
    update.set("cost", cost);

    //upsert: If document is matched, update it, else create a new document by combining the
    // query and update object, it’s works like findAndModifyElseCreate()
    return template
         .upsert(query, update, Project.class)
         .then();

  }


  public Mono<Void> deleteWithCriteriaTemplate(String id) {

    Query query = new Query();
    query.addCriteria(Criteria.where("_id")
                              .is(id));

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