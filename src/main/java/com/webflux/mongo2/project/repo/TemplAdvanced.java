package com.webflux.mongo2.project.repo;

import com.webflux.mongo2.project.Project;
import com.webflux.mongo2.project.ProjectChild;
import com.webflux.mongo2.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository("templAdvanced")
public class TemplAdvanced {

  @Autowired
  ReactiveMongoTemplate template;

  /*╔══════════════════════════════════╗
    ║ REACTIVE-MONGO-TEMPLATE-CRITERIA ║
    ╚══════════════════════════════════╝*/

  public Mono<Project> UpdateCritTemplArray(String id, String country) {

    Query query = new Query();
    query.addCriteria(Criteria.where("_id")
                              .is(id));

    Update update = new Update();
    update.push("countryList", country);

    return template
         // findAndModify:
         // Find/modify/get the "new object" from a single operation.
         .findAndModify(
              query, update,
              new FindAndModifyOptions().returnNew(true), Project.class
                       );

  }

  public Mono<Project> DeleteCritTemplArray(String id, String country) {

    Query query = new Query();
    query.addCriteria(Criteria.where("_id")
                              .is(id));

    Update update = new Update();
    update.pull("countryList", country);

    return template
         // findAndModify:
         // Find/modify/get the "new object" from a single operation.
         .findAndModify(
              query, update,
              new FindAndModifyOptions().returnNew(true), Project.class
                       );

  }

  public Mono<ProjectChild> UpdateCritTemplChild(
       String id, String idch, String ownername) {

    Query query = new Query();
    query.addCriteria(Criteria.where("_id")
                              .is(id));
    query.addCriteria(Criteria.where("tasks._id")
                              .is(idch));

    Update update = new Update();
    update.set("tasks.$.ownername", ownername);

    return template
         // findAndModify:
         // Find/modify/get the "new object" from a single operation.
         .findAndModify(
              query, update,
              new FindAndModifyOptions().returnNew(true), ProjectChild.class
                       )
         ;

  }

  public Mono<ProjectChild> DeleteCritTemplChild(String id, String idch) {

    Query query = new Query();
    query.addCriteria(Criteria
                           .where("_id")
                           .is(id)
                           .and("tasks._id")
                           .is(idch)
                     );

    Update update = new Update();
    update.set("tasks.$", null);

    return template
         // findAndModify:
         // Find/modify/get the "new object" from a single operation.
         .findAndModify(
              query, update,
              new FindAndModifyOptions().returnNew(true), ProjectChild.class
                       );
  }

  public Mono<Void> deleteWithCriteriaTemplateMult(String id) {

    Query queryTask = new Query();
    queryTask.addCriteria(Criteria.where("_id")
                                  .is(id));

    return template
         .findAndRemove(queryTask, Task.class)
         .flatMap(task ->
                       template
                            .findById(task.getProjectId(), Project.class)
                            .flatMap(project -> {
                              Query queryProject = new Query();
                              queryProject.addCriteria(Criteria.where("_id")
                                                               .is(project.get_id()));
                              return template.remove(queryProject, Project.class)
                                             .then();
                            }))
         ;
  }

  public Flux<ProjectChild> findAllTemplate() {

    return template.findAll(ProjectChild.class);
  }
}