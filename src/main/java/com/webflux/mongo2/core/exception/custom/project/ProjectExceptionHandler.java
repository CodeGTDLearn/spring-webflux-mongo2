package com.webflux.mongo2.core.exception.custom.project;

import com.webflux.mongo2.core.exception.custom.CustomExceptionAttributes;
import com.webflux.mongo2.core.exception.custom.project.types.ProjectCostNegativeException;
import com.webflux.mongo2.core.exception.custom.project.types.ProjectNameNotFoundException;
import com.webflux.mongo2.core.exception.custom.project.types.ProjectNameSizeIncorretException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;

@ControllerAdvice(annotations = {RestController.class})
@AllArgsConstructor
public class ProjectExceptionHandler {

  @ExceptionHandler(ProjectNameNotFoundException.class)
  public ResponseEntity<?> CustomExceptionWithCustomAttributes(ProjectNameNotFoundException exception) {

    CustomExceptionAttributes customExceptionAttributes =
         new CustomExceptionAttributes(
              exception.getMessage(),
              exception.getClass()
                       .getName(),
              NOT_ACCEPTABLE.value(),
              new Date().getTime()
         );
    return new ResponseEntity<>(customExceptionAttributes, NOT_ACCEPTABLE);
  }

  @ExceptionHandler(ProjectCostNegativeException.class)
  public ResponseEntity<?> CustomExceptionWithCustomAttributes(ProjectCostNegativeException exception) {

    CustomExceptionAttributes customExceptionAttributes =
         new CustomExceptionAttributes(
              exception.getMessage(),
              exception.getClass()
                       .getName(),
              NOT_ACCEPTABLE.value(),
              new Date().getTime()
         );
    return new ResponseEntity<>(customExceptionAttributes, NOT_ACCEPTABLE);
  }

  @ExceptionHandler(ProjectNameSizeIncorretException.class)
  public ResponseEntity<?> CustomExceptionWithCustomAttributes(ProjectNameSizeIncorretException exception) {

    CustomExceptionAttributes customExceptionAttributes =
         new CustomExceptionAttributes(
              exception.getMessage(),
              exception.getClass()
                       .getName(),
              NOT_ACCEPTABLE.value(),
              new Date().getTime()
         );
    return new ResponseEntity<>(customExceptionAttributes, NOT_ACCEPTABLE);
  }
}