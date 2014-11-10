package tests.integration;
//@author A0116703N

import app.Main;
import app.model.FileStorage;
import app.model.TodoItem;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.Assertion;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * The corrupted watdo.json was just the mid-boss. Now comes the final challenge.
 * The corrupted settings.json stands between you and your very important tasks.
 *  
 * Fourth integration test.
 * Tests program behavior when settings data is compromised.
 * 
 * @author Nguyen Quoc Dat (A0116703N)
 *
 */
public class IntegrationTest4 {
    @Rule
    public final ExpectedSystemExit exit = ExpectedSystemExit.none();
    
    // Tests add from Main
    @Test
    public void testMain() {
        // First we load the old settings data.
        String settingsFileString = readFromFile("settings.json");
            
        // First we manually switch to the test directory
        FileStorage testStorage = new FileStorage();
        try {
            testStorage.loadSettings();
        } catch (Exception e) {
            fail();
        }
         
        try {
            testStorage.changeSettings("testDirectory/", null, null);
        } catch (Exception e) {
            fail();
        }
           
        // Then we make fixtures
        ArrayList<TodoItem> testTodoItems = getFixtures();
        
        // Then write those fixtures to testDirectory/watdo.json
        try {
            testStorage.updateFile(testTodoItems);
        } catch (Exception e) {
            fail();
        }

        // Then we load the string from testDirectory/watdo.json
        String dataFileString = readFromFile("testDirectory/watdo.json");
           
        // Now we shave half of the characters off from the settings string.
        // This string should now be meaningless.
        String badFileString = settingsFileString.substring(settingsFileString.length()/2);
           
        // Now we replace the settings file with the bad string.
        writeToFile(badFileString, "settings.json");
           
        // Now we setup the commands to be carried out.
        String[] testCommands = getCommands();
        // Now none of the commands should work except exit, now that the settings are corrupted.
           
        exit.expectSystemExit();
        exit.checkAssertionAfterwards(new Assertion() {
            @Override
            public void checkAssertion() throws Exception {
               // Now we check if there is any change to the data.
               String newFileString = readFromFile("testDirectory/watdo.json");
            
               // There should be no change.
               assertEquals(dataFileString, newFileString);
           
               // Good. No change. None of the commands have any effect.
               // Restore the old data.
               testStorage.updateFile(new ArrayList<TodoItem>());
               writeToFile(settingsFileString, "settings.json");
            }
       });
            
       // Carry out commands
       try {
           Main.main(testCommands);
       } catch (Exception e) {
           // Do nothing, this is already caught by the ExpectedSystemExit.
       }
    }
    
    /**
     * 
     * 
     * @return
     */
    private ArrayList<TodoItem> getFixtures() {
        ArrayList<TodoItem> testTodoItems = new ArrayList<TodoItem>();
        testTodoItems.add(new TodoItem("task 1", null, null));
        testTodoItems.add(new TodoItem("task 2", null, null, TodoItem.HIGH, null));
        testTodoItems.add(new TodoItem("task 3", null, new Date(), TodoItem.LOW, null));
        testTodoItems.add(new TodoItem("task 4", new Date(), new Date(), null, null));
        testTodoItems.add(new TodoItem("task 5", null, new Date(), TodoItem.LOW, null));
        testTodoItems.add(new TodoItem("task 6", null, new Date(), TodoItem.LOW, null));
        testTodoItems.add(new TodoItem("task 7", null, new Date(), null, null));
        return testTodoItems;
    }
    
    /**
     * Reads a file's data as a String and returns it.
     * 
     * @param targetFile The file path of the target file
     * @return The string containing a file's data
     */
    private String readFromFile(String targetFile) {
        // First we open the file
        FileReader fileToRead;
        try {
            fileToRead = new FileReader(targetFile);
        } catch (FileNotFoundException e) { // if no file found at stated path, error (we should have written it there)
            fail();
            return null;
        }
        
        // Successfully opened the file, now we get the data string.
        BufferedReader reader = new BufferedReader(fileToRead);

        String fileString = "";
        try {
            String line = "";
            while ((line = reader.readLine()) != null) {
                fileString += (line + "\n");
            }
            reader.close();
        } catch (Exception e) {
            fail();
        }
        
        return fileString;
    }
    
    /**
     * Writes the given string to the target file.
     * 
     * @param toWrite The string to be written to the target file.
     * @param targetFile The target file to be written to.
     */
    private void writeToFile(String toWrite, String targetFile) {
        // We write the bad string to the testDirectory/watdo.json file.
        FileWriter fileToWrite;
        try {
            fileToWrite = new FileWriter(targetFile);
        } catch (Exception e) {
            fail();
            return;
        }

        // Access successful, now we write the bad string out.
        BufferedWriter writer = new BufferedWriter(fileToWrite);
        try {
            writer.write(toWrite);
            writer.flush();
            writer.close();
            fileToWrite.close();
        } catch (Exception e) {
            fail();
            return;
        }
    }
    
    /**
     * Creates an array of commands to be carried out.
     * 
     * @return The string array of commands to feed into main's args array.
     */
    private String[] getCommands() {
        String directoryCommand = "saveto testDirectory/";
        String clearCommand = "clear";
        String addCommand1 = "add task 1";
        String addCommand2 = "add task 2 priority high";
        String addCommand3 = "add task 3 due november 11 1am priority low";
        String addCommand4 = "add task 4 start november 10 1am end november 11 12:30am";
        String addCommand5 = "add task 5 due november 15 1am priority low";
        String addCommand6 = "add task 6 due november 16 1am priority low";
        String addCommand7 = "add task 7 due november 19 1am";
        String sortCommand1 = "sort name";
        String sortCommand2 = "sort end";
        String sortCommand3 = "sort start";
        String sortCommand4 = "sort priority";
        String deleteCommand1 = "delete 7";
        String deleteCommand2 = "delete 6";
        String searchCommand = "search 5";
        String deleteCommand3 = "delete 1";
        String deleteCommand4 = "delete 1";
        String undoCommand = "undo";
        String redoCommand = "redo";
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
                sortCommand2,
                sortCommand3,
                sortCommand4,
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
        
        return testCommands;
    }
}