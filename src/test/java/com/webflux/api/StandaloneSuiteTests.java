package com.webflux.api;

import org.junit.platform.suite.api.*;

@Suite
@SuiteDisplayName("DockerCompose: StandaloneSuite")
@SelectPackages({
     "com.webflux.api.modules.project.core.exceptions",
     "com.webflux.api.core.exceptions",
     "com.webflux.api.modules.project.resource",
     "com.webflux.api.modules.task",
})
@IncludeTags({"standalone"})
@ExcludeTags("no-standalone")
public class StandaloneSuiteTests {
}