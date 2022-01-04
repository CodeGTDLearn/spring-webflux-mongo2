package com.webflux.mongo2.core.exception.custom.task;

import com.webflux.mongo2.core.exception.custom.CustomExceptionAttributes;
import com.webflux.mongo2.core.exception.custom.task.types.TaskProjectIdLackException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;

@ControllerAdvice(annotations = {RestController.class})
@AllArgsConstructor
public class TaskExceptionHandler {

  @ExceptionHandler(TaskProjectIdLackException.class)
  public ResponseEntity<?> TaskExceptionWithCustomAttributes(TaskProjectIdLackException exception) {

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