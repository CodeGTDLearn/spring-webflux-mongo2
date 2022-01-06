package com.webflux.api.core.exception.modules.project;

import com.webflux.api.core.exception.modules.ModulesExceptionAttributes;
import com.webflux.api.core.exception.modules.project.types.ProjectCostNegativeException;
import com.webflux.api.core.exception.modules.project.types.ProjectNameNotFoundException;
import com.webflux.api.core.exception.modules.project.types.ProjectNameSizeIncorretException;
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

    ModulesExceptionAttributes customExceptionAttributes =
         new ModulesExceptionAttributes(
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

    ModulesExceptionAttributes customExceptionAttributes =
         new ModulesExceptionAttributes(
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

    ModulesExceptionAttributes customExceptionAttributes =
         new ModulesExceptionAttributes(
              exception.getMessage(),
              exception.getClass()
                       .getName(),
              NOT_ACCEPTABLE.value(),
              new Date().getTime()
         );
    return new ResponseEntity<>(customExceptionAttributes, NOT_ACCEPTABLE);
  }
}