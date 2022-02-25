package com.webflux.api.modules.task.repo;


import com.webflux.api.modules.task.entity.Task;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository("taskRepo")
public interface ITaskRepo extends ReactiveMongoRepository<Task, String> {

}