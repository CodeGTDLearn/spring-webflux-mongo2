package com.webflux.mongo2.project.repo;

import com.webflux.mongo2.project.entity.ProjectChild;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository("repoChild")
public interface IRepoProjectChild extends ReactiveMongoRepository<ProjectChild, String> {


}