package com.webflux.api.core.exceptions.beanValidation;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.util.Date;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

/*
 ==> EXCEPTIONS IN CONTROLLER:
 *** REASON: IN WEBFLUX, EXCEPTIONS MUST BE IN CONTROLLER - WHY?
     - "Como stream pode ser manipulado por diferentes grupos de thread,
     - caso um erro aconteça em uma thread que não é a que operou a controller,
     - o ControllerAdvice não vai ser notificado "
     - https://medium.com/nstech/programa%C3%A7%C3%A3o-reativa-com-spring-boot-webflux-e-mongodb
     -chega-de-sofrer-f92fb64517c3
*/
@ControllerAdvice(annotations = {RestController.class})
@AllArgsConstructor
@Slf4j
public class BeanValidationExceptionHandler {

  @ExceptionHandler(WebExchangeBindException.class)
  public ResponseEntity<?> beanValidationExceptions(WebExchangeBindException exception) {

    log.error("Exception Caught in handleRequestBodyError : {}", exception.getMessage());

    var beanValidationErrors =
         exception
              .getBindingResult()
              .getAllErrors()
              .stream()
              .map(DefaultMessageSourceResolvable::getDefaultMessage)
              .sorted()
              .collect(Collectors.joining(", "));

    log.error("BeanValidation Errors: {}", beanValidationErrors);

    BeanValidationExceptionAttributes exceptionAttributes =
         new BeanValidationExceptionAttributes(
              beanValidationErrors,
              "Bean Validations",
              BAD_REQUEST.value(),
              new Date().getTime()
         );
    return new ResponseEntity<>(exceptionAttributes, BAD_REQUEST);
  }

}