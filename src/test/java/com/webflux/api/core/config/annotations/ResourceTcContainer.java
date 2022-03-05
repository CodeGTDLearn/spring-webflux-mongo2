package com.webflux.api.core.config.annotations;

import com.webflux.api.core.config.testcontainer.container.TcContainer;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_CLASS;

@Retention(RUNTIME)
@Target(TYPE)
@AutoConfigureWebTestClient(timeout = "3600000")
@SpringBootTest(webEnvironment = RANDOM_PORT)
@DirtiesContext(classMode = BEFORE_CLASS)
@TestPropertySource("classpath:application.yml")
@ActiveProfiles("gr-test-tr")
@TcContainer
public @interface ResourceTcContainer {
}