package config.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/*
     ╔═══════════════════════════════╗
     ║ USING CONTAINERS IN THE TESTS ║
     ╠═══════════════════════════════╩════════════════╗
     ║ CONFLICT: TEST-CONTAINERS X DOCKER-CONTAINERS  ║
     ║           THEY DO NOT WORKS TOGETHER           ║
     ╠════════════════════════════════════════════════╩════════════════╗
     ║A) TEST-CONTAINERS:                                              ║
     ║   A.1) STOP+CLEAN DOCKER-CONTAINERS  (DOCKER-BAT-SCRIPT)        ║
     ║   A.2) "UNCOMMENT" THE TEST-CONTAINERS-ANNOTATION BELOW         ║
     ║   A.3) RUN THE TESTS                                            ║
     ║                                                                 ║
     ║B) DOCKER-CONTAINERS:                                            ║
     ║   B.1) SET PROFILE-ACTIVE IN SRC/MAIN/RESOURCES/APPLICATION.YML ║
     ║   B.2) "COMMENT" THE TEST-CONTAINERS-ANNOTATION BELOW           ║
     ║   B.3) START DOCKER-CONTAINER (DOCKER-BAT-SCRIPT-PROFILE)       ║
     ║   B.4) RUN THE TESTS                                            ║
     ╚═════════════════════════════════════════════════════════════════╝
*/

//@Testcontainers
@Retention(RUNTIME)
@Target(TYPE)
public @interface TestcontainersConfig {
}