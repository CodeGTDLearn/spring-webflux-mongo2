package com.webflux.api.modules.task.core.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.validation.constraints.NotEmpty;

// ========================== PropertySource + ConfigurationProperties =============================
// - https://www.appsdeveloperblog.com/spring-boot-configurationproperties-tutorial/
// Check - PropertySource: https://www.baeldung.com/configuration-properties-in-spring-boot
// Setter/Getter are CRUCIAL for PropertySource + ConfigurationProperties works properly
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "modules.exception.task")
@PropertySource(value = "classpath:exceptions-messages.yml", ignoreResourceNotFound = true)
public class TaskExceptionsCustomAttributes {

  // THE BEAN-VALIDATION IS VALIDATING THE MESSAGE-CONTENT
  // THAT COMES FROM THE EXCEPTIONS-MANAGEMENT.PROPERTIES FILE
  // THOSE VALIDATIONS NOT HAVE RELATION WITH THE EXCEPTIONS
  @NotEmpty
  private String taskNameIsEmptyMessage;
  private String taskNameLessThanThreeMessage;
}