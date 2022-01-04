package com.webflux.mongo2.core.exception.custom.project;

import com.webflux.mongo2.core.exception.custom.project.types.ProjectCostNegativeException;
import com.webflux.mongo2.core.exception.custom.project.types.ProjectNameNotFoundException;
import com.webflux.mongo2.core.exception.custom.project.types.ProjectNameSizeIncorretException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

// getters + setter are necessary, in order to use @ConfigurationProperties
@Component("customExceptions")
@Getter
@Setter
@AllArgsConstructor
public class ProjectExceptions {

    private ProjectExceptionPropertiesValidation projectExceptionPropertiesValidation;


    public <T> Mono<T> projectNameNotFoundException() {
        return Mono.error(new ProjectNameNotFoundException(
             projectExceptionPropertiesValidation.getProjectNameNotFoundMessage()));
    }

    public <T> Mono<T> projectCostNegativeException() {
        return Mono.error(new ProjectCostNegativeException(
             projectExceptionPropertiesValidation.getProjectCostNegativeException()));
    }

    public <T> Mono<T> projectNameSizeIncorretMessage() {
        return Mono.error(new ProjectNameSizeIncorretException(
             projectExceptionPropertiesValidation.getProjectNameSizeIncorretMessage()));
    }


}