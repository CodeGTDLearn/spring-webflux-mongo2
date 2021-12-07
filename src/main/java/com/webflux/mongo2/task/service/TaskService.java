package com.webflux.mongo2.task.service;

import com.webflux.mongo2.task.entity.Task;
import com.webflux.mongo2.task.repo.ITaskRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("taskService")
public class TaskService implements ITaskService {

  @Autowired
  ITaskRepo taskRepo;


  @Override
  public Mono<Task> createTask(Task task) {
    return taskRepo.save(task);
  }

}