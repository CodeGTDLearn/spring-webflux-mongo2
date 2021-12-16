package com.webflux.mongo2.task.service;

import com.webflux.mongo2.task.Task;
import com.webflux.mongo2.task.repo.ITaskRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service("taskService")
public class ServiceTask implements IServiceTask {

  @Autowired
  ITaskRepo taskRepo;


  @Override
  public Mono<Task> createTask(Task task) {

    return taskRepo.save(task);
  }

  @Override
  public Flux<Task> findAll() {

    return taskRepo.findAll();
  }

//  @Override
//  public Mono<Void> deleteById(String id) {
//
//    return taskRepo.deleteById(id);
//  }

}