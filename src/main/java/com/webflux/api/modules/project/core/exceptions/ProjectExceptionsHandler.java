package com.webflux.api.modules.project.core.exceptions;

import com.webflux.api.modules.project.core.exceptions.types.ProjectCostNegativeException;
import com.webflux.api.modules.project.core.exceptions.types.ProjectNameEmptyException;
import com.webflux.api.modules.project.core.exceptions.types.ProjectNameSizeIncorretException;
import com.webflux.api.modules.project.core.exceptions.types.ProjectNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;

@ControllerAdvice(annotations = {RestController.class})
@AllArgsConstructor
public class ProjectExceptionsHandler {

  @ExceptionHandler(ProjectCostNegativeException.class)
  public ResponseEntity<?> responseException(ProjectCostNegativeException exception) {

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

  @ExceptionHandler(ProjectNameEmptyException.class)
  public ResponseEntity<?> responseException(ProjectNameEmptyException exception) {

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

  @ExceptionHandler(ProjectNameSizeIncorretException.class)
  public ResponseEntity<?> responseException(ProjectNameSizeIncorretException exception) {

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

  @ExceptionHandler(ProjectNotFoundException.class)
  public ResponseEntity<?> responseException(ProjectNotFoundException exception) {

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