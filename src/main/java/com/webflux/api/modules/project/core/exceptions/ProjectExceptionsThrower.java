package com.webflux.api.modules.project.core.exceptions;

import com.webflux.api.modules.project.core.exceptions.types.ProjectNameIsEmptyException;
import com.webflux.api.modules.project.core.exceptions.types.ProjectNotFoundException;
import com.webflux.api.modules.project.core.exceptions.types.UpdateOptmisticVersionException;
import com.webflux.api.modules.project.core.exceptions.types.UpdateSimpleException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

// getters + setter are necessary, in order to use @ConfigurationProperties
@Component("projectExceptionsThrower")
@Getter
@Setter
@AllArgsConstructor
public class ProjectExceptionsThrower {

  private ProjectExceptionsCustomAttributes projectExceptionsCustomAttributes;

  public <T> Mono<T> throwProjectNotFoundException() {
    return Mono.error(new ProjectNotFoundException(
         projectExceptionsCustomAttributes.getProjectNotFoundMessage()));
  }

  public <T> Mono<T> throwUpdateOptmVersionException() {
    return Mono.error(new UpdateOptmisticVersionException(
         projectExceptionsCustomAttributes.getProjectUpdateOptFailMessage()));
  }

  public <T> Mono<T> throwUpdateSimpleException() {
    return Mono.error(new UpdateSimpleException(
         projectExceptionsCustomAttributes.getProjectUpdateSimpleFailMessage()));
  }

  public <T> Mono<T> throwProjectNameIsEmptyException() {
    return Mono.error(new ProjectNameIsEmptyException(
         projectExceptionsCustomAttributes.getProjectNameIsEmptyMessage()));
  }
}