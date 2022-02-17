package com.webflux.api.modules.project.service;

import com.webflux.api.modules.project.core.exceptions.ProjectExceptionsThrower;
import com.webflux.api.modules.project.entity.Project;
import com.webflux.api.modules.task.ITaskRepo;
import com.webflux.api.modules.task.Task;
import com.webflux.api.modules.task.core.exceptions.TaskExceptionsThrower;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service("serviceTransaction")
@AllArgsConstructor
public class ServiceTransaction implements IServiceTransaction {

  private final ProjectExceptionsThrower projectThrower;
  private final TaskExceptionsThrower taskThrower;
  IServiceRepo serviceRepo;
  ITaskRepo taskRepo;

  /*
  EXCEPTIONS:
  A) SERVICE: BLOW-UP EXCEPTIONS IN THE SERVICE
  B) CONTROLLER: TREAT/HANDLE EXCEPTIONS IN THE CONTROLLER(ON-ERROR-RESUME)
   */
  @Transactional
  @Override
  public Mono<Task> createProjectTransaction(Project project, Task task) {
    // @formatter:off
    return
         serviceRepo
              .save(project)
              .flatMap(proj -> {
                task.setProjectId(proj.get_id());
                return Mono.just(proj);
              })
              .flatMap(proj1 ->{
                if (proj1.getName().isEmpty()) return projectThrower.throwProjectNameIsEmptyException();
                return Mono.just(task);
              })
              .flatMap(task1 -> taskRepo.save(task1))
              .flatMap(task2 -> {
                if (task2.getName().isEmpty()) return taskThrower.throwTaskNameIsEmptyException();
                if (task2.getName().length() < 3) return taskThrower.throwTaskNameLessThanThreeException();
                return Mono.just(task2);
              })
         ;
    // @formatter:on
  }

}