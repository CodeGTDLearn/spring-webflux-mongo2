package com.webflux.api.modules.project.core.exceptions.types;

import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serializable;

import static org.springframework.http.HttpStatus.*;

@ResponseStatus(NOT_FOUND)
public class ProjectNotFoundException extends RuntimeException implements Serializable {

  private static final long serialVersionUID = - 8894682964071077679L;

  public ProjectNotFoundException(String message) {
    super(message);
  }
}