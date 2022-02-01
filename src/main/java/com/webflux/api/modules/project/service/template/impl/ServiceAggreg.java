package com.webflux.api.modules.project.service.template.impl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.webflux.api.modules.project.entity.Project;
import com.webflux.api.modules.project.core.dto.ResultByStartDateAndCost;
import com.webflux.api.modules.project.core.dto.ResultCount;
import com.webflux.api.modules.project.repo.template.RepoAggreg;
import com.webflux.api.modules.project.service.template.IServiceAggreg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service("serviceAggreg")
public class ServiceAggreg implements IServiceAggreg {

  @Autowired
  RepoAggreg repoAggreg;


  /**
   * db.project.aggregate([{$match:{"costProject" : {"$gt" : 2000}}},{ $count:
   * "costly_projects" }]);
   */
  @Override
  public Flux<ResultCount> findNoOfProjectsCostGreaterThan(Long projectCost) {

    MatchOperation matchStage =
         Aggregation.match(new Criteria("cost").gt(projectCost));

    CountOperation countStage =
         Aggregation.count()
                    .as("costly_projects");

    Aggregation aggregation =
         Aggregation.newAggregation(matchStage, countStage);

    return
         repoAggreg.aggregs(
              aggregation,
              "project",
              ResultCount.class
                           );

  }


  /**
   * db.project.aggregate([ {$match:{"cost" : {"$gt" : 100}}}, {$group: {
   * _id:"$startDate",total:{$sum:"$cost"} } }, {$sort: { "total": -1 } } ]);
   */
  @Override
  public Flux<ResultByStartDateAndCost> findCostsGroupByStartDateForProjectsCostGreaterThan
  (Long projectCost) {

    MatchOperation filterCost = Aggregation.match(new Criteria("cost").gt(projectCost));

    GroupOperation groupByStartDateAndSumCost =
         Aggregation
              .group("startDate")
              .sum("cost")
              .as("total");

    SortOperation sortByTotal = Aggregation.sort(Sort.by(Direction.DESC, "total"));

    Aggregation aggregation =
         Aggregation
              .newAggregation(
                   filterCost,
                   groupByStartDateAndSumCost,
                   sortByTotal
                             );
    return repoAggreg
         .aggregs(
              aggregation,
              "project",
              ResultByStartDateAndCost.class
                 );
  }

  private String serializetoJson(Project project) {

    try {
      ObjectMapper mapper = new ObjectMapper();
      return mapper.writerWithDefaultPrettyPrinter()
                   .writeValueAsString(project);

    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }


  private Project deserialize(String json) {

    Project p = null;
    try {

      ObjectMapper objectMapper = new ObjectMapper();
      objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

      p = objectMapper.readValue(json, Project.class);
    } catch (Exception i) {

      throw new RuntimeException(i);
    }
    return p;
  }

}