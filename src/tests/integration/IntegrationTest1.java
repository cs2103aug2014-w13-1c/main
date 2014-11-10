package tests.integration;
//@author A0116703N

import app.Main;
import app.model.FileStorage;
import app.model.TodoItem;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.Assertion;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * First integration test.
 * Tests the correct use case for the add, saveto and clear commands.
 */
public class IntegrationTest1 {
    @Rule
    public final ExpectedSystemExit exit = ExpectedSystemExit.none();
    
    // Tests add from Main
    @Test
    public void testMainAdd() {
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
       String addCommand2 = "add task 2 priority high";
       String addCommand3 = "add task 3 due tomorrow";
       String addCommand4 = "add task 4 start yesterday end today";
       String exitCommand = "exit";
       String[] testCommands = {
               directoryCommand,
               clearCommand,
               addCommand1,
               addCommand2,
               addCommand3,
               addCommand4,
               exitCommand};

       exit.expectSystemExit();
       exit.checkAssertionAfterwards(() -> {

           // Checks if the data is intact!
           ArrayList<TodoItem> testTodoItems;

           try {
               testStorage.loadSettings();
               testTodoItems = testStorage.loadFile();
           } catch (Exception e) {
               fail();
               return;
           }

           // After cleaning and adding, we should have four tasks.
           assertEquals(4, testTodoItems.size());

           String testInput1 = "task 1";
           String testInput2 = "task 2";
           String testInput3 = "task 3";
           String testInput4 = "task 4";

           assertEquals(testInput4, testTodoItems.get(0).getTaskName());
           assertEquals(testInput3, testTodoItems.get(1).getTaskName());
           assertEquals(testInput2, testTodoItems.get(2).getTaskName());
           assertEquals(testInput1, testTodoItems.get(3).getTaskName());

           // Cleanup
           testStorage.updateFile(new ArrayList<TodoItem>());
           testStorage.changeSettings(previousDirectory, null, null);
       });

       // Carry out commands
       try {
           Main.main(testCommands);
       } catch (Exception e) {
           // Do nothing, this is already caught by the ExpectedSystemExit.
       }
    }
}