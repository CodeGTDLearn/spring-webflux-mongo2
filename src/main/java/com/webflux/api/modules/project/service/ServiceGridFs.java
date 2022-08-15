//package com.webflux.api.modules.project.service;
//
//import com.fasterxml.jackson.databind.DeserializationFeature;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.google.common.primitives.Bytes;
//import com.mongodb.BasicDBObject;
//import com.mongodb.DBObject;
//import com.mongodb.client.gridfs.model.GridFSFile;
//import com.webflux.api.modules.project.entity.Project;
//import com.webflux.api.modules.project.repo.IRepo;
//import lombok.RequiredArgsConstructor;
//import org.springframework.core.io.buffer.DefaultDataBuffer;
//import org.springframework.core.io.buffer.DefaultDataBufferFactory;
//import org.springframework.data.domain.Sort;
//import org.springframework.data.domain.Sort.Direction;
//import org.springframework.data.mongodb.core.query.Criteria;
//import org.springframework.stereotype.Service;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//
//import java.nio.charset.StandardCharsets;
//
////@Service("serviceGridFs")
////@RequiredArgsConstructor
//public class ServiceGridFs implements IServiceGridFs {
//
//  private final IRepo repo;
//
//
//  @Override
//  public Mono<Void> chunkAndSaveProject(Project p) {
//
//    String s = serializetoJson(p);
//    byte[] serialized = s.getBytes();
//
//    DBObject metaData = new BasicDBObject();
//    metaData.put("projectId", p.get_id());
//    DefaultDataBufferFactory factory = new DefaultDataBufferFactory();
//    DefaultDataBuffer dataBuffer =
//         factory.wrap(serialized);
//    Flux<DataBuffer> body = Flux.just(dataBuffer);
//    return reactiveGridFs.store(body, p.get_id(), metaData)
//                                 .then();
//
//  }
//
//
//  @Override
//  public Mono<Project> loadProjectFromGrid(String projectId) {
//
//    Mono<GridFSFile> file = reactiveGridFsTemplate.findOne(new Query(
//         Criteria.where("metadata.projectId")
//                 .is(projectId)).with(Sort.by(
//                                     Direction.DESC, "uploadDate"))
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
//                                        String s = new String(data, StandardCharsets.UTF_8);
//
//                                        return Mono.just(deserialize(s));
//                                      });
//
//    return totalbyte;
//  }
//
//
//  @Override
//  public Mono<Void> deleteProjectFromGrid(String projectId) {
//
//    return reactiveGridFs.delete(new Query(Criteria.where("metadata.projectId")
//                                                           .is(projectId)));
//  }
//
//
//}