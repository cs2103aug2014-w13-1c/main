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

import static org.junit.Assert.*;

/**
 * Tests the correct use case of setting a task as done and undone.
 *
 * Steps taken:
 * 1) Add two tasks
 * 2) Set both to done
 * 3) Go to display done
 * 4) Set the first one to undone
 * 5) Go to display
 * 6) Exit
 * 
 * Fifth integration test.
 */
public class IntegrationTest5 {
    @Rule
    public final ExpectedSystemExit exit = ExpectedSystemExit.none();
    
    // Tests add from Main
    @Test
    public void testMain() {
       FileStorage testStorage = new FileStorage();
       try {
           testStorage.loadSettings();
       } catch (Exception e) {
           fail();
       }
       String previousDirectory = testStorage.getFileDirectory();

       // Commands to be carried out
       String directoryCommand = "saveto testDirectory/";
       String clearCommand = "clear";
       String addCommand1 = "add task 1";
       String addCommand2 = "add task 2";
       String doneCommand1 = "done 1";
       String doneCommand2 = "done 1";
       String displayCommand1 = "display done";
       String undoneCommand1 = "undone 1";
       String displayCommand2 = "display";
       String exitCommand = "exit";
       String[] testCommands = {
               directoryCommand,
               clearCommand,
               addCommand1,
               addCommand2,
               doneCommand1,
               doneCommand2,
               displayCommand1,
               undoneCommand1,
               displayCommand2,
               exitCommand};

       exit.expectSystemExit();
       exit.checkAssertionAfterwards(new Assertion() {
           @Override
           public void checkAssertion() throws Exception {

               // Checks if the data is intact!
               ArrayList<TodoItem> testTodoItems;

               try {
                   testStorage.loadSettings();
                   testTodoItems = testStorage.loadFile();
               } catch (Exception e) {
                   fail();
                   return;
               }

               // There are only two tasks added.
               assertEquals(2, testTodoItems.size());
               
               String testInput1 = "task 1";
               String testInput2 = "task 2";

               // The first task is undone, as it received both done and undone commands.
               assertEquals(testInput1, testTodoItems.get(0).getTaskName());
               assertFalse(testTodoItems.get(0).isDone());

               // The second task should remain as done, as it received only the done command.
               assertEquals(testInput2, testTodoItems.get(1).getTaskName());
               assertTrue(testTodoItems.get(1).isDone());

               // Cleanup
               testStorage.updateFile(new ArrayList<TodoItem>());
               testStorage.changeSettings(previousDirectory, null, null);
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