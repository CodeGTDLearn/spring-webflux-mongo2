package com.webflux.api.modules.project.service;

import com.webflux.api.modules.project.entity.Project;
import com.webflux.api.modules.task.ITaskRepo;
import com.webflux.api.modules.task.Task;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("serviceTransaction")
@AllArgsConstructor
public class ServiceTransaction implements IServiceTransaction {

  IServiceRepo serviceRepo;

  ITaskRepo taskRepo;

//  @Transactional
  @Override
  public Mono<Task> saveProjectAndTaskTransaction(Project project, Task task) {

    return
         serviceRepo
              .save(project)
              .flatMap(proj -> {
                task.setProjectId(proj.get_id());
                return Mono.just(task);
              })
              .flatMap(task1 -> taskRepo.save(task1))
         ;
  }

}