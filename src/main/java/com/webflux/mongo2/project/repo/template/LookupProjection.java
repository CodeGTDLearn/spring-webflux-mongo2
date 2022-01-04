package com.webflux.mongo2.project.repo.template;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository("lookupProjection")
public class LookupProjection {

  @Autowired
  ReactiveMongoTemplate template;

    public <T> Flux<T> aggreg(
         Aggregation aggregation,
         String collection,
         Class<T> classe) {

      return template.aggregate(aggregation, collection, classe);
    }
}