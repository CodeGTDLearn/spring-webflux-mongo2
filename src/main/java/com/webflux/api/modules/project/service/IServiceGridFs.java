//package com.webflux.api.modules.project.service;
//
//import com.fasterxml.jackson.databind.DeserializationFeature;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.webflux.api.modules.project.entity.Project;
//import reactor.core.publisher.Mono;
//
//public interface IServiceGridFs {
//  Mono<Void> chunkAndSaveProject(Project p);
//
//
//  Mono<Project> loadProjectFromGrid(String projectId);
//
//
//  Mono<Void> deleteProjectFromGrid(String projectId);
//
//
//  default String serializetoJson(Project p) {
//
//    try {
//      ObjectMapper mapper = new ObjectMapper();
//      return mapper.writerWithDefaultPrettyPrinter()
//                   .writeValueAsString(p);
//
//    }
//    catch (Exception e) {
//      e.printStackTrace();
//    }
//    return null;
//  }
//
//
//  default Project deserialize(String json) {
//
//    Project p = null;
//    try {
//
//      ObjectMapper objectMapper = new ObjectMapper();
//      objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//
//      p = objectMapper.readValue(json, Project.class);
//    }
//    catch (Exception i) {
//
//      throw new RuntimeException(i);
//    }
//    return p;
//  }
//}