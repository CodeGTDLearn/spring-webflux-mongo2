package com.webflux.mongo2.project;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository("projectRepo")
public interface IProjectRepo extends ReactiveMongoRepository<Project, String> {


  Flux<Project> findByName(String name); //{"name" : name}

  Flux<Project> findByNameNot(String name);//{"name" : {"$ne" : name}}

  Flux<Project> findByEstimatedCostGreaterThan(Long cost);//{"cost" : {"$gt" : cost}}

  //{"cost" : {"$gt" : from, "$lt" : to}}
  Flux<Project> findByEstimatedCostBetween(Long from, Long to);

  Flux<Project> findByNameLike(String name);//{"name": /name/ }

  Flux<Project> findByNameRegex(String name);//{"name" : {"$regex" : name }} =>/^Pro/

  @Query("{'name' : ?0}")
  Flux<Project> findProjectByNameQuery(String name);


  @Query("{'name' : ?0 , 'cost' : ?1}")
  Flux<Project> findProjectByNameAndCostQuery(String name, Long cost);


  @Query("{cost : {$lt : ?1, $gt : ?0}}")
  Flux<Project> findByEstimatedCostBetweenQuery(
       Long from, Long to,
       org.springframework.data.domain.Sort sort);


  @Query(value = "{ 'name' : { $regex: ?0 } }", fields = "{'name' : 1,'cost':1}")
  Flux<Project> findByNameRegexQuery(String regexp);


}