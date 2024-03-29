package com.webflux.api.modules.task.core.exceptions;

import com.webflux.api.modules.task.core.exceptions.types.TaskNameIsEmptyException;
import com.webflux.api.modules.task.core.exceptions.types.TaskNameLessThanThreeException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

// getters + setter are necessary, in order to use @ConfigurationProperties
@Component("taskExceptionsThrower")
@Getter
@Setter
@AllArgsConstructor
public class TaskExceptionsThrower {

  @Autowired
  private TaskExceptionsMessages taskExceptionsMessages;

  public <T> Mono<T> throwTaskNameIsEmptyException() {

    return Mono.error(new TaskNameIsEmptyException(
         taskExceptionsMessages.getTaskNameIsEmptyMessage()));
  }

  public <T> Mono<T> throwTaskNameLessThanThreeException() {

    return Mono.error(new TaskNameLessThanThreeException(
         taskExceptionsMessages.getTaskNameLessThanThreeMessage()));

  }

}