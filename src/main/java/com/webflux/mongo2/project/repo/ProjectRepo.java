package com.webflux.mongo2.project.repo;

import com.webflux.mongo2.project.Project;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository("projectRepo")
public interface ProjectRepo extends ReactiveMongoRepository<Project, String> {

  //{"name" : name}
  Flux<Project> findByName(String name);

  //{"name" : {"$ne" : name}}
  Flux<Project> findByNameNot(String name);

  //{"cost" : {"$gt" : cost}}
  Flux<Project> findByEstimatedCostGreaterThan(Long cost);

  //{"cost" : {"$gt" : from, "$lt" : to}}

  Flux<Project> findByEstimatedCostBetween(Long from,Long to);

  //{"name": /name/ }
  Flux<Project> findByNameLike(String name);


  //{"name" : {"$regex" : name }} =>/^Pro/
  Flux<Project> findByNameRegex(String name);


  @Query("{'name' : ?0}")
  Flux<Project> findProjectByNameQuery(String name);


  @Query("{'name' : ?0 , 'cost' : ?1}")
  Flux<Project> findProjectByNameAndCostQuery(String name,Long cost);


  @Query("{cost : {$lt : ?1, $gt : ?0}}")
  Flux<Project> findByEstimatedCostBetweenQuery(
       Long from,Long to,
       org.springframework.data.domain.Sort sort);


  @Query(value = "{ 'name' : { $regex: ?0 } }", fields = "{'name' : 1,'cost':1}")
  Flux<Project> findByNameRegexQuery(String regexp);


}