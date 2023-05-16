package com.webflux.api.core.exceptions.beanValidation;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
public class BeanValidationExceptionMessages {

  private final String messagesPath = "classpath:/exception-messages/beanValidation";

  @Bean
  public MessageSource messages() {
    var messageSource = new ReloadableResourceBundleMessageSource();
    messageSource.setBasenames(messagesPath);
    messageSource.setDefaultEncoding("UTF-8");
    return messageSource;
  }


  @Bean
  public LocalValidatorFactoryBean validator(MessageSource messages) {
    var localValidatorFactoryBean = new LocalValidatorFactoryBean();
    localValidatorFactoryBean.setValidationMessageSource(messages);
    return localValidatorFactoryBean;
  }
}