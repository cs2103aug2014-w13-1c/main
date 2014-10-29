package tests.integration;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

/**
 * Created by jolly on 29/10/14.
 */
public class IntegrationTestRunner {
    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(IntegrationTestSuite.class);
        for (Failure failure : result.getFailures()) {
            System.out.println(failure.getClass());
            System.out.println(failure.toString());
        }
        System.out.println(result.wasSuccessful());
    }
}
