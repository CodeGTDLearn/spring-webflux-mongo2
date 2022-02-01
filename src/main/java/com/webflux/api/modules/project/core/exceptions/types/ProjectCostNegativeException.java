package com.webflux.api.modules.project.core.exceptions.types;

import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serializable;

import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;

@ResponseStatus(NOT_ACCEPTABLE)
public class ProjectCostNegativeException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = - 4546758104179118397L;

    public ProjectCostNegativeException(String message) {

    super(message);
  }
}