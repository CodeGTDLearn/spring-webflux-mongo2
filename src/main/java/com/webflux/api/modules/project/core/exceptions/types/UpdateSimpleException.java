package com.webflux.api.modules.project.core.exceptions.types;

import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serializable;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ResponseStatus(INTERNAL_SERVER_ERROR)
public class UpdateSimpleException extends RuntimeException implements Serializable {

  private static final long serialVersionUID = - 5789512662465578333L;

  public UpdateSimpleException(String message) {
    super(message);
  }
}