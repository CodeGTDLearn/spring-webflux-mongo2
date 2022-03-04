package com.webflux.api;


import com.webflux.api.core.exception.GlobalExceptionTest;
import com.webflux.api.modules.project.core.exceptions.ResourceCrudExcTest;
import com.webflux.api.modules.project.core.exceptions.ResourceTransactionExcTest;
import com.webflux.api.modules.project.resource.*;
import com.webflux.api.modules.task.ResourceTaskTest;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
@SelectClasses({
     ResourceTemplTest.class,
     ResourceColectionsTest.class,
     ResourceCrudTest.class,
     GlobalExceptionTest.class,
     ResourceAggregTest.class,
     ResourceCrudExcTest.class,
     ResourceProjectionTest.class,
     ResourceLookupProjectionTest.class,
     ResourceRepoTest.class,
     ResourceChildArrayTest.class,
     ResourceTaskTest.class,
     ResourceTransactionTest.class,
     ResourceTransactionExcTest.class,
})
public class ApiDriverSuiteTests {

//  @BeforeAll
//  public static void setUp() {
//
//    System.out.println("----- SUITE-SUPER SETUP -----");
//  }
//
//  @AfterAll
//  public static void tearDown() {
//
//    System.out.println("----- SUITE-SUPER TEARDOWN -----");
//  }
}