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
 * Second integration test.
 * Tests the correct use case for sort, search, delete, undo and redo.
 */
public class IntegrationTest2 {
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
       String addCommand2 = "add task 2 priority high";
       String addCommand3 = "add task 3 due november 11 1am priority low";
       String addCommand4 = "add task 4 start november 10 1am end november 11 12:30am";
       String addCommand5 = "add task 5 due november 15 1am priority low";
       String addCommand6 = "add task 6 due november 16 1am priority low";
       String addCommand7 = "add task 7 due november 19 1am";
       String sortCommand1 = "sort priority";
       String deleteCommand1 = "delete 7"; // Should delete task 6
       String sortCommand2 = "sort end";
       String deleteCommand2 = "delete 6"; // Should delete task 7
       String searchCommand = "search 5";
       String deleteCommand3 = "delete 1"; // Should delete task 5
       String deleteCommand4 = "delete 1"; // Should delete task 2, then task 1 (called twice)
       String undoCommand = "undo"; // Should recreate task 1, then task 2 (called twice)
       String redoCommand = "redo"; // Should delete task 2
       String exitCommand = "exit";
       
       String[] testCommands = {
               directoryCommand,
               clearCommand,
               addCommand1,
               addCommand2,
               addCommand3,
               addCommand4,
               addCommand5,
               addCommand6,
               addCommand7,
               sortCommand1,
               deleteCommand1,
               sortCommand2,
               deleteCommand2,
               searchCommand,
               deleteCommand3,
               deleteCommand4,
               deleteCommand4,
               undoCommand,
               undoCommand,
               redoCommand,
               exitCommand};
       
       exit.expectSystemExit();
       exit.checkAssertionAfterwards(() -> {

           // Now we check if the data is intact.
           ArrayList<TodoItem> testTodoItems;

           try {
               testStorage.loadSettings();
               testTodoItems = testStorage.loadFile();
           } catch (Exception e) {
               fail();
               return;
           }

           // The leftover data after the five deletes, two undos and one redo should be 3 items.
           assertEquals(3, testTodoItems.size());

           // Then leftover data should be task 1, task 4 then task 3.
           String testInput2 = "task 2";
           String testInput3 = "task 3";
           String testInput7 = "task 7";

           assertEquals(testInput3, testTodoItems.get(0).getTaskName());
           assertEquals(testInput7, testTodoItems.get(1).getTaskName());
           assertEquals(testInput2, testTodoItems.get(2).getTaskName());

           // Yay! We passed the tests. Now change back to previous directory.
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