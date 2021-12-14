package com.webflux.mongo2.project.repo;

import com.webflux.mongo2.project.Project;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository("crud")
public interface ICrud extends ReactiveCrudRepository<Project, String> {

}