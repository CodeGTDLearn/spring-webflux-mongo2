package com.webflux.api.modules.project.core.exceptions;

import com.webflux.api.modules.project.core.exceptions.types.ProjectNotFoundException;
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
  public ResponseEntity<?> responseException(ProjectNotFoundException exception) {

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
}