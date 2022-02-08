package com.webflux.api.core.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@Component
@Getter
@Setter
@AllArgsConstructor
public class GlobalExceptionAttributes extends DefaultErrorAttributes {

  private GlobalExceptionCustomAttributes attributes;

  @Override
  public Map<String, Object> getErrorAttributes(ServerRequest request,
                                                ErrorAttributeOptions options) {

    Map<String, Object> globalAttributes = super.getErrorAttributes(request, options);

    // ADICIONA A GLOBAL-EXCEPTION(ResponseStatusException)
    // POIS NAO SE TRATA DE NENHUMA DAS 'CUSTOM-EXCEPTIONS'
    Throwable throwable = getError(request);
    if (throwable instanceof ResponseStatusException) {

      ResponseStatusException error = (ResponseStatusException) throwable;

      // SENDO UMA GLOBAL-EXCEPTION(ResponseStatusException)
      // adiciona ATTRIBUTES no globalAttributes
      /* A) DEFAULT-EXCEPTION-ATTRIBUTES:
      {
           "timestamp": "2022-02-08T22:02:08.410+00:00",
           "path": "/project/save",
           "status": 500,
           "error": "Internal Server Error",
           "message": "",
           "requestId": "317e3568"
      }
      */
      // B) Fix the Default-Parameter "message"("message": "",) which, initially, is Empty
      globalAttributes.put("message", error.getMessage());
      // C) Add Custom-Parameters in the Default-Parameters
      globalAttributes.put(attributes.getGlobalAttributeMessage(), "error.getMessage()");
      globalAttributes.put(attributes.getDeveloperAttributeMessage(),
                           attributes.getDeveloperMessage()
                             );
    }

    // NAO SENDO UMA GLOBAL-EXCEPTION(ResponseStatusException)
    // PORTANTO SENDO, UMA CUSTOM-EXCEPTION
    // retorna o valor PADRAO de ATTRIBUTES ou seja,
    // o globalAttributes "PURO"
    // OU SEJA, nao se acrescenta os atributos definidos no IF-ACIMA
    return globalAttributes;
  }

}