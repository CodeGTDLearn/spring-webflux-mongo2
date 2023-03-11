package com.webflux.api.modules.project.core.exceptions;

import com.webflux.api.modules.project.core.exceptions.types.ProjectNotFoundException;
import com.webflux.api.modules.project.core.exceptions.types.UpdateOptmisticVersionException;
import com.webflux.api.modules.project.core.exceptions.types.UpdateSimpleException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

// ==> EXCEPTIONS IN CONTROLLER:
// *** REASON: IN WEBFLUX, EXCEPTIONS MUST BE IN CONTROLLER - WHY?
//     - "Como stream pode ser manipulado por diferentes grupos de thread,
//     - caso um erro aconteça em uma thread que não é a que operou a controller,
//     - o ControllerAdvice não vai ser notificado "
//     - https://medium.com/nstech/programa%C3%A7%C3%A3o-reativa-com-spring-boot-webflux-e-mongodb-chega-de-sofrer-f92fb64517c3
// getters + setter are necessary, in order to use @ConfigurationProperties
@Component("projectExceptionsThrower")
@Getter
@Setter
@RequiredArgsConstructor
public class ProjectExceptionsThrower {

  @Autowired
  private ProjectExceptionsMessages projectExceptionsMessages;

  public <T> Mono<T> throwProjectNotFoundException() {

    return Mono.error(new ProjectNotFoundException(
         projectExceptionsMessages.getProjectNotFoundMessage()));
  }

  public <T> Mono<T> throwUpdateOptmVersionException() {

    return Mono.error(new UpdateOptmisticVersionException(
         projectExceptionsMessages.getProjectUpdateOptFailMessage()));
  }

  public <T> Mono<T> throwUpdateSimpleException() {

    return Mono.error(new UpdateSimpleException(
         projectExceptionsMessages.getProjectUpdateSimpleFailMessage()));
  }
}