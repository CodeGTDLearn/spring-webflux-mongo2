package com.webflux.api.core.config.suites;


import org.junit.platform.suite.api.*;

@Suite
@SuiteDisplayName("Tc-Compose(No Rs) + Testcontainer(Rs) Suite")
@SelectPackages({
     "com.webflux.api.modules.project.core.exceptions",
     "com.webflux.api.core.exceptions",
     "com.webflux.api.modules.project.resource",
     "com.webflux.api.modules.task",
})
@IncludeTags({"testcontainer"})
public class Testcontainer {
}