package com.webflux.mongo2.project.service;


import com.webflux.mongo2.project.Project;
import com.webflux.mongo2.project.ProjectChild;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IServiceTemplAdvanced {

  Mono<Project> UpdateCritTemplArray(String id, String country);


  Mono<ProjectChild> UpdateCritTemplChild(String id, String idch, String ownername);


  Mono<Void> deleteWithCriteriaTemplateMult(String id);


  Flux<ProjectChild> DeleteCritTemplChild(String id, String idch);


  Mono<Project> DeleteCritTemplArray(String id, String country);
}