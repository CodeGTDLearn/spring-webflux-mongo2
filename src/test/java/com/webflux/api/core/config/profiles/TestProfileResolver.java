package com.webflux.api.core.config.profiles;

import org.springframework.test.context.ActiveProfilesResolver;

import javax.validation.constraints.NotNull;

public class TestProfileResolver implements ActiveProfilesResolver {
    @NotNull
    @Override
    public String[] resolve(@NotNull Class<?> testClass) {
        return new String[]{System.getProperty("testEnvt", "dev")};
    }
}