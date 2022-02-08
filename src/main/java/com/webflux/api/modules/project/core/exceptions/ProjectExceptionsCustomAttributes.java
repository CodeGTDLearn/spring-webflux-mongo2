package com.webflux.api.modules.project.core.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.validation.constraints.*;


// ========================== PropertySource + ConfigurationProperties =============================
// - https://www.appsdeveloperblog.com/spring-boot-configurationproperties-tutorial/
// Check - PropertySource: https://www.baeldung.com/configuration-properties-in-spring-boot
// Setter/Getter are CRUCIAL for PropertySource + ConfigurationProperties works properly
@Getter
@Setter
@PropertySource(value = "classpath:exceptions-custom-attributes.yml", ignoreResourceNotFound = true)
@ConfigurationProperties(prefix = "modules.exception.project")
@Component
public class ProjectExceptionsCustomAttributes {

    // THE BEAN-VALIDATION IS VALIDATING THE MESSAGE-CONTENT
    // THAT COMES FROM THE EXCEPTIONS-MANAGEMENT.PROPERTIES FILE
    // THOSE VALIDATIONS NOT HAVE RELATION WITH THE EXCEPTIONS
    @Positive
    private String projectCostNegativeMessage;

    @NotEmpty
    private String projectNameEmptyMessage;

    @Size(min = 10, max = 20)
    private String projectNameSizeIncorretMessage;

    private String projectNotFoundMessage;
}