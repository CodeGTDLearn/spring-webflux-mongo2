package config.annotations;

import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@Retention(RUNTIME)
@Target(TYPE)
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@TestPropertySource("classpath:application-test.properties")
@ActiveProfiles("test")
public @interface GlobalConfig {
}

//se deletar os app.properties do src/test/resources suge o errorCaused by: java.lang.IllegalArgumentException: Could not resolve placeholder 'MONGO_REPLICASET_NAME' in value "${MONGO_REPLICASET_NAME}"
//
// se mantem as indicacoes do profile aqui(TestPropertySource+ActiveProfiles), da o seguinte erroCaused by: org.springframework.boot.context.config.InvalidConfigDataPropertyException: Property 'spring.profiles.include' imported from location 'class path resource [application-test.properties]' is invalid in a profile specific resource [origin: class path resource [application-test.properties] - 1:25]