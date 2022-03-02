package com.webflux.api.core.config.annotations;

import com.webflux.api.core.config.testcontainer.compose.TcCompose;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target(TYPE)
@TcCompose
@MongoConfig
@StartupConfig
public @interface MergedRepo {
}