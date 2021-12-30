package com.webflux.mongo2.project.service.template;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.webflux.mongo2.project.dto.ResultByStartDateAndCost;
import com.webflux.mongo2.project.dto.ResultCount;
import com.webflux.mongo2.project.entity.Project;
import com.webflux.mongo2.project.repo.template.TemplAggreg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service("serviceTemplAggreg")
public class ServiceTemplAggreg implements IServiceTemplAggreg {

  @Autowired
  TemplAggreg templAggreg;


  /**
   * db.project.aggregate([{$match:{"cost" : {"$gt" : 2000}}},{ $count:
   * "costly_projects" }]);
   */
  @Override
  public Mono<Long> findNoOfProjectsCostGreaterThan(Long cost) {

    MatchOperation matchStage = Aggregation.match(new Criteria("cost").gt(cost));

    CountOperation countStage = Aggregation.count()
                                           .as("costly_projects");

    Aggregation aggregation = Aggregation.newAggregation(matchStage, countStage);

    Flux<ResultCount> output =
         templAggreg.aggreg(
              aggregation,
              "project",
              ResultCount.class
                           );

    Flux<Long> resultc =
         output
              .map(result -> result.getCostly_projects())
              .switchIfEmpty(Flux.just(0L));

    return resultc.take(1)
                  .single();

  }


  /**
   * db.project.aggregate([ {$match:{"cost" : {"$gt" : 100}}}, {$group: {
   * _id:"$startDate",total:{$sum:"$cost"} } }, {$sort: { "total": -1 } } ]);
   */
  @Override
  public Flux<ResultByStartDateAndCost> findCostsGroupByStartDateForProjectsCostGreaterThan
  (Long cost) {

    MatchOperation filterCost = Aggregation.match(new Criteria("cost").gt(cost));

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
    return templAggreg
         .aggreg(
              aggregation,
              "project",
              ResultByStartDateAndCost.class
                );
  }

  private String serializetoJson(Project p) {

    try {
      ObjectMapper mapper = new ObjectMapper();
      return mapper.writerWithDefaultPrettyPrinter()
                   .writeValueAsString(p);

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