package config.utils;

import com.webflux.mongo2.project.entity.Project;
import com.webflux.mongo2.project.repo.IProjectRepo;
import com.webflux.mongo2.task.entity.Task;
import com.webflux.mongo2.task.repo.ITaskRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

@Slf4j
public class TestDbUtils {

  @Autowired
  IProjectRepo projectRepo;

  @Autowired
  ITaskRepo taskRepo;


  public void countAndExecuteProjectFlux(Flux<Project> flux,int totalElements) {
    StepVerifier
         .create(flux)
         .expectSubscription()
         .expectNextCount(totalElements)
         .verifyComplete();
  }


  public void countProjectElementsDb(int totalExpected) {
    StepVerifier
         .create(projectRepo.findAll())
         .expectSubscription()
         .expectNextCount(totalExpected)
         .verifyComplete();
  }


  public void countAndExecuteTaskFlux(Flux<Task> flux,int totalElements) {
    StepVerifier
         .create(flux)
         .expectSubscription()
         .expectNextCount(totalElements)
         .verifyComplete();
  }


  public Flux<Project> saveProjectList(List<Project> projectList) {
    return projectRepo.deleteAll()
                      .thenMany(Flux.fromIterable(projectList))
                      .flatMap(projectRepo::save)
                      .doOnNext(item -> projectRepo.findAll())
                      .doOnNext(item -> System.out.println(
                           "\n--> Saved 'Project' in DB: \n    --> " + item.toString() + "\n"));
  }


  public Flux<Task> saveTaskList(List<Task> taskList) {
    return taskRepo.deleteAll()
                   .thenMany(Flux.fromIterable(taskList))
                   .flatMap(taskRepo::save)
                   .doOnNext(item -> taskRepo.findAll())
                   .doOnNext(item -> System.out.println(
                        "\n--> Saved 'Task' in DB: \n    --> " + item.toString() + "\n"))
         ;
  }


  public void cleanTestDb() {
    StepVerifier
         .create(projectRepo.deleteAll())
         .expectSubscription()
         .verifyComplete();

    StepVerifier
         .create(taskRepo.deleteAll())
         .expectSubscription()
         .verifyComplete();

    System.out.println("\n>==================================================>" +
                            "\n>===============> CLEAN-DB-TO-TEST >===============>" +
                            "\n>==================================================>\n");
  }

}