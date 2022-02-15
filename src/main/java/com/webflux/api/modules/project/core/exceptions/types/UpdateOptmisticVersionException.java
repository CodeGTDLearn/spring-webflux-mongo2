package com.webflux.api.modules.project.core.exceptions.types;

import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serializable;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ResponseStatus(BAD_REQUEST)
public class UpdateOptmisticVersionException extends RuntimeException implements Serializable {

  private static final long serialVersionUID = 2850705722823547736L;

  public UpdateOptmisticVersionException(String message) {
    super(message);
  }
}