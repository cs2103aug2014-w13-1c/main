package tests.integration;
//@author A0111764L

import app.Main;
import app.model.FileStorage;
import app.model.TodoItem;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.Assertion;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Integration test 0. Tests for a successful application launch and file storage IO.
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

       // Commands to be carried out
       String exitCommand = "exit";
       String[] testCommands = {
               exitCommand};

       exit.expectSystemExit();
       exit.checkAssertionAfterwards(new Assertion() {
           @Override
           public void checkAssertion() throws Exception {

               // If the application failed to launch, this point will not be reached.
               assertTrue(true);
           }
       });

       // Carry out commands
       try {
           Main.main(testCommands);
       } catch (Exception e) {
           // Do nothing, this is already caught by the ExpectedSystemExit.
       }
    }
}