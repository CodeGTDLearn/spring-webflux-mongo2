package com.webflux.api.modules.task.core.exceptions;

import com.webflux.api.modules.task.core.exceptions.types.TaskNameIsEmptyException;
import com.webflux.api.modules.task.core.exceptions.types.TaskNameLessThanThreeException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice(annotations = {RestController.class})
@AllArgsConstructor
public class TaskExceptionsHandler {

  @ExceptionHandler(TaskNameIsEmptyException.class)
  public ResponseEntity<?> taskNotFoundException(TaskNameIsEmptyException exception) {

    TaskExceptionsAttributes attributes =
         new TaskExceptionsAttributes(
              exception.getMessage(),
              exception.getClass()
                       .getName(),
              NOT_ACCEPTABLE.value(),
              new Date().getTime()
         );
    return new ResponseEntity<>(attributes, NOT_ACCEPTABLE);
  }

  @ExceptionHandler(TaskNameLessThanThreeException.class)
  public ResponseEntity<?> taskNameLessThanThreeException(TaskNameLessThanThreeException exception) {

    TaskExceptionsAttributes attributes =
         new TaskExceptionsAttributes(
              exception.getMessage(),
              exception.getClass()
                       .getName(),
              NOT_ACCEPTABLE.value(),
              new Date().getTime()
         );
    return new ResponseEntity<>(attributes, NOT_ACCEPTABLE);
  }
}