package com.webflux.api.modules.project.core.exceptions;

import com.webflux.api.modules.project.core.exceptions.types.ProjectNotFoundException;
import com.webflux.api.modules.project.core.exceptions.types.UpdateOptmisticVersionException;
import com.webflux.api.modules.project.core.exceptions.types.UpdateSimpleException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

import static org.springframework.http.HttpStatus.*;

/*
 ==> EXCEPTIONS IN CONTROLLER:
 *** REASON: IN WEBFLUX, EXCEPTIONS MUST BE IN CONTROLLER - WHY?
     - "Como stream pode ser manipulado por diferentes grupos de thread,
     - caso um erro aconteça em uma thread que não é a que operou a controller,
     - o ControllerAdvice não vai ser notificado "
     - https://medium.com/nstech/programa%C3%A7%C3%A3o-reativa-com-spring-boot-webflux-e-mongodb-chega-de-sofrer-f92fb64517c3
*/
@ControllerAdvice(annotations = {RestController.class})
@AllArgsConstructor
@Slf4j
public class ProjectExceptionsHandler {

  @ExceptionHandler(ProjectNotFoundException.class)
  public ResponseEntity<?> projectNotFoundException(ProjectNotFoundException exception) {

    ProjectExceptionsAttributes exceptionAttributes =
         new ProjectExceptionsAttributes(
              exception.getMessage(),
              exception.getClass()
                       .getName(),
              NOT_FOUND.value(),
              new Date().getTime()
         );
    return new ResponseEntity<>(exceptionAttributes, NOT_FOUND);
  }

  @ExceptionHandler(UpdateSimpleException.class)
  public ResponseEntity<?> updateException(UpdateSimpleException exception) {

    ProjectExceptionsAttributes exceptionAttributes =
         new ProjectExceptionsAttributes(
              exception.getMessage(),
              exception.getClass()
                       .getName(),
              INTERNAL_SERVER_ERROR.value(),
              new Date().getTime()
         );
    return new ResponseEntity<>(exceptionAttributes, INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(UpdateOptmisticVersionException.class)
  public ResponseEntity<?> updateOptmVersionException(UpdateOptmisticVersionException exception) {

    ProjectExceptionsAttributes exceptionAttributes =
         new ProjectExceptionsAttributes(
              exception.getMessage(),
              exception.getClass()
                       .getName(),
              BAD_REQUEST.value(),
              new Date().getTime()
         );
    return new ResponseEntity<>(exceptionAttributes, BAD_REQUEST);
  }
}