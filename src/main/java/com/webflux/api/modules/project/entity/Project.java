package com.webflux.api.modules.project.entity;

import com.webflux.api.modules.project.core.exceptions.ProjectExceptionsMessages;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotEmpty;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Document(collection = "project")
public class Project implements Serializable {

  @Serial
  private static final long serialVersionUID = - 3086158644332115460L;
  private final String NAME_EMPTY = messages.getProjectNameEmptyBeanValidationMessage();

  @Autowired
  private ProjectExceptionsMessages messages;

  @Id
  private String _id;

  @NotEmpty(message = NAME_EMPTY)
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