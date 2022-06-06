package com.webflux.api.core.config.profiles;

import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/*╔═══════════════════════════════╗
  ║ USING CONTAINERS IN THE TESTS ║
  ╠═══════════════════════════════╩════════════════╗
  ║ CONFLICT: TEST-CONTAINERS X DOCKER-CONTAINERS  ║
  ║           THEY DO NOT WORKS TOGETHER           ║
  ╠════════════════════════════════════════════════╩═════════╗
  ║A) TEST-CONTAINERS:                                       ║
  ║A.1) Define @ActiveProfiles, as testcontainer, in:        ║
  ║ - ProfileGeneral + ProfileTransaction                    ║
  ║A.1) STOP+CLEAN DOCKER-CONTAINERS  (DOCKER-BAT-SCRIPT)    ║
  ║A.2) "UNCOMMENT" TEST-CONTAINERS-ANNOTATIONS IN ALL TESTS ║
  ║ - TEST-CLASSES: @Container                               ║
  ║ - Tc-Compose Annotattion: @Testcontainers                ║
  ║ - TcContainerReplicaset Annotattion: @ExtendWith(...)    ║
  ║A.3) RUN THE TESTS                                        ║
  ║                                                          ║
  ║B) DOCKER-CONTAINERS:                                     ║
  ║B.1) Define @ActiveProfiles, as dockercompose, in:        ║
  ║ - ProfileGeneral + ProfileTransaction                    ║
  ║B.2) "COMMENT" TEST-CONTAINERS-ANNOTATIONS:               ║
  ║ - TEST-CLASSES: @Container                               ║
  ║ - Tc-Compose Annottation: @Testcontainers                ║
  ║ - TcContainerReplicaset Annottation: @ExtendWith(...)    ║
  ║B.3) START DOCKER-CONTAINER (DOCKER-BAT-SCRIPT-PROFILE)   ║
  ║B.4) RUN THE TESTS                                        ║
  ╚══════════════════════════════════════════════════════════╝*/
@Retention(RUNTIME)
@Target(TYPE)
//https://stackoverflow.com/questions/52100384/conditionally-set-activeprofile-before-database-test-in-spring-boot
//@ActiveProfiles("test-development-testcontainer-composemodule-noreplicaset")
@ActiveProfiles("test-development-dockercompose-standalone-noreplicaset")
//@ActiveProfiles("test-development-dockercompose-singlenode-replicaset")
public @interface ProfileGeneral {
}