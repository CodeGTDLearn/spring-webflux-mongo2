package com.webflux.api.modules.project.core.exceptions.types;

import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;
import java.io.Serializable;

import static org.springframework.http.HttpStatus.NOT_FOUND;
/*
 ==> EXCEPTIONS IN CONTROLLER:
 *** REASON: IN WEBFLUX, EXCEPTIONS MUST BE IN CONTROLLER - WHY?
     - "Como stream pode ser manipulado por diferentes grupos de thread,
     - caso um erro aconteça em uma thread que não é a que operou a controller,
     - o ControllerAdvice não vai ser notificado "
     - https://medium.com/nstech/programa%C3%A7%C3%A3o-reativa-com-spring-boot-webflux-e-mongodb-chega-de-sofrer-f92fb64517c3
*/
@ResponseStatus(NOT_FOUND)
public class ProjectNotFoundException extends RuntimeException implements Serializable {

  @Serial
  private static final long serialVersionUID = - 8894682964071077679L;

  public ProjectNotFoundException(String message) {

    super(message);
  }
}