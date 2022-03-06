package com.webflux.api.core.config.testcontainer.compose;

import org.testcontainers.junit.jupiter.Testcontainers;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Testcontainers
@Retention(RUNTIME)
@Target(TYPE)
public @interface TcCompose {
}