package com.webflux.api.modules.task.core.exceptions;

import com.webflux.api.modules.task.core.exceptions.types.TaskProjectIdLackException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;

@ControllerAdvice(annotations = {RestController.class})
@AllArgsConstructor
public class TaskExceptionsHandler {

  @ExceptionHandler(TaskProjectIdLackException.class)
  public ResponseEntity<?> exceptionAttributes(TaskProjectIdLackException exception) {

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