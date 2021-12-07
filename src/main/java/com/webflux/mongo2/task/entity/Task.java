package com.webflux.mongo2.task.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@Document(collection = "task")
public class Task {

  @Id
  private String _id;

  @Field("pid")
  private String projectId;

  private String name;

  @Field("desc")
  private String description;

  private String ownername;

  private long cost;

  @Version
  private Long version;
}