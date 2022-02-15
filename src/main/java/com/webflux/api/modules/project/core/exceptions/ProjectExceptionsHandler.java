package com.webflux.api.modules.project.core.exceptions;

import com.webflux.api.modules.project.core.exceptions.types.ProjectNameIsEmptyException;
import com.webflux.api.modules.project.core.exceptions.types.ProjectNotFoundException;
import com.webflux.api.modules.project.core.exceptions.types.UpdateOptmisticVersionException;
import com.webflux.api.modules.project.core.exceptions.types.UpdateSimpleException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice(annotations = {RestController.class})
@AllArgsConstructor
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

  @ExceptionHandler(ProjectNameIsEmptyException.class)
  public ResponseEntity<?> projectNameIsEmptyException(ProjectNameIsEmptyException exception) {

    ProjectExceptionsAttributes exceptionAttributes =
         new ProjectExceptionsAttributes(
              exception.getMessage(),
              exception.getClass()
                       .getName(),
              NOT_ACCEPTABLE.value(),
              new Date().getTime()
         );
    return new ResponseEntity<>(exceptionAttributes, NOT_ACCEPTABLE);
  }
}