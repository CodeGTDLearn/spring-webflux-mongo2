package com.webflux.api.core.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Component
@Getter
@Setter
@AllArgsConstructor
public class GlobalExceptionThrower {

  final private GlobalExceptionCustomAttributes messages;

  public <T> Mono<T> throwGlobalException() {
    return Mono.error(
         new ResponseStatusException(NOT_FOUND, messages.getGlobalMessage()));
  }

}