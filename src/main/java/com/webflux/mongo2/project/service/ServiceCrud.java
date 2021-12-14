package com.webflux.mongo2.project.service;

import com.webflux.mongo2.project.Project;
import com.webflux.mongo2.project.repo.ICrud;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service("serviceCrud")
public class ServiceCrud implements IServiceCrud {

  @Autowired
  ICrud crud;


  /*╔══════════════════════════════╗
    ║   REACTIVE-MONGO-REPOSITORY  ║
    ╚══════════════════════════════╝*/
  @Override
  public Mono<Project> createProject(Project project) {

    return crud.save(project);
  }


  @Override
  public Flux<Project> findAll() {

    return crud.findAll();
  }


  @Override
  public Mono<Project> findById(String id) {

    return crud.findById(id);
  }


  @Override
  public Mono<Void> deleteById(String id) {

    return crud.deleteById(id);
  }
}