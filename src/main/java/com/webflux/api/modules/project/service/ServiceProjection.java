package com.webflux.api.modules.project.service;

import com.webflux.api.modules.project.core.dto.ProjectDto;
import com.webflux.api.modules.project.repo.TemplProjection;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;

@Service("serviceProjection")
@AllArgsConstructor
public class ServiceProjection implements IServiceProjection {


  TemplProjection repoProjection;

  @Override
  public Flux<ProjectDto> findAllProjectDto() {

    ProjectionOperation projection =
         project()
              .andExpression("name")
              .as("name")
              .andExpression("desc")
              .as("description");

    Aggregation aggregation = newAggregation(projection);

    return this.repoProjection.aggreg(aggregation, "project", ProjectDto.class);

  }

}