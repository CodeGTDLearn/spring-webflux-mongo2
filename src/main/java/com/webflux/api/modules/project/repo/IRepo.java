package com.webflux.api.modules.project.repo;

import com.webflux.api.modules.project.entity.Project;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository("repo")
public interface IRepo extends ReactiveMongoRepository<Project, String> {

  /*╔══════════════════════════════╗
    ║     AUTO-GENERATED QUERY     ║
    ╚══════════════════════════════╝*/

  //{"projectName" : {"$ne" : projectName}}
  Flux<Project> findByNameNot(String projectName);

  //{"ProjectCost" : {"$gt" : ProjectCost}}
  Flux<Project> findByEstimatedCostGreaterThan(Long ProjectCost);

  //{"cost" : {"$gt" : from, "$lt" : to}}
  Flux<Project> findByEstimatedCostBetween(Long from, Long to);

  //{"projectName": /projectName/ }
  Flux<Project> findByNameLike(String projectName);

  //{"projectName" : {"$regex" : projectName }} =>/^Pro/
  Flux<Project> findByNameRegex(String projectName);


  /*╔══════════════════════════════╗
    ║        ANNOTATED-QUERY       ║
    ╚══════════════════════════════╝*/
  @Query("{'name' : ?0}")
  Flux<Project> findProjectByNameQuery(String projectName);

  @Query("{'name' : ?0 , 'cost' : ?1}")
  Flux<Project> findProjectByNameAndCostQuery(String projectName, Long projectCost);

  @Query("{cost : {$lt : ?1, $gt : ?0}}")
  Flux<Project> findByEstimatedCostBetweenQuery(
       Long from, Long to,
       org.springframework.data.domain.Sort sort);

  @Query(value = "{ 'name' : { $regex: ?0 } }", fields = "{'name' : 1,'cost':1}")
  Flux<Project> findByNameRegexQuery(String regexpProjectName);
}