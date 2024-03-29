package com.webflux.api.core.config.testcontainer.container;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target({TYPE})
@ExtendWith(TcContainerConfig.class)
@ActiveProfiles({"testcontainer"})
public @interface TcContainerReplicaset {
}