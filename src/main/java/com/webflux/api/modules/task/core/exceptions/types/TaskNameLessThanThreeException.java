package com.webflux.api.modules.task.core.exceptions.types;

import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;
import java.io.Serializable;

import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;

@ResponseStatus(NOT_ACCEPTABLE)
public class TaskNameLessThanThreeException extends RuntimeException implements Serializable {

  @Serial
  private static final long serialVersionUID = 5302918459702447598L;

  public TaskNameLessThanThreeException(String message) {
    super(message);
  }
}