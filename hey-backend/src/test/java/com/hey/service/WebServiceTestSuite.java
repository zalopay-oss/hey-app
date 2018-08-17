package com.hey.service;

import com.hey.BaseVerticleTestSuite;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(VertxUnitRunner.class)
public class WebServiceTestSuite extends BaseVerticleTestSuite {


    @BeforeClass
    public static void startTestCases() {
        System.out.println("Running " + WebServiceTestSuite.class.getName());
    }

    @Test
    public void test1(TestContext context) {
        context.assertTrue(true);
    }

    @Test
    public void test2(TestContext context) {
        context.assertTrue(true);
    }
}
