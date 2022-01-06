package com.webflux.api.modules.project.service.template.impl;

import com.webflux.api.modules.project.dto.ResultProjectTasks;
import com.webflux.api.modules.project.repo.template.LookupProjection;
import com.webflux.api.modules.project.service.template.IServiceLookupProjection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.UnwindOperation;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Service("serviceLookupProjection")
public class ServiceLookupProjection implements IServiceLookupProjection {

  @Autowired
  LookupProjection lookupProjection;

  @Override
  public Flux<ResultProjectTasks> findAllProjectTasks() {

    LookupOperation lookup = LookupOperation
         .newLookup()
         .from("task")
         .localField("_id")
         .foreignField("pid")
         .as("ProjectTasks");

    UnwindOperation unwind = unwind("ProjectTasks");

    ProjectionOperation
         projection =
         project()
              .andExpression("_id")
              .as("_id")
              .andExpression("name")
              .as("name")
              .andExpression("ProjectTasks.name")
              .as("taskName")
              .andExpression("ProjectTasks.ownername")
              .as("taskOwnerName");

    Aggregation aggregation =
         newAggregation(
              lookup,
              unwind,
              projection
                       );

    return lookupProjection
         .aggreg(
              aggregation,
              "project",
              ResultProjectTasks.class
                );

  }

}