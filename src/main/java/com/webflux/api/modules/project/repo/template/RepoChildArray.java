package com.webflux.api.modules.project.repo.template;

import com.webflux.api.modules.project.entity.Project;
import com.webflux.api.modules.project.entity.ProjectChild;
import com.webflux.api.modules.task.Task;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository("repoChildArray")
@AllArgsConstructor
public class RepoChildArray {


  ReactiveMongoTemplate template;

  /*╔══════════════════════════════════╗
    ║ REACTIVE-MONGO-TEMPLATE-CRITERIA ║
    ╚══════════════════════════════════╝*/

  public Mono<Project> AddCritTemplArray(String projectId, String country) {

    Query project = new Query();
    project.addCriteria(Criteria.where("_id")
                                .is(projectId));

    Update updateCountryList = new Update();
    updateCountryList.push("countryList", country);

    return template
         // findAndModify:
         // Find/modify/get the "new object" from a single operation.
         .findAndModify(
              project, updateCountryList,
              new FindAndModifyOptions().returnNew(true), Project.class
                       );

  }

  public Mono<Project> updateCritTemplArray(
       String projectId,
       String country,
       String newcountry) {

    Query project = new Query();
    project.addCriteria(Criteria.where("_id")
                                .is(projectId));

    Update updateCountryList = new Update();
    updateCountryList.pull("countryList", country);

    Update updateCountryListPush = new Update();
    updateCountryListPush.push("countryList", newcountry);

    return template
         .findAndModify(project, updateCountryList, Project.class)
         .then(
              template
                   .findAndModify(
                        project, updateCountryListPush,
                        new FindAndModifyOptions().returnNew(true), Project.class
                                 ))
         ;
  }

  public Mono<Project> DeleteCritTemplArray(String projectId, String country) {

    Query project = new Query();
    project.addCriteria(Criteria.where("_id")
                                .is(projectId));

    Update updateCountryList = new Update();
    updateCountryList.pull("countryList", country);

    return template
         // findAndModify:
         // Find/modify/get the "new object" from a single operation.
         .findAndModify(
              project, updateCountryList,
              new FindAndModifyOptions().returnNew(true), Project.class
                       );

  }

  public Mono<ProjectChild> AddCritTemplChild(String projectId, Mono<Task> task) {

    Query project = new Query();
    project.addCriteria(Criteria.where("_id")
                                .is(projectId));

    return task.flatMap(item -> {
      Update pushItem = new Update();
      pushItem.push("tasks", item);
      return template
           // findAndModify:
           // Find/modify/get the "new object" from a single operation.
           .findAndModify(
                project, pushItem,
                new FindAndModifyOptions().returnNew(true), ProjectChild.class
                         );
    });
  }

  public Mono<ProjectChild> UpdateCritTemplChild(
       String projectId,
       String taskIdToUpdate,
       String ownername) {

    Query project = new Query();
    project.addCriteria(Criteria.where("_id")
                                .is(projectId));
    project.addCriteria(Criteria.where("tasks._id")
                                .is(taskIdToUpdate));

    Update updateTasksList = new Update();
    updateTasksList.set("tasks.$.ownername", ownername);

    return template
         // findAndModify:
         // Find/modify/get the "new object" from a single operation.
         .findAndModify(
              project, updateTasksList,
              new FindAndModifyOptions().returnNew(true), ProjectChild.class
                       )
         ;

  }

  public Mono<Boolean> existTheTaskInProjectChild(String projectId, String taskId) {

    Query project = new Query();
    project.addCriteria(Criteria.where("_id")
                                .is(projectId));
    project.addCriteria(Criteria.where("tasks._id")
                                .is(taskId));

    return template.exists(project, ProjectChild.class);
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

  public Flux<ProjectChild> findAllTemplProjectChild() {

    return template.findAll(ProjectChild.class);
  }

}