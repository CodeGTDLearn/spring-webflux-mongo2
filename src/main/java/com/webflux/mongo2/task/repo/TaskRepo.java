package com.webflux.mongo2.task.repo;


import com.webflux.mongo2.task.Task;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface TaskRepo extends ReactiveMongoRepository<Task, String> {

}