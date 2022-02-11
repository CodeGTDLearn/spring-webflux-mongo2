package com.webflux.api.modules.task.core.exceptions.types;

import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serializable;

import static org.springframework.http.HttpStatus.*;

@ResponseStatus(NOT_FOUND)
public class TaskNotFoundException extends RuntimeException implements Serializable {

  private static final long serialVersionUID = - 3999988295319075185L;

  public TaskNotFoundException(String message) {

    super(message);
  }
}