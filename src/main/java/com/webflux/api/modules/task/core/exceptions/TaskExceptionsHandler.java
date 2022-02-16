package com.webflux.api.modules.task.core.exceptions;

import com.webflux.api.modules.task.core.exceptions.types.TaskNameIsEmptyException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice(annotations = {RestController.class})
@AllArgsConstructor
public class TaskExceptionsHandler {

  @ExceptionHandler(TaskNameIsEmptyException.class)
  public ResponseEntity<?> TaskNotFoundException(TaskNameIsEmptyException exception) {

    TaskExceptionsAttributes attributes =
         new TaskExceptionsAttributes(
              exception.getMessage(),
              exception.getClass()
                       .getName(),
              NOT_FOUND.value(),
              new Date().getTime()
         );
    return new ResponseEntity<>(attributes, NOT_FOUND);
  }
}