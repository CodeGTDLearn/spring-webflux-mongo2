package com.webflux.api.core.config.suites;

import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SuiteDisplayName("DockerCompose: ReplicasetSuite")
@SelectPackages({
     "com.webflux.api.core.exceptions.global",
     "com.webflux.api.modules.project.core.exceptions",
     "com.webflux.api.modules.project.resource",
     "com.webflux.api.modules.task",
})
@IncludeTags({"replicaset"})
public class Replicaset {
}