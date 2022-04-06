package com.webflux.api.core.config.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;

@Import({
     DbUtilsConfig.class,
     TransactionManagerConfig.class
})
@TestConfiguration(value = "testCoreConfig")
public class ReplicasetConfig {

}