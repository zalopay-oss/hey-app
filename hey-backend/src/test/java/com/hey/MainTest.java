package com.hey;

import com.hey.server.ApiServerTestSuite;
import com.hey.server.WsServerTestSuite;
import com.hey.repository.DataRepositoryTestSuite;
import com.hey.service.ApiServiceTestSuite;
import com.hey.service.BaseServiceTestSuite;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({
        DataRepositoryTestSuite.class,
        BaseServiceTestSuite.class,
        ApiServiceTestSuite.class,
        ApiServerTestSuite.class,
        WsServerTestSuite.class
})
public class MainTest {

    @BeforeClass
    public static void setupTest() {
    }
}
