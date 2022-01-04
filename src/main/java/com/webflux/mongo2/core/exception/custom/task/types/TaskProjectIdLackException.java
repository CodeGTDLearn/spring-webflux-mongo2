package com.webflux.mongo2.core.exception.custom.task.types;

import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serializable;

import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;

@ResponseStatus(NOT_ACCEPTABLE)
public class TaskProjectIdLackException extends RuntimeException implements Serializable {


  private static final long serialVersionUID = - 3999988295319075185L;

  public TaskProjectIdLackException(String message) {

    super(message);
  }
}