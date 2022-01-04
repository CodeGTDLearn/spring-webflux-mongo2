package com.webflux.mongo2.core.exception.custom.project;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.validation.constraints.*;

// Properties Importation: https:
// - www.baeldung.com/properties-with-spring
// - https://www.appsdeveloperblog.com/spring-boot-configurationproperties-tutorial/
// getters + setter are necessary, in order to use @ConfigurationProperties
@Component
@Getter
@Setter
@PropertySource(value = "classpath:exceptions-messages.yml", ignoreResourceNotFound = true)
@ConfigurationProperties(prefix = "custom.exception.project")
public class ProjectExceptionPropertiesValidation {

    @Size(min = 10, max = 20)
    private String projectNameSizeIncorretMessage;

    @NotEmpty
    private String projectNameNotFoundMessage;

    @Positive
    private String projectCostNegativeException;
}