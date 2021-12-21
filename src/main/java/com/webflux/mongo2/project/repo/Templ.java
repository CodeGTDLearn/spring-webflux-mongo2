package com.webflux.mongo2.project.repo;

import com.webflux.mongo2.project.Project;
import com.webflux.mongo2.project.ProjectChild;
import com.webflux.mongo2.task.repo.ITaskRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
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

  @Autowired
  ITaskRepo taskRepo;

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

    //upsert: If document is matched, update it, else create a new document by combining the query and update object, it’s works like findAndModifyElseCreate()
    return template
         .upsert(query, update, Project.class)
         .then();

  }


  public Mono<Project> UpdateCountryListWithCritTemplUpsertArray(String id, String country) {

    Query query = new Query();
    query.addCriteria(Criteria.where("_id")
                              .is(id));

    Update update = new Update();
    update.push("countryList", country);

    return template
         //findAndModify: Find and modify and get the newly updated object from a single operation.
         .findAndModify(
              query, update,
              new FindAndModifyOptions().returnNew(true), Project.class
                       );
    //         .upsert(query, update, Project.class)
    //         .then(template.findOne(query, Project.class));

  }

  public Mono<ProjectChild> UpdateCountryListWithCritTemplUpsertChild(String id, String ownername) {

    Query query = new Query();
    query.addCriteria(Criteria.where("_id")
                              .is(id));

    Update update = new Update();
    update.set("tasks.ownername", ownername);

    return template
         .upsert(query, update, ProjectChild.class)
         .then(template.findOne(query, ProjectChild.class));

  }

  public Mono<Void> deleteWithCriteriaTemplate(String id) {

    Query query = new Query();
    query.addCriteria(Criteria.where("_id")
                              .is(id));

    return template.remove(query, Project.class)
                   .then();
  }


  public Mono<Void> deleteWithCriteriaTemplateMult(String id) {

    Query projectDocument = new Query();
    projectDocument.addCriteria(Criteria.where("_id")
                                        .is(id));
    Query taskDocument = new Query();
    taskDocument.addCriteria(Criteria.where("projectId")
                                     .is(id));

    return template.remove(projectDocument, Project.class)
                   .then(Mono.empty());

    //    return template.remove(taskDocument, Task.class)
    //                   .then(Mono.empty());
  }

}