package com.webflux.api.modules.project.service;

import com.webflux.api.modules.project.entity.Project;
import com.webflux.api.modules.task.ITaskRepo;
import com.webflux.api.modules.task.Task;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service("serviceTransaction")
@AllArgsConstructor
public class ServiceTransaction implements IServiceTransaction {

  IServiceRepo serviceRepo;

  ITaskRepo taskRepo;

  @Override
  @Transactional
  public Mono<Void> saveProjectAndTask(Project project, Task task) {

    return
         serviceRepo
              .save(project)
              .then(taskRepo.save(task))
              .then();
  }

}