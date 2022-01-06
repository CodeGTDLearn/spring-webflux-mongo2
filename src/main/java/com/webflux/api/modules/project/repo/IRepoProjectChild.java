package com.webflux.api.modules.project.repo;

import com.webflux.api.modules.project.entity.ProjectChild;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository("repoChild")
public interface IRepoProjectChild extends ReactiveMongoRepository<ProjectChild, String> {


}