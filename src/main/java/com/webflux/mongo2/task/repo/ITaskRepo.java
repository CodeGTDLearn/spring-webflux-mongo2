package com.webflux.mongo2.task.repo;


import com.webflux.mongo2.task.Task;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository("taskRepo")
public interface ITaskRepo extends ReactiveMongoRepository<Task, String> {

}