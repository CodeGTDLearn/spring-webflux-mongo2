package com.webflux.mongo2.project.repo;

import com.webflux.mongo2.project.entity.Project;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository("crud")
public interface ICrud extends ReactiveCrudRepository<Project, String> {
  /*╔══════════════════════════════╗
    ║     AUTO-GENERATED QUERY     ║
    ╚══════════════════════════════╝*/
  //{"name" : name}
  Flux<Project> findByName(String name);
}