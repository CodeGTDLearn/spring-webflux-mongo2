package com.webflux.api.core.exceptions.global;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

/*
    ╔═══════════════════════════════════════════════════════════╗
    ║              GLOBAL-EXCEPTIONS EXPLANATIONS               ║
    ╠═══════════════════════════════════════════════════════════╣
    ║         There is no Thrower in Global-Exceptions          ║
    ║           Because Global-Exceptions are threw             ║
    ║               for "the system by itself",                 ║
    ║         not programmatically in a specific method         ║
    ║(meaning threw inside a method according the coder defined)║
    ╚═══════════════════════════════════════════════════════════╝
*/
@Component
@Getter
@Setter
@AllArgsConstructor
public class GlobalExceptionAttributes extends DefaultErrorAttributes {

  private GlobalExceptionMessages attributes;

  @Override
  public Map<String, Object> getErrorAttributes(
       ServerRequest request,
       ErrorAttributeOptions options) {

    Map<String, Object> globalAttributes = super.getErrorAttributes(request, options);

    /*╔══════════════════════════════════════════════════════╗
      ║ ADICIONA A GLOBAL-EXCEPTION(ResponseStatusException) ║
      ╠══════════════════════════════════════════════════════╣
      ║ POIS NAO SE TRATA DE NENHUMA DAS 'CUSTOM-EXCEPTIONS' ║
      ╚══════════════════════════════════════════════════════╝*/
    Throwable throwable = getError(request);
    if (throwable instanceof ResponseStatusException) {

      ResponseStatusException error = (ResponseStatusException) throwable;

      /*╔═════════════════════════════════════════════╗
        ║    1) ADDING GLOBAL-EXCEPTION-ATTRIBUTES    ║
        ╠═════════════════════════════════════════════╣
        ║ adiciona/PUT ATTRIBUTES no globalAttributes ║
        ║             DEFAULT ATTRIBUTES              ║
        ╚═════════════════════════════════════════════╝
        ╔═════════════════════════════════════════════╗
        ║  1.1) CLASSIC-ERROR IN 'MESSAGE' ATTRIBUTE  ║
        ╠═════════════════════════════════════════════╣
        ║   DONT USE ":" - "message"("message": "",)  ║
        ╚═════════════════════════════════════════════╝*/
      globalAttributes.put("message", error.getMessage());  // 'message was removed
      globalAttributes.put("reason", error.getReason());

      /*╔══════════════════════════════════════════════╗
        ║ 2) ADDING CUSTOM GLOBAL-EXCEPTION-ATTRIBUTES ║
        ╠══════════════════════════════════════════════╣
        ║      PUT CUSTOM-ATRIBUTES (NOT DEFAULT)      ║
        ║EX: globalAttributes.put("example","example2")║
        ╚══════════════════════════════════════════════╝*/
      globalAttributes.put("Global-Global-Atribute", attributes.getGlobalMessage());
      globalAttributes.put("Global-Dev-Atribute", attributes.getDeveloperMessage());

      /*╔══════════════════════════════════════════════╗
        ║    3) REMOVING GLOBAL-EXCEPTION-ATTRIBUTES   ║
        ╠══════════════════════════════════════════════╣
        ║           REMOVE CUSTOM-ATTRIBUTES           ║
        ║          REMOVING Keys/Fields from           ║
        ║         the Global-Exception-Message         ║
        ║      EX: globalAttributes.remove("path");    ║
        ║   EX: globalAttributes.remove("message");    ║
        ╚══════════════════════════════════════════════╝*/
      // globalAttributes.remove("path");
      globalAttributes.remove("error");
      //globalAttributes.remove("message");
      globalAttributes.remove("timestamp");
      globalAttributes.remove("requestId");
    }

/*
     NAO SENDO UMA GLOBAL-EXCEPTION(ResponseStatusException)
     PORTANTO SENDO, UMA CUSTOM-EXCEPTION
     retorna o valor PADRAO de ATTRIBUTES ou seja,
     o globalAttributes "PURO", sem insercao(.put's do IF acima) de qquer atributo
     personalizado
     OU SEJA, nao se acrescenta os atributos definidos no IF-ACIMA
*/
    return globalAttributes;

  }

}