package com.webflux.mongo2.project.repo;

import com.mongodb.client.result.DeleteResult;
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

  public Mono<Project> AddCritTemplArray(String id, String country) {

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

  public Mono<Project> updateCritTemplArray(String id, String country, String newcountry) {

    Query query = new Query();
    query.addCriteria(Criteria.where("_id")
                              .is(id));

    Update updatePull = new Update();
    updatePull.pull("countryList", country);

    Update updatePush = new Update();
    updatePush.push("countryList", newcountry);

    return template
         .findAndModify(query, updatePull, Project.class)
         .then(
              template
                   .findAndModify(
                        query, updatePush,
                        new FindAndModifyOptions().returnNew(true), Project.class
                                 ))
         ;
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

  public Mono<ProjectChild> AddCritTemplChild(String id, Mono<Task> task) {

    Query query = new Query();
    query.addCriteria(Criteria.where("_id")
                              .is(id));

    return task.flatMap(item -> {
      Update update = new Update();
      update.push("tasks", item);
      return template
           // findAndModify:
           // Find/modify/get the "new object" from a single operation.
           .findAndModify(
                query, update,
                new FindAndModifyOptions().returnNew(true), ProjectChild.class
                         );
    });
//
//    return template
//         // findAndModify:
//         // Find/modify/get the "new object" from a single operation.
//         .findAndModify(
//              query, update,
//              new FindAndModifyOptions().returnNew(true), ProjectChild.class
//                       );

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

  public Mono<ProjectChild> DeleteCritTemplChild(
       String projectId,
       String taskIdToDelete) {

    Query project = Query.query(Criteria.where("_id")
                                        .in(projectId));

    Query taskToDelete = Query.query(Criteria.where("_id")
                                             .in(taskIdToDelete));

    Update updateTasksArray = new Update();
    updateTasksArray.pull("tasks", taskToDelete);

    return template
         .updateMulti(project, updateTasksArray, "projectchild")
         .then(template.findById(projectId, ProjectChild.class));
  }

  public Mono<ProjectChild> DeleteCritTemplMultCollections(
       String projectId,
       String taskId) {

    Query project = Query.query(Criteria.where("_id")
                                        .in(projectId));

    Query taskToDelete = Query.query(Criteria.where("_id")
                                             .in(taskId));

    Update deleteTaskInProjectCollection = new Update();
    deleteTaskInProjectCollection.pull("tasks", taskToDelete);

    Mono<DeleteResult> removeTaskInTaskCollection =
         template.remove(taskToDelete, Task.class);

    return template
         .updateMulti(project, deleteTaskInProjectCollection, "projectchild")
         .then(removeTaskInTaskCollection)
         .then(template.findById(projectId, ProjectChild.class));
  }

  public Flux<ProjectChild> findAllTemplate() {

    return template.findAll(ProjectChild.class);
  }

  public Mono<Void> deleteAllCollectionsTemplate() {

    Flux<String> collections = template.getCollectionNames();

    return collections
         .map(item -> template.dropCollection(item + ".class"))
         .then();
  }


  public Flux<String> checkCollectionsTemplate() {

    Flux<String> collections = template.getCollectionNames();

    return collections
         .map(item -> template.dropCollection(item + ".class"))
         .thenMany(collections)
         .map(item -> item + "\n");
  }

}