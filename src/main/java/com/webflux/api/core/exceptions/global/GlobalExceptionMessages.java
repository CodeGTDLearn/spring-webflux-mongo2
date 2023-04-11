package com.webflux.api.core.exceptions.global;

import com.webflux.api.core.config.YamlProcessor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

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
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "global.exception")
@PropertySource(value = "classpath:exception-messages\\global.yml", factory =
     YamlProcessor.class)
public class GlobalExceptionMessages {

  private String developerMessage;
  private String globalMessage;

}