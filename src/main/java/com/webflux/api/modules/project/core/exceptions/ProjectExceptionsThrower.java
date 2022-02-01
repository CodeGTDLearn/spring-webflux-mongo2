package com.webflux.api.modules.project.core.exceptions;

import com.webflux.api.modules.project.core.exceptions.types.ProjectCostNegativeException;
import com.webflux.api.modules.project.core.exceptions.types.ProjectNameNotFoundException;
import com.webflux.api.modules.project.core.exceptions.types.ProjectNameSizeIncorretException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

// getters + setter are necessary, in order to use @ConfigurationProperties
@Component("projectExceptions")
@Getter
@Setter
@AllArgsConstructor
public class ProjectExceptionsThrower {

  private ProjectExceptionMessages projectExceptionMessages;


  public <T> Mono<T> projectNameEmptyMessage() {

    return Mono.error(new ProjectNameNotFoundException(
         projectExceptionMessages.getProjectNameEmptyMessage()));
  }

  public <T> Mono<T> projectNotFoundException() {

    return Mono.error(new ProjectNameNotFoundException(
         projectExceptionMessages.getProjectNotFoundException()));
  }

  public <T> Mono<T> projectCostNegativeException() {

    return Mono.error(new ProjectCostNegativeException(
         projectExceptionMessages.getProjectCostNegativeException()));
  }

  public <T> Mono<T> projectNameSizeIncorretMessage() {

    return Mono.error(new ProjectNameSizeIncorretException(
         projectExceptionMessages.getProjectNameSizeIncorretMessage()));
  }


}