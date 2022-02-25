package com.webflux.api.modules.project.repo;

import com.mongodb.client.result.DeleteResult;
import com.webflux.api.modules.project.entity.ProjectChild;
import com.webflux.api.modules.task.entity.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository("repoColections")
@RequiredArgsConstructor
public class TemplColections {


  private final ReactiveMongoTemplate reactiveMongoTemplate;

  /*╔══════════════════════════════════╗
    ║ REACTIVE-MONGO-TEMPLATE-CRITERIA ║
    ╚══════════════════════════════════╝*/

  public Mono<ProjectChild> deleteTemplMultCollections(
       String projectId,
       String taskId) {

    Query project = Query.query(Criteria.where("_id")
                                        .in(projectId));

    Query taskToDelete = Query.query(Criteria.where("_id")
                                             .in(taskId));

    Update deleteTaskInProjectCollection = new Update();
    deleteTaskInProjectCollection.pull("tasks", taskToDelete);

    Mono<DeleteResult> removeTaskInTaskCollection =
         reactiveMongoTemplate.remove(taskToDelete, Task.class);

    return reactiveMongoTemplate
         .updateMulti(project, deleteTaskInProjectCollection, "projectchild")
         .then(removeTaskInTaskCollection)
         .then(reactiveMongoTemplate.findById(projectId, ProjectChild.class));
  }

  public Mono<ProjectChild> addTemplMultCollections(Mono<Task> task) {

    return task
         .flatMap(taskToSave ->
                       reactiveMongoTemplate
                            .save(taskToSave, "task")
                            .flatMap(savedTask -> {
                              Query project = Query.query(Criteria.where("_id")
                                                                  .in(savedTask.getProjectId()));

                              Update updateTaskList = new Update();
                              updateTaskList.push("tasks", savedTask);

                              return reactiveMongoTemplate
                                   .findAndModify(
                                        project, updateTaskList,
                                        new FindAndModifyOptions().returnNew(true),
                                        ProjectChild.class
                                                 );
                            }))
         ;
  }

  public Mono<ProjectChild> updateTemplMultCollections(Mono<Task> task) {

    return task
         .flatMap(taskToSave -> reactiveMongoTemplate
              // saving an object with ID, actually is update
              .save(taskToSave, "task")
              .flatMap(savedTask -> {
                Query projectAndTask = new Query();
                projectAndTask.addCriteria(Criteria.where("_id")
                                                   .is(savedTask.getProjectId()));
                projectAndTask.addCriteria(Criteria.where("tasks._id")
                                                   .is(savedTask.get_id()));

                Update updateTaskInTaskList = new Update();
                updateTaskInTaskList.set("tasks.$", savedTask);

                return reactiveMongoTemplate
                     .findAndModify(
                          projectAndTask, updateTaskInTaskList,
                          new FindAndModifyOptions().returnNew(true),
                          ProjectChild.class
                                   );
              }))
         ;
  }

  public Mono<Void> dropCollectionsTemplate() {

    Flux<String> collections = reactiveMongoTemplate.getCollectionNames();

    return collections
         .map(item -> reactiveMongoTemplate.dropCollection(item + ".class"))
         .then();
  }

  public Flux<String> checkCollectionsTemplate() {

    Flux<String> collections = reactiveMongoTemplate.getCollectionNames();

    return collections
         .map(item -> reactiveMongoTemplate.dropCollection(item + ".class"))
         .thenMany(collections)
         .map(item -> item + "\n");
  }
}