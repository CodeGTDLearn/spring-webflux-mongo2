package com.webflux.api.modules.project.core.exceptions;

import com.webflux.api.modules.project.core.exceptions.types.ProjectCostNegativeException;
import com.webflux.api.modules.project.core.exceptions.types.ProjectNameEmptyException;
import com.webflux.api.modules.project.core.exceptions.types.ProjectNameSizeIncorretException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

// getters + setter are necessary, in order to use @ConfigurationProperties
@Component
@Getter
@Setter
@AllArgsConstructor
public class ProjectExceptionsThrower {

  private ProjectExceptionsCustomAttributes messages;

  public <T> Mono<T> throwProjectCostNegativeException() {

    return Mono.error(new ProjectCostNegativeException(
         messages.getProjectCostNegativeMessage()));
  }

  public <T> Mono<T> throwProjectNameEmptyException() {
    return Mono.error(new ProjectNameEmptyException(
         messages.getProjectNameEmptyMessage()));}

  public <T> Mono<T> throwProjectNameSizeIncorretException() {

    return Mono.error(new ProjectNameSizeIncorretException(
         messages.getProjectNameSizeIncorretMessage()));
  }

  public <T> Mono<T> throwProjectNotFoundException() {
    return Mono.error(new ProjectNameEmptyException(
         messages.getProjectNotFoundMessage()));
  }




}