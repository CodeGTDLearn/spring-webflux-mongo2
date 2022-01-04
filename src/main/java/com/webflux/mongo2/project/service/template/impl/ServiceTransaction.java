package com.webflux.mongo2.project.service.template.impl;

import com.webflux.mongo2.project.entity.Project;
import com.webflux.mongo2.project.service.IServiceRepo;
import com.webflux.mongo2.project.service.template.IServiceTransaction;
import com.webflux.mongo2.task.Task;
import com.webflux.mongo2.task.repo.ITaskRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service("serviceTransaction")
public class ServiceTransaction implements IServiceTransaction {

  @Autowired
  IServiceRepo serviceRepo;

  @Autowired
  ITaskRepo taskRepo;

  @Override
  @Transactional
  public Mono<Void> saveProjectAndTask(Mono<Project> projectMono, Mono<Task> taskMono) {

    return
         projectMono
              .flatMap(serviceRepo::save)
              .then(taskMono)
              .flatMap(taskRepo::save)
              .then();

  }

}