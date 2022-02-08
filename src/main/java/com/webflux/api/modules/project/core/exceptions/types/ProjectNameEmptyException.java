package com.webflux.api.modules.project.core.exceptions.types;

import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serializable;

import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;

@ResponseStatus(NOT_ACCEPTABLE)
public class ProjectNameEmptyException extends RuntimeException implements Serializable {

  private static final long serialVersionUID = 2107293117911837311L;

  public ProjectNameEmptyException(String message) {

    super(message);
  }
}