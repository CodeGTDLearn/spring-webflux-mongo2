package com.webflux.api.modules.project.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.webflux.api.modules.project.entity.Project;
import com.webflux.api.modules.project.repo.IRepo;
import com.webflux.api.modules.task.ITaskRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service("serviceRepo")
public class ServiceRepo implements IServiceRepo {

  @Autowired
  IRepo repo;

  @Autowired
  ITaskRepo taskRepo;


  /*╔══════════════════════════════╗
    ║   REACTIVE-MONGO-REPOSITORY  ║
    ╚══════════════════════════════╝*/
  @Override
  public Mono<Project> save(Project project) {

    return repo.save(project);
  }


  @Override
  public Flux<Project> findAll() {

    return repo.findAll();
  }


  @Override
  public Mono<Project> findById(String id) {

    return repo.findById(id);
  }


  @Override
  public Mono<Void> deleteById(String id) {

    return repo.deleteById(id);
  }


  @Override
  public Flux<Project> findByNameNot(String name) {

    return repo.findByNameNot(name);
  }


  @Override
  public Flux<Project> findByEstimatedCostGreaterThan(Long cost) {

    return repo.findByEstimatedCostGreaterThan(cost);
  }


  @Override
  public Flux<Project> findByEstimatedCostBetween(Long from, Long to) {

    return repo.findByEstimatedCostBetween(from, to);
  }


  @Override
  public Flux<Project> findByNameLike(String name) {

    return repo.findByNameLike(name);
  }


  @Override
  public Flux<Project> findByNameRegex(String name) {

    return repo.findByNameRegex(name);
  }


  /*╔══════════════════════════════╗
    ║        ANNOTATED-QUERY       ║
    ╚══════════════════════════════╝*/
  @Override
  public Flux<Project> findProjectByNameQuery(String name) {

    return repo.findProjectByNameQuery(name);
  }


  @Override
  public Flux<Project> findProjectByNameAndCostQuery(String name, Long cost) {

    return repo.findProjectByNameAndCostQuery(name, cost);
  }


  @Override
  public Flux<Project> findByEstimatedCostBetweenQuery(Long from, Long to) {

    return repo.findByEstimatedCostBetweenQuery(from, to,
                                                Sort.by(Direction.DESC, "cost")
                                               );
  }


  @Override
  public Flux<Project> findByNameRegexQuery(String regexp) {

    return repo.findByNameRegexQuery(regexp);
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
      objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

      p = objectMapper.readValue(json, Project.class);
    } catch (Exception i) {

      throw new RuntimeException(i);
    }
    return p;
  }

}