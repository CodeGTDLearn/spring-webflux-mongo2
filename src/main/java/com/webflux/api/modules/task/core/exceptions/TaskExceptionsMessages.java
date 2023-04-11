package com.webflux.api.modules.task.core.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/*╔══════════════════════════════════════════════════════════════════════════════════════╗
  ║                     PropertySource + YAML ConfigurationProperties                    ║
  ╠══════════════════════════════════════════════════════════════════════════════════════╣
  ║ https://www.appsdeveloperblog.com/spring-boot-configurationproperties-tutorial/      ║
  ║ PropertySource|PropertyFile www.baeldung.com/configuration-properties-in-spring-boot ║
  ║ PropertySource|YAML: www.baeldung.com/spring-yaml-propertysource                     ║
  ║ Setter/Getter are CRUCIAL for PropertySource + ConfigurationProperties works properly║
  ╚══════════════════════════════════════════════════════════════════════════════════════╝*/
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "modules.exception.task")
@PropertySource(value = "classpath:exception-messages\\task.yml", ignoreResourceNotFound = true)
public class TaskExceptionsMessages {

  private String taskNameIsEmptyMessage;
  private String taskNameLessThanThreeMessage;
}