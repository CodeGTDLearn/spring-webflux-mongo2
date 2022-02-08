package com.webflux.api.core.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

// ========================== PropertySource + ConfigurationProperties =============================
// - https://www.appsdeveloperblog.com/spring-boot-configurationproperties-tutorial/
// Check - PropertySource: https://www.baeldung.com/configuration-properties-in-spring-boot
// Setter/Getter are CRUCIAL for PropertySource + ConfigurationProperties works properly
@Setter
@Getter
@PropertySource(value = "classpath:exceptions-custom-attributes.yml", ignoreResourceNotFound = true)
@ConfigurationProperties(prefix = "global.exception")
@Component
public class GlobalExceptionCustomAttributes {

    // THE BEAN-VALIDATION IS VALIDATING THE MESSAGE-CONTENT
    // THAT COMES FROM THE EXCEPTIONS-MANAGEMENT.PROPERTIES FILE
    // THOSE VALIDATIONS NOT HAVE RELATION WITH THE EXCEPTIONS
    private String developerAttributeMessage;
    private String developerMessage;
    private String globalAttributeMessage;
    private String globalMessage;

}