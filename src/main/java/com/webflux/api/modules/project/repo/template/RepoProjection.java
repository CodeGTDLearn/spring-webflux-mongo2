package com.webflux.api.modules.project.repo.template;

import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository("repoProjection")
@AllArgsConstructor
public class RepoProjection {


  ReactiveMongoTemplate template;

    public <T> Flux<T> aggreg(
         Aggregation aggregation,
         String collection,
         Class<T> classe) {

      return template.aggregate(aggregation, collection, classe);
    }
}