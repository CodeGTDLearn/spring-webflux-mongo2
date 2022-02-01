package com.webflux.api.modules.project.core.exceptions.types;

import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serializable;

import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;

@ResponseStatus(NOT_ACCEPTABLE)
public class ProjectNotFoundException extends RuntimeException implements Serializable {


  private static final long serialVersionUID = - 8894682964071077679L;

  public ProjectNotFoundException(String message) {

    super(message);
  }
}