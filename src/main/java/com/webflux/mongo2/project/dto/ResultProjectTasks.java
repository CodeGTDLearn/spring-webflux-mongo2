package com.webflux.mongo2.project.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResultProjectTasks {
  private String _id;
  private String name;
  private String taskName;
  private String taskOwnerName;
}