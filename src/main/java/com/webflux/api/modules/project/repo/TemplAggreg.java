package com.webflux.api.modules.project.repo;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository("repoAggreg")
@RequiredArgsConstructor
public class TemplAggreg {

  private final ReactiveMongoTemplate reactiveRepoTemplate;

  public <T> Flux<T> aggregs(
       Aggregation aggregation,
       String collection,
       Class<T> classe) {

    return reactiveRepoTemplate.aggregate(aggregation, collection, classe);
  }
}