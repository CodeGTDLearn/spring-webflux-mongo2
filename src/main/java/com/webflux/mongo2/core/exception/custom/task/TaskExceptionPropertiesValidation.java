package com.webflux.mongo2.core.exception.custom.task;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotEmpty;

// Properties Importation: https:
// - www.baeldung.com/properties-with-spring
// - https://www.appsdeveloperblog.com/spring-boot-configurationproperties-tutorial/
// getters + setter are necessary, in order to use @ConfigurationProperties
@Component
@Getter
@Setter
@PropertySource(value = "classpath:exceptions-messages.yml", ignoreResourceNotFound = true)
@ConfigurationProperties(prefix = "custom.exception.task")
public class TaskExceptionPropertiesValidation {

    @NotEmpty
    private String taskProjectIdLackMessage;

}