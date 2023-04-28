package com.webflux.api.modules.project.core.exceptions;

import com.webflux.api.core.config.YamlProcessor;
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
@ConfigurationProperties(prefix = "modules.exception.project")
@PropertySource(value = "classpath:exception-messages\\project.yml", factory = YamlProcessor.class)
public class ProjectExceptionsMessages {

  private String projectNotFoundMessage;
  private String projectUpdateSimpleFailMessage;
  private String projectUpdateOptFailMessage;
  private String projectNameIsEmptyMessage;
  private String projectNameEmptyBeanValidationMessage;
}