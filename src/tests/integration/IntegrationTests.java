package tests.integration;

import app.Main;
import app.model.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.Assertion;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class IntegrationTests {
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
       String directoryCommand1 = "saveto testDirectory/";
       String clearCommand = "clear";
       String addCommand1 = "add task 1";
       String addCommand2 = "add task 2 priority high";
       String addCommand3 = "add task 3 due tomorrow";
       String addCommand4 = "add task 4 start today end tomorrow";
       String[] testCommands = {directoryCommand1, clearCommand, addCommand1, addCommand2, addCommand3, addCommand4};

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

               String testInput1 = "task 1";
               String testInput2 = "task 2";
               String testInput3 = "task 3";
               String testInput4 = "task 4";
               assertEquals(testInput3, testTodoItems.get(0).getTaskName());
               assertEquals(testInput4, testTodoItems.get(1).getTaskName());
               assertEquals(testInput2, testTodoItems.get(2).getTaskName());
               assertEquals(testInput1, testTodoItems.get(3).getTaskName());

               testStorage.updateFile(new ArrayList<TodoItem>());
               testStorage.changeDirectory(previousDirectory);
           }
       });

       // Carry out commands
       try {
           Main.main(testCommands);
       } catch (Exception e) {}
    }
}