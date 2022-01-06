package com.webflux.api.modules.project.repo.template;

import com.webflux.api.modules.project.entity.Project;
import com.webflux.api.modules.project.entity.ProjectChild;
import com.webflux.api.modules.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository("childArray")
public class ChildArray {

  @Autowired
  ReactiveMongoTemplate template;

  /*╔══════════════════════════════════╗
    ║ REACTIVE-MONGO-TEMPLATE-CRITERIA ║
    ╚══════════════════════════════════╝*/

  public Mono<Project> AddCritTemplArray(String id, String country) {

    Query project = new Query();
    project.addCriteria(Criteria.where("_id")
                                .is(id));

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

  public Mono<Project> updateCritTemplArray(String id, String country, String newcountry) {

    Query project = new Query();
    project.addCriteria(Criteria.where("_id")
                                .is(id));

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

  public Mono<Project> DeleteCritTemplArray(String id, String country) {

    Query project = new Query();
    project.addCriteria(Criteria.where("_id")
                                .is(id));

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

  public Mono<ProjectChild> AddCritTemplChild(String id, Mono<Task> task) {

    Query project = new Query();
    project.addCriteria(Criteria.where("_id")
                                .is(id));

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
       String id, String idch, String ownername) {

    Query project = new Query();
    project.addCriteria(Criteria.where("_id")
                                .is(id));
    project.addCriteria(Criteria.where("tasks._id")
                                .is(idch));

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