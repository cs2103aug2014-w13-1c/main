package tests.integration;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created by jolly on 29/10/14.
 */

@RunWith(Suite.class)

@Suite.SuiteClasses({
        tests.integration.IntegrationTests.class,
        tests.integration.IntegrationTests2.class
})

public class IntegrationTestSuite {
}
