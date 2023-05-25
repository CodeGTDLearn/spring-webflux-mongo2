package com.webflux.api.core.exceptions.beanValidation;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BeanValidationExceptionAttributes {
  private String detail;
  private String title;
  private int status;
  private long timeStamp;
}