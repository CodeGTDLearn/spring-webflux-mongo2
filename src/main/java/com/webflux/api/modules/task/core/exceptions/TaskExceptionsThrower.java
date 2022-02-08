package com.webflux.api.modules.task.core.exceptions;

import com.webflux.api.modules.project.core.exceptions.types.ProjectNameEmptyException;
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
public class TaskExceptionsThrower {

    private TaskExceptionsCustomAttributes messages;

    public <T> Mono<T> projectNameNotFoundException() {
        return Mono.error(new ProjectNameEmptyException(
             messages.getTaskProjectIdLackMessage()));
    }


}