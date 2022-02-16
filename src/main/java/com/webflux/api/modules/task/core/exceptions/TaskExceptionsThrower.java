package com.webflux.api.modules.task.core.exceptions;

import com.webflux.api.modules.task.core.exceptions.types.TaskNameIsEmptyException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

// getters + setter are necessary, in order to use @ConfigurationProperties
@Component("taskExceptionsThrower")
@Getter
@Setter
@AllArgsConstructor
public class TaskExceptionsThrower {

    private TaskExceptionsCustomAttributes taskExceptionsCustomAttributes;

    public <T> Mono<T> throwTaskNameIsEmptyException() {
        return Mono.error(new TaskNameIsEmptyException(
             taskExceptionsCustomAttributes.getTaskNameIsEmptyMessage()));
    }
}