package com.webflux.api.modules.task.core.exceptions;

import com.webflux.api.modules.project.core.exceptions.types.ProjectNameNotFoundException;
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

    private TaskExceptionValidator taskExceptionValidator;


    public <T> Mono<T> projectNameNotFoundException() {
        return Mono.error(new ProjectNameNotFoundException(
             taskExceptionValidator.getTaskProjectIdLackMessage()));
    }


}