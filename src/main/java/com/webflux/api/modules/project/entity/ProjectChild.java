package com.webflux.api.modules.project.entity;

import com.webflux.api.modules.task.entity.Task;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Document(collection = "projectchild")
public class ProjectChild implements Serializable {
  private static final long serialVersionUID = 2311041330320264951L;

  @Id
  private String _id;

  private String name;

  private String code;

  @Field("desc")
  private String description;

  private String startDate;

  private String endDate;

  @Field("cost")
  private long estimatedCost;

  private List<String> countryList;

  private List<Task> tasks;

  @Version
  private Long version;
}