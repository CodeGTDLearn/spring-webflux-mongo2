package com.webflux.api.modules.project.repo;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository("repoProjection")
@RequiredArgsConstructor
public class TemplProjection {


  private final ReactiveMongoTemplate reactiveMongoTemplate;

  public <T> Flux<T> aggreg(
       Aggregation aggregation,
       String collection,
       Class<T> classe) {

    return reactiveMongoTemplate.aggregate(aggregation, collection, classe);
  }
}