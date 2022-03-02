package com.webflux.api.core.config.annotations;

import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target(TYPE)
@ResourceConfig
@StartupConfig
@ActiveProfiles("gr-test-tr")
public @interface MergedResource {
}