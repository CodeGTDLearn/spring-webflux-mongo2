package com.webflux.api.core.exception.modules.project;

import com.webflux.api.core.exception.modules.project.types.ProjectCostNegativeException;
import com.webflux.api.core.exception.modules.project.types.ProjectNameNotFoundException;
import com.webflux.api.core.exception.modules.project.types.ProjectNameSizeIncorretException;
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
public class ProjectExceptions {

  private ProjectExceptionValidator projectExceptionValidator;


  public <T> Mono<T> projectNameEmptyMessage() {

    return Mono.error(new ProjectNameNotFoundException(
         projectExceptionValidator.getProjectNameEmptyMessage()));
  }

  public <T> Mono<T> projectNotFoundException() {

    return Mono.error(new ProjectNameNotFoundException(
         projectExceptionValidator.getProjectNotFoundException()));
  }

  public <T> Mono<T> projectCostNegativeException() {

    return Mono.error(new ProjectCostNegativeException(
         projectExceptionValidator.getProjectCostNegativeException()));
  }

  public <T> Mono<T> projectNameSizeIncorretMessage() {

    return Mono.error(new ProjectNameSizeIncorretException(
         projectExceptionValidator.getProjectNameSizeIncorretMessage()));
  }


}