package com.webflux.api.core.config.testconfigs;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;

@Import({
     TestDbUtilsConfig.class,
     TransactionManagerTestConfig.class
})
@TestConfiguration(value = "testCoreConfig")
public class TestCoreConfig {

}