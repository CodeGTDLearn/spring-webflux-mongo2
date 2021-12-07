package com.webflux.mongo2.exception;

import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class ErrorAttributes extends DefaultErrorAttributes {

  //  @Override
  public Map<String, Object> getErrorAttributes(ServerRequest request,boolean includeStackTrace) {
    return assembleError(request);
  }


  private Map<String, Object> assembleError(ServerRequest request) {
    Map<String, Object> errorAttributes = new LinkedHashMap<>();
    Throwable error = getError(request);
    if (error instanceof OptimisticLockingFailureException) {
      errorAttributes.put("errorCode",400);
      errorAttributes.put("errorMessage","Version mismatch");
    } else {
      errorAttributes.put("errorCode",HttpStatus.INTERNAL_SERVER_ERROR);
      errorAttributes.put("errorMessage","INTERNAL SERVER ERROR");
    }
    return errorAttributes;
  }
}