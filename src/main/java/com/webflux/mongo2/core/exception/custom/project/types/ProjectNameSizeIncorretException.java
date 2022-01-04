package com.webflux.mongo2.core.exception.custom.project.types;

import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serializable;

import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;

@ResponseStatus(NOT_ACCEPTABLE)
public class ProjectNameSizeIncorretException extends RuntimeException implements Serializable {


  private static final long serialVersionUID = - 1673068357262919698L;

  public ProjectNameSizeIncorretException(String message) {

    super(message);
  }
}