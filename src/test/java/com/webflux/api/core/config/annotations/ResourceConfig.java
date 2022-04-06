package com.webflux.api.core.config.annotations;

import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;

/*
     ╔═══════════════════════════════╗
     ║ USING CONTAINERS IN THE TESTS ║
     ╠═══════════════════════════════╩════════════════╗
     ║ CONFLICT: TEST-CONTAINERS X DOCKER-CONTAINERS  ║
     ║           THEY DO NOT WORKS TOGETHER           ║
     ╠════════════════════════════════════════════════╩═════════╗
     ║A) TEST-CONTAINERS:                                       ║
     ║   A.1) STOP+CLEAN DOCKER-CONTAINERS  (DOCKER-BAT-SCRIPT) ║
     ║   A.2) "UNCOMMENT" THE TEST-CONTAINERS-ANNOTATIONS       ║
     ║   A.3) RUN THE TESTS                                     ║
     ║   A.4) COMMENT THE APPLICATION-TC-TRANSACTIONS.YML       ║
     ║                                                          ║
     ║B) DOCKER-CONTAINERS:                                     ║
     ║   B.1) SET PROFILE-ACTIVE IN SRC/MAIN/APPLICATION.YML    ║
     ║   B.2) "COMMENT" THE TEST-CONTAINER-ANNOTATION(TcCompose)║
     ║   B.3) START DOCKER-CONTAINER (DOCKER-BAT-SCRIPT-PROFILE)║
     ║   B.4) RUN THE TESTS                                     ║
     ║   B.5) UNCOMMENT THE APPLICATION-TC-CONTAINER.YML        ║
     ╚══════════════════════════════════════════════════════════╝
*/
@Retention(RUNTIME)
@Target(TYPE)
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = RANDOM_PORT)
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
@TestPropertySource("classpath:application.yml")
public @interface ResourceConfig {
}