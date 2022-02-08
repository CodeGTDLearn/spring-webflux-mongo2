package com.webflux.api.modules.project.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Document(collection = "project")
public class Project implements Serializable {

  private static final long serialVersionUID = - 1403327247460781703L;

  @Id
  private String _id;

  @NotEmpty
  private String name;

  private String code;

  @Field("desc")
  private String description;

  private String startDate;

  private String endDate;

  @Field("cost")
  private long estimatedCost;
  
  private List<String> countryList;

  @Version
  private Long version;
}