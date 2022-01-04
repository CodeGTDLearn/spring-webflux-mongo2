package com.webflux.mongo2.core.exception.custom.task;

import com.webflux.mongo2.core.exception.custom.project.types.ProjectNameNotFoundException;
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
public class TaskExceptions {

    private TaskExceptionPropertiesValidation taskExceptionPropertiesValidation;


    public <T> Mono<T> projectNameNotFoundException() {
        return Mono.error(new ProjectNameNotFoundException(
             taskExceptionPropertiesValidation.getTaskProjectIdLackMessage()));
    }


}