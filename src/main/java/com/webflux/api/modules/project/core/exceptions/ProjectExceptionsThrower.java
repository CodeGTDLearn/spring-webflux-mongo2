package com.webflux.api.modules.project.core.exceptions;

import com.webflux.api.modules.project.core.exceptions.types.ProjectNotFoundException;
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
}