package com.webflux.mongo2.project.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.webflux.mongo2.project.IProjectRepo;
import com.webflux.mongo2.project.Project;
import com.webflux.mongo2.task.entity.Task;
import com.webflux.mongo2.task.repo.ITaskRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service("projectService")
public class ProjectService implements IProjectService {

  //  @Autowired
  //  private ReactiveGridFsTemplate reactiveGridFsTemplate;

  //  @Autowired
  //  private ReactiveGridFsOperations reactiveGridFsOperations;

  @Autowired
  IProjectRepo projectRepo;

  @Autowired
  ITaskRepo taskRepo;

  @Autowired
  ReactiveMongoTemplate reactiveMongoTemplate;


  @Override
  public Mono<Project> createProject(Project project) {
    return projectRepo.save(project);
  }


  @Override
  public Flux<Project> findAll() {
    return projectRepo.findAll();
  }


  @Override
  public Mono<Project> findById(String id) {
    return projectRepo.findById(id);
  }


  @Override
  public Mono<Void> deleteById(String id) {
    return projectRepo.deleteById(id);
  }


  @Override
  public Flux<Project> findByName(String name) {

    return projectRepo.findByName(name);
  }


  @Override
  public Flux<Project> findByNameNot(String name) {

    return projectRepo.findByNameNot(name);
  }


  @Override
  public Flux<Project> findByEstimatedCostGreaterThan(Long cost) {

    return projectRepo.findByEstimatedCostGreaterThan(cost);
  }


  @Override
  public Flux<Project> findByEstimatedCostBetween(Long from,Long to) {

    return projectRepo.findByEstimatedCostBetween(from,to);
  }


  @Override
  public Flux<Project> findByNameLike(String name) {

    return projectRepo.findByNameLike(name);
  }


  @Override
  public Flux<Project> findByNameRegex(String name) {

    return projectRepo.findByNameRegex(name);
  }


  @Override
  public Flux<Project> findProjectByNameQuery(String name) {

    return projectRepo.findProjectByNameQuery(name);
  }


  @Override
  public Flux<Project> findProjectByNameAndCostQuery(String name,Long cost) {

    return projectRepo.findProjectByNameAndCostQuery(name,cost);
  }


  @Override
  public Flux<Project> findByEstimatedCostBetweenQuery(Long from,Long to) {

    return projectRepo.findByEstimatedCostBetweenQuery(from,to,
                                                       Sort.by(Direction.DESC,"cost")
                                                      );
  }


  @Override
  public Flux<Project> findByNameRegexQuery(String regexp) {

    return projectRepo.findByNameRegexQuery(regexp);
  }


  @Override
  public Flux<Project> findProjectByNameQueryWithTemplate(String name) {

    Query query = new Query();
    query.addCriteria(Criteria.where("name")
                              .is(name));
    return reactiveMongoTemplate.find(query,Project.class);

  }


  @Override
  public Flux<Project> findByEstimatedCostBetweenQueryWithTemplate(Long from,Long to) {

    Query query = new Query();
    query.with(Sort.by(Direction.ASC,"cost"));
    query.addCriteria(Criteria.where("cost")
                              .lt(to)
                              .gt(from));
    return reactiveMongoTemplate.find(query,Project.class);

  }


  @Override
  public Flux<Project> findByNameRegexQueryWithTemplate(String regexp) {

    Query query = new Query();
    query.addCriteria(Criteria.where("name")
                              .regex(regexp));
    return reactiveMongoTemplate.find(query,Project.class);

  }


  @Override
  public Mono<Void> upsertCostWithCriteriaTemplate(String id,Long cost) {
    Query query = new Query();
    query.addCriteria(Criteria.where("_id")
                              .is(id));
    Update update = new Update();
    update.set("cost",cost);
    return reactiveMongoTemplate.upsert(query,update,Project.class)
                                .then();

  }


  @Override
  public Mono<Void> deleteWithCriteriaTemplate(String id) {
    Query query = new Query();
    query.addCriteria(Criteria.where("_id")
                              .is(id));
    return reactiveMongoTemplate.remove(query,Project.class)
                                .then();

  }


  /**
   * db.project.aggregate([{$match:{"cost" : {"$gt" : 2000}}},{ $count:
   * "costly_projects" }]);
   */
  //  @Override
  //  public Mono<Long> findNoOfProjectsCostGreaterThan(Long cost) {
  //
  //    MatchOperation matchStage = Aggregation.match(new Criteria("cost").gt(cost));
  //    CountOperation countStage = Aggregation.count()
  //                                           .as("costly_projects");
  //
  //    Aggregation aggregation = Aggregation.newAggregation(matchStage,countStage);
  //    Flux<ResultCount> output = reactiveMongoTemplate.aggregate(aggregation,"project",
  //                                                               ResultCount.class
  //                                                              );
  //    Flux<Long> resultc = output.map(result -> result.getCostly_projects())
  //                               .switchIfEmpty(Flux.just(0L));
  //    return resultc.take(1)
  //                  .single();
  //
  //  }


  /**
   * db.project.aggregate([ {$match:{"cost" : {"$gt" : 100}}}, {$group: {
   * _id:"$startDate",total:{$sum:"$cost"} } }, {$sort: { "total": -1 } } ]);
   */
  //  @Override
  //  public Flux<ResultByStartDateAndCost> findCostsGroupByStartDateForProjectsCostGreaterThan
  //  (Long cost) {
  //
  //    MatchOperation filterCost = Aggregation.match(new Criteria("cost").gt(cost));
  //    GroupOperation groupByStartDateAndSumCost = Aggregation.group("startDate")
  //                                                           .sum("cost")
  //                                                           .as("total");
  //
  //    SortOperation sortByTotal = Aggregation.sort(Sort.by(Direction.DESC,"total"));
  //
  //    Aggregation aggregation = Aggregation.newAggregation(filterCost,groupByStartDateAndSumCost,
  //                                                         sortByTotal
  //                                                        );
  //    return reactiveMongoTemplate.aggregate(aggregation,"project",ResultByStartDateAndCost
  //         .class);
  //
  //  }


  //  @Override
  //  public Flux<ResultProjectTasks> findAllProjectTasks() {
  //
  //    LookupOperation lookupOperation = LookupOperation.newLookup()
  //                                                     .from("task")
  //                                                     .localField("_id")
  //                                                     .foreignField("pid")
  //                                                     .as("ProjectTasks");
  //    UnwindOperation unwindOperation = Aggregation.unwind("ProjectTasks");
  //    ProjectionOperation projectOpertaion = Aggregation.project()
  //                                                      .andExpression("_id")
  //                                                      .as("_id")
  //                                                      .andExpression("name")
  //                                                      .as("name")
  //                                                      .andExpression("ProjectTasks.name")
  //                                                      .as("taskName")
  //                                                      .andExpression("ProjectTasks.ownername")
  //                                                      .as("taskOwnerName");
  //    Aggregation aggregation = Aggregation.newAggregation(lookupOperation,unwindOperation,
  //                                                         projectOpertaion
  //                                                        );
  //    return reactiveMongoTemplate.aggregate(aggregation,"project",ResultProjectTasks.class);
  //
  //  }
  @Override
  @Transactional
  public Mono<Void> saveProjectAndTask(Mono<Project> p,Mono<Task> t) {

    return p.flatMap(projectRepo::save)
            .then(t)
            .flatMap(taskRepo::save)
            .then();

  }


  //  @Override
  //  public Mono<Void> chunkAndSaveProject(Project p) {
  //    String s = serializetoJson(p);
  //    byte[] serialized = s.getBytes();
  //
  //    DBObject metaData = new BasicDBObject();
  //    metaData.put("projectId",p.get_id());
  //    DefaultDataBufferFactory factory = new DefaultDataBufferFactory();
  //    DefaultDataBuffer dataBuffer =
  //         factory.wrap(serialized);
  //    Flux<DataBuffer> body = Flux.just(dataBuffer);
  //    return reactiveGridFsTemplate.store(body,p.get_id(),metaData)
  //                                 .then();
  //
  //  }


  //  @Override
  //  public Mono<Project> loadProjectFromGrid(String projectId) {
  //    Mono<GridFSFile> file = reactiveGridFsTemplate.findOne(new Query(
  //         Criteria.where("metadata.projectId")
  //                 .is(projectId)).with(Sort.by(
  //                                     Direction.DESC,"uploadDate"))
  //                                .limit(1));
  //
  //    Flux<byte[]> bytesseq = file.flatMap(f -> reactiveGridFsOperations.getResource(f))
  //                                .flatMapMany(rgrs -> rgrs.getDownloadStream())
  //                                .map(buffer -> {
  //
  //                                  byte[] b = new byte[buffer.readableByteCount()];
  //                                  buffer.read(b);
  //                                  return b;
  //                                });
  //
  //
  //    Mono<Project> totalbyte = bytesseq.collectList()
  //                                      .flatMap(bytes -> {
  //                                        byte[] data = Bytes.concat(
  //                                             bytes.toArray(new byte[bytes.size()][]));
  //                                        String s = new String(data,StandardCharsets.UTF_8);
  //
  //                                        return Mono.just(deserialize(s));
  //                                      });
  //
  //    return totalbyte;
  //  }


  //  @Override
  //  public Mono<Void> deleteProjectFromGrid(String projectId) {
  //    return reactiveGridFsTemplate.delete(new Query(Criteria.where("metadata.projectId")
  //                                                           .is(projectId)));
  //  }


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
      objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);

      p = objectMapper.readValue(json,Project.class);
    } catch (Exception i) {

      throw new RuntimeException(i);
    }
    return p;
  }

}