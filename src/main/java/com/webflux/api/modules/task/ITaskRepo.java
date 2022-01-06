package com.webflux.api.modules.task;


import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository("taskRepo")
public interface ITaskRepo extends ReactiveMongoRepository<Task, String> {

}