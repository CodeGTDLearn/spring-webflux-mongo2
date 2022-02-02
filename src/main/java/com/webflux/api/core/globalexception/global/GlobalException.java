package com.webflux.api.core.globalexception.global;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Component("globalException")
@Getter
@Setter
@AllArgsConstructor
public class GlobalException {

  private GlobalExceptionProperties properties;

  public <T> Mono<T> globalErrorException() {

    return Mono.error(
         new ResponseStatusException(NOT_FOUND,
                                     properties.getGlobalMessage()
         )
                     );
  }

}