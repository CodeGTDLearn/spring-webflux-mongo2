package com.webflux.api.core.exceptions.beanValidation;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

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

  ╔══════════════════════════════════════════════════════════════════════════════════════╗
  ║                     PropertySource + YAML ConfigurationProperties                    ║
  ╠══════════════════════════════════════════════════════════════════════════════════════╣
  ║ https://www.appsdeveloperblog.com/spring-boot-configurationproperties-tutorial/      ║
  ║ PropertySource|PropertyFile www.baeldung.com/configuration-properties-in-spring-boot ║
  ║ PropertySource|YAML: www.baeldung.com/spring-yaml-propertysource                     ║
  ║ Setter/Getter are CRUCIAL for PropertySource + ConfigurationProperties works properly║
  ╚══════════════════════════════════════════════════════════════════════════════════════╝*/
@Configuration
public class BeanValidationExceptionMessages {

  // https://www.javadevjournal.com/spring-boot/spring-custom-validation-message-source/

  private final String messagesSourcePath = "classpath:exception-messages\\beanValidation.yml";

  @Bean
  public MessageSource messageSource() {
    var source = new ReloadableResourceBundleMessageSource();
    source.setBasename(messagesSourcePath);
    source.setDefaultEncoding("UTF-8");
    return source;
  }

  @Bean
  public LocalValidatorFactoryBean validator(MessageSource messageSource) {

    var bean = new LocalValidatorFactoryBean();
    bean.setValidationMessageSource(messageSource);
    return bean;
  }

}