package tests.integration;
//@author A0111764L

import app.Main;
import app.model.FileStorage;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.Assertion;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Tests for a successful application launch and file storage IO.
 * 
 * Integration test zero. 
 */
public class IntegrationTest0 {
    @Rule
    public final ExpectedSystemExit exit = ExpectedSystemExit.none();
    
    @Test
    public void testMain() {
       FileStorage testStorage = new FileStorage();
       try {
           testStorage.loadSettings();
       } catch (Exception e) {
           fail();
       }

       String exitCommand = "exit";
       String[] testCommands = {
               exitCommand};

       exit.expectSystemExit();
       exit.checkAssertionAfterwards(() -> assertTrue(true));

       // Carry out commands
       try {
           Main.main(testCommands);
       } catch (Exception e) {
           // Do nothing, this is already caught by the ExpectedSystemExit.
       }
    }
}