package config.utils;

import com.webflux.api.modules.project.entity.Project;
import com.webflux.api.modules.project.entity.ProjectChild;
import com.webflux.api.modules.project.repo.IRepo;
import com.webflux.api.modules.project.repo.IRepoProjectChild;
import com.webflux.api.modules.project.repo.template.RepoColections;
import com.webflux.api.modules.task.Task;
import com.webflux.api.modules.task.ITaskRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class TestDbUtils {

  @Autowired
  IRepo projectRepo;

  @Autowired
  IRepoProjectChild repoChild;

  @Autowired
  ITaskRepo taskRepo;

  @Autowired
  RepoColections collections;


  public <E> void countAndExecuteFlux(Flux<E> flux, int totalElements) {
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

  public Flux<ProjectChild> saveProjectChildList(List<ProjectChild> projectList) {
    return repoChild.deleteAll()
                      .thenMany(Flux.fromIterable(projectList))
                      .flatMap(repoChild::save)
                      .doOnNext(item -> repoChild.findAll())
                      .doOnNext(item -> System.out.println(
                           "\n--> Saved 'ProjectChild' in DB: \n    --> " + item.toString() + "\n"));
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

  public <E> void checkFluxListElements(Flux<E> listFlux, List<E> listCompare) {
    StepVerifier.create(listFlux)
                .recordWith(ArrayList::new)
                .expectNextCount(listCompare.size())
                .thenConsumeWhile(listCompare::equals)
                .verifyComplete();
  }

  public void cleanTestDb() {
    StepVerifier
         .create(collections.dropCollectionsTemplate())
         .expectSubscription()
         .verifyComplete();

//    StepVerifier
//         .create(projectRepo.deleteAll())
//         .expectSubscription()
//         .verifyComplete();
//
//    StepVerifier
//         .create(repoChild.deleteAll())
//         .expectSubscription()
//         .verifyComplete();
//
//    StepVerifier
//         .create(taskRepo.deleteAll())
//         .expectSubscription()
//         .verifyComplete();

    System.out.println("\n>==================================================>" +
                            "\n>===============> CLEAN-DB-TO-TEST >===============>" +
                            "\n>==================================================>\n");
  }

}