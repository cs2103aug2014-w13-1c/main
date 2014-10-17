package app.tests;

import app.model.FileStorage;
import app.model.ModelManager;
import app.model.TodoItem;
import app.model.TodoItemList;
import app.model.TodoItemSorter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;

import org.json.JSONObject;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ModelUnits {

    // Tests TodoItem constructor
    @Test
    public void testTodoItemConstructor() {
        String testInput1 = "Test String 1";
        Date startDate1 = new Date();
        Date endDate1 = new Date();
        String testInput2 = "Dummy priority";
        String testInput3 = "3. High";
        Boolean testBoolean1 = true;
        
        TodoItem testedTodoItem1 = new TodoItem(testInput1, startDate1, endDate1, testInput3, testBoolean1);
        assertEquals(testInput1, testedTodoItem1.getTaskName());
        assertEquals(startDate1.getTime(), testedTodoItem1.getStartDate().getTime());
        assertEquals(endDate1.getTime(), testedTodoItem1.getEndDate().getTime());
        assertEquals(TodoItem.HIGH, testedTodoItem1.getPriority());
        assertEquals(testBoolean1, testedTodoItem1.isDone());
        
        TodoItem testedTodoItem2 = new TodoItem(testInput1, startDate1, endDate1);
        assertEquals(testInput1, testedTodoItem2.getTaskName());
        assertEquals(startDate1.getTime(), testedTodoItem2.getStartDate().getTime());
        assertEquals(endDate1.getTime(), testedTodoItem2.getEndDate().getTime());
        assertEquals(TodoItem.MEDIUM, testedTodoItem2.getPriority());
        assertEquals(false, testedTodoItem2.isDone());
        
        TodoItem testedTodoItem3 = new TodoItem(null, null, null, testInput2, null);
        assertEquals(null, testedTodoItem3.getTaskName());
        assertEquals(null, testedTodoItem3.getStartDate());
        assertEquals(null, testedTodoItem3.getEndDate());
        assertEquals(TodoItem.MEDIUM, testedTodoItem3.getPriority());
        assertEquals(false, testedTodoItem3.isDone());
    }
    
    // Tests TodoItemList constructor
    @Test
    public void testTodoItemListConstructor() {
        TodoItemList testedList1 = new TodoItemList();
        assertEquals(0, testedList1.countTodoItems());
        
        ArrayList<TodoItem> inputArrayList1 = new ArrayList<TodoItem>();
        TodoItemList testedList2 = new TodoItemList(inputArrayList1);
        assertEquals(0, testedList2.countTodoItems());
        
        ArrayList<TodoItem> inputArrayList2 = new ArrayList<TodoItem>();
        inputArrayList2.add(new TodoItem(null, null, null));
        TodoItemList testedList3 = new TodoItemList(inputArrayList2);
        assertEquals(1, testedList3.countTodoItems());
    }
    
    // Tests TodoItemList create and delete
    @Test
    public void testTodoItemListAddAndDelete() {
        String testInput1 = "Test String 1";
        String testInput2 = "Test String 2";
        String testInput3 = "Test String 3";
        Date startDate1 = new Date();
        Date endDate1 = new Date();
        
        TodoItemList testedList1 = new TodoItemList();
        testedList1.addTodoItem(new TodoItem(testInput1, startDate1, endDate1));
        assertEquals(1, testedList1.countTodoItems());
        
        testedList1.addTodoItem(new TodoItem(testInput2, startDate1, null));
        assertEquals(2, testedList1.countTodoItems());
        
        testedList1.addTodoItem(new TodoItem(testInput2, null, endDate1));
        assertEquals(3, testedList1.countTodoItems());
        
        testedList1.addTodoItem(new TodoItem(testInput3, null, null));
        assertEquals(4, testedList1.countTodoItems());
        
        ArrayList<TodoItem> currentTestedList = testedList1.getTodoItems();
        assertEquals(TodoItem.EVENT, currentTestedList.get(0).getTodoItemType());
        assertEquals(TodoItem.ENDLESS, currentTestedList.get(1).getTodoItemType());
        assertEquals(TodoItem.DEADLINE, currentTestedList.get(2).getTodoItemType());
        assertEquals(TodoItem.FLOATING, currentTestedList.get(3).getTodoItemType());
        
        testedList1.deleteByUUID(currentTestedList.get(2).getUUID());
        assertEquals(3, testedList1.countTodoItems());
        currentTestedList = testedList1.getTodoItems();
        assertEquals(testInput1, currentTestedList.get(0).getTaskName());
        assertEquals(null, currentTestedList.get(1).getEndDate());
        assertEquals(null, currentTestedList.get(2).getEndDate());
        
        testedList1.clearTodoItems();
        assertEquals(0, testedList1.countTodoItems());
    }
    
    // Tests TodoItemSorter
    // Thou who trieth to debug this crawling horror, surrender.
    @Test
    public void testTodoItemSorter() {
        String testInput1 = "Test String 1";
        String testInput2 = "Test String 2";
        String testInput3 = "Test String 3";
        String testInput4 = "Test String 4";
        String testInput5 = "Test String 5";
        String testInput6 = "Test String 6";
        Date earlyDate = new Date();
        Date lateDate = new Date(earlyDate.getTime() + 100000);
        
        TodoItemList testedList1 = new TodoItemList();
        testedList1.addTodoItem(new TodoItem(testInput1, null, lateDate, TodoItem.HIGH, false));
        testedList1.addTodoItem(new TodoItem(testInput2, null, lateDate, TodoItem.MEDIUM, false));
        testedList1.addTodoItem(new TodoItem(null, earlyDate, lateDate, TodoItem.LOW, false));
        testedList1.addTodoItem(new TodoItem(testInput4, lateDate, earlyDate, TodoItem.MEDIUM, false));
        testedList1.addTodoItem(new TodoItem(testInput4, earlyDate, lateDate, TodoItem.HIGH, false));
        testedList1.addTodoItem(new TodoItem(null, null, lateDate, TodoItem.MEDIUM, false));
        testedList1.addTodoItem(new TodoItem(testInput5, null, lateDate, TodoItem.HIGH, false));
        testedList1.addTodoItem(new TodoItem(testInput6, null, lateDate, TodoItem.MEDIUM, false));
        
        // TaskName then EndDate
        TodoItemSorter.sortingStyle = 0;
        TodoItemSorter.resortTodoList(testedList1);
        ArrayList<TodoItem> currentTodoItems = testedList1.getTodoItems();
        for (int i = testedList1.countTodoItems() - 1; i > 0; i--) {
            TodoItem currentTodoItem = currentTodoItems.get(i);
            TodoItem nextTodoItem = currentTodoItems.get(i - 1);
            if (currentTodoItem.getTaskName() == null) {
                if (nextTodoItem.getTaskName() == null) {
                   if (currentTodoItem.getEndDate() != null) {
                       if (nextTodoItem.getEndDate() == null) fail();
                       if (currentTodoItem.getEndDate().getTime() < nextTodoItem.getEndDate().getTime()) fail();
                   }
                }
            } else {
                if (nextTodoItem.getTaskName() == null) fail();
                if (currentTodoItem.getTaskName().compareTo(nextTodoItem.getTaskName()) < 0) fail();
                if (currentTodoItem.getTaskName().equals(nextTodoItem.getTaskName())) {
                    if (currentTodoItem.getEndDate() != null) {
                        if (nextTodoItem.getEndDate() == null) fail();
                        if (currentTodoItem.getEndDate().getTime() < nextTodoItem.getEndDate().getTime()) fail();
                    }
                }
            }
        }
        
        // StartDate then Priority
        TodoItemSorter.sortingStyle = 1;
        TodoItemSorter.resortTodoList(testedList1);
        currentTodoItems = testedList1.getTodoItems();
        for (int i = testedList1.countTodoItems() - 1; i > 0; i--) {
            TodoItem currentTodoItem = currentTodoItems.get(i);
            TodoItem nextTodoItem = currentTodoItems.get(i - 1);
            if (currentTodoItem.getStartDate() == null) {
                if (nextTodoItem.getStartDate() == null) {
                   if (currentTodoItem.getPriority() != null) {
                       if (nextTodoItem.getPriority() == null) fail();
                       if (currentTodoItem.getPriority().compareTo(nextTodoItem.getPriority()) < 0) fail();
                   }
                }
            } else {
                if (nextTodoItem.getStartDate() == null) fail();
                if (currentTodoItem.getStartDate().getTime() < nextTodoItem.getStartDate().getTime()) fail();
                if (currentTodoItem.getStartDate().getTime() == nextTodoItem.getStartDate().getTime()) {
                    if (currentTodoItem.getPriority() != null) {
                        if (nextTodoItem.getPriority() == null) fail();
                        if (currentTodoItem.getPriority().compareTo(nextTodoItem.getPriority()) < 0) fail();
                    }
                }
            }
        }
        
        // EndDate then Priority
        TodoItemSorter.sortingStyle = 2;
        TodoItemSorter.resortTodoList(testedList1);
        currentTodoItems = testedList1.getTodoItems();
        for (int i = testedList1.countTodoItems() - 1; i > 0; i--) {
            TodoItem currentTodoItem = currentTodoItems.get(i);
            TodoItem nextTodoItem = currentTodoItems.get(i - 1);
            if (currentTodoItem.getEndDate() == null) {
                if (nextTodoItem.getEndDate() == null) {
                   if (currentTodoItem.getPriority() != null) {
                       if (nextTodoItem.getPriority() == null) fail();
                       if (currentTodoItem.getPriority().compareTo(nextTodoItem.getPriority()) < 0) fail();
                   }
                }
            } else {
                if (nextTodoItem.getEndDate() == null) fail();
                if (currentTodoItem.getEndDate().getTime() < nextTodoItem.getEndDate().getTime()) fail();
                if (currentTodoItem.getEndDate().getTime() == nextTodoItem.getEndDate().getTime()) {
                    if (currentTodoItem.getPriority() != null) {
                        if (nextTodoItem.getPriority() == null) fail();
                        if (currentTodoItem.getPriority().compareTo(nextTodoItem.getPriority()) < 0) fail();
                    }
                }
            }
        }
        
        // Priority then EndDate
        TodoItemSorter.sortingStyle = 3;
        TodoItemSorter.resortTodoList(testedList1);
        currentTodoItems = testedList1.getTodoItems();
        for (int i = testedList1.countTodoItems() - 1; i > 0; i--) {
            TodoItem currentTodoItem = currentTodoItems.get(i);
            TodoItem nextTodoItem = currentTodoItems.get(i - 1);
            if (currentTodoItem.getPriority() == null) {
                if (nextTodoItem.getPriority() == null) {
                   if (currentTodoItem.getEndDate() != null) {
                       if (nextTodoItem.getEndDate() == null) fail();
                       if (currentTodoItem.getEndDate().getTime() < nextTodoItem.getEndDate().getTime()) fail();
                   }
                }
            } else {
                if (nextTodoItem.getPriority() == null) fail();
                if (currentTodoItem.getPriority().compareTo(nextTodoItem.getPriority()) < 0) {
                    fail();
                }
                if (currentTodoItem.getPriority().equals(nextTodoItem.getPriority())) {
                    if (currentTodoItem.getEndDate() != null) {
                        if (nextTodoItem.getEndDate() == null) fail();
                        if (currentTodoItem.getEndDate().getTime() < nextTodoItem.getEndDate().getTime()) fail();
                    }
                }
            }
        }
    }
    
    // Tests FileStorage
    @Test
    public void testFileStorage() {
        FileStorage testStorage = new FileStorage();
        assertEquals(FileStorage.DEFAULT_FILE_NAME, testStorage.getFileName());
        assertEquals(FileStorage.DEFAULT_FILE_DIRECTORY, testStorage.getFileDirectory());
        
        // Settings file fixture
        FileWriter fileToWrite;
        try {
            fileToWrite = new FileWriter(FileStorage.SETTINGS_FILE_NAME);
        } catch (Exception e) {
            fail();
            return;
        }
        BufferedWriter writer = new BufferedWriter(fileToWrite);
        JSONObject settingsObject = new JSONObject();
        try {
            settingsObject.put("fileDirectory", "testDirectory/");
            settingsObject.put("displayStatus", false);
        } catch (Exception e) {
            fail();
        }
        try {
            writer.write(settingsObject.toString(2));
            writer.flush();
            fileToWrite.close();
        } catch (Exception e) {
            fail();
        }
        
        // Try to load settings file
        try {
            testStorage.loadSettings();
        } catch (Exception e) {
            fail();
        }
        
        assertEquals(FileStorage.DEFAULT_FILE_NAME, testStorage.getFileName());
        assertEquals("testDirectory/", testStorage.getFileDirectory());
        
        // Yay! Successfully loaded settings file!
        // Now we set up an empty watdo.json
        ArrayList<TodoItem> testTodoItems = new ArrayList<TodoItem>();
        try {
            testStorage.updateFile(testTodoItems);
        } catch (Exception e) {
            fail();
        }
        
        // Now we try to load it.
        FileReader fileToRead;
        try {
            fileToRead = new FileReader("testDirectory/watdo.json");
        } catch (FileNotFoundException e) {
            fail();
            return;
        }
        BufferedReader reader = new BufferedReader(fileToRead);
        String fileString = "";
        String line = "";
        try {
            while ((line = reader.readLine()) != null) {
                fileString += line;
            }
            fileToRead.close();
        } catch (Exception e) {
            fail();
        }
        
        assertEquals("[]", fileString);
        
        // Yay! Now we insert actual data inside!
        String testInput1 = "Test String 1";
        Date earlyDate = new Date();
        Date lateDate = new Date(earlyDate.getTime() + 100000);
        testTodoItems.add(new TodoItem(null, null, null));
        testTodoItems.add(new TodoItem(null, earlyDate, lateDate));
        testTodoItems.add(new TodoItem(testInput1, null, lateDate));
        testTodoItems.add(new TodoItem(testInput1, earlyDate, null));
        testTodoItems.add(new TodoItem(testInput1, earlyDate, lateDate, TodoItem.HIGH, true));
        try {
            testStorage.updateFile(testTodoItems);
        } catch (Exception e) {
            fail();
        }
        
        // Now we extract the data and see if it's the same.
        ArrayList<TodoItem> extractedResult;
        try {
            extractedResult = testStorage.loadFile();
        } catch (Exception e) {
            fail();
            return;
        }
        
        assertEquals(5, extractedResult.size());
        assertEquals(null, extractedResult.get(0).getTaskName());
        assertEquals(null, extractedResult.get(0).getStartDate());
        assertEquals(null, extractedResult.get(0).getEndDate());
        assertEquals(TodoItem.MEDIUM, extractedResult.get(0).getPriority());
        assertEquals(false, extractedResult.get(0).isDone());
        assertEquals(null, extractedResult.get(1).getTaskName());
        assertEquals(earlyDate.getTime(), extractedResult.get(1).getStartDate().getTime());
        assertEquals(lateDate.getTime(), extractedResult.get(1).getEndDate().getTime());
        assertEquals(lateDate.getTime(), extractedResult.get(1).getEndDate().getTime());
        assertEquals(TodoItem.HIGH, extractedResult.get(4).getPriority());
        assertEquals(true, extractedResult.get(4).isDone());
        
        // Yay! We're done! Time to clean up.
        try {
            testStorage.updateFile(new ArrayList<TodoItem>());
        } catch (Exception e) {
            fail();
        }
        
        // Switch back to old directory!
        try {
            testStorage.changeDirectory("");
        } catch (Exception e) {
            fail();
        }
        
        try {
            testStorage.loadSettings();
        } catch (Exception e) {
            fail();
        }
        
        assertEquals(FileStorage.DEFAULT_FILE_NAME, testStorage.getFileName());
        assertEquals("", testStorage.getFileDirectory());
    }
    
    // Everyone together now!
    @Test
    public void testModelManager() {
        ModelManager testManager1;
        try {
            testManager1 = new ModelManager();
        } catch (Exception e) {
            fail();
            return;
        }
        
        try {
            testManager1.changeFileDirectory("testDirectory/");
        } catch (Exception e) {
            fail();
        }
        
        // Create
        String testInput1 = "Test String 1";
        String testInput2 = "Test String 2";
        String testInput3 = "Test String 3";
        String testInput4 = "Test String 4";
        Date earlyDate = new Date();
        Date lateDate = new Date(earlyDate.getTime() + 100000);
        
        try {
            testManager1.addTask(testInput1, earlyDate, lateDate, TodoItem.HIGH, true);
            testManager1.addTask(testInput2, null, null, null, null);
            testManager1.addTask(testInput3, earlyDate, null, TodoItem.MEDIUM, null);
            testManager1.addTask(testInput4, null, earlyDate, TodoItem.LOW, false);
        } catch (Exception e) {
            fail();
        }
        
        ModelManager testManager2;
        try {
            testManager2 = new ModelManager();
        } catch (Exception e) {
            fail();
            return;
        }
        
        assertEquals(4, testManager2.countTasks());
        
        testManager2.setSortingStyle(0);
        
        assertEquals(testInput1, testManager2.getTodoItemList().get(0).getTaskName());
        assertEquals(testInput2, testManager2.getTodoItemList().get(1).getTaskName());
        assertEquals(testInput3, testManager2.getTodoItemList().get(2).getTaskName());
        assertEquals(testInput4, testManager2.getTodoItemList().get(3).getTaskName());
        
        // Sort
        testManager2.setSortingStyle(3);
        
        // Remember, collections.sort is stable.
        assertEquals(testInput4, testManager2.getTodoItemList().get(0).getTaskName());
        assertEquals(testInput2, testManager2.getTodoItemList().get(1).getTaskName());
        assertEquals(testInput3, testManager2.getTodoItemList().get(2).getTaskName());
        assertEquals(testInput1, testManager2.getTodoItemList().get(3).getTaskName());

        //Update
        Boolean[] testParameters = {false, false, false, true, true};
        
        try {
            testManager2.updateTask(testManager2.getTodoItemList().get(0).getUUID(), testParameters, null, null, null, TodoItem.HIGH, true);
        } catch (Exception e) {
            fail();
        }
        
        assertEquals(testInput2, testManager2.getTodoItemList().get(0).getTaskName());
        assertEquals(testInput3, testManager2.getTodoItemList().get(1).getTaskName());
        assertEquals(testInput4, testManager2.getTodoItemList().get(2).getTaskName());
        assertEquals(testInput1, testManager2.getTodoItemList().get(3).getTaskName());
        
        
        
        
        // Delete
        try {
            testManager2.deleteTask(testManager2.getTodoItemList().get(1).getUUID());
        } catch (Exception e) {
            fail();
        }
        
        assertEquals(3, testManager2.countTasks());
        
        assertEquals(testInput2, testManager2.getTodoItemList().get(0).getTaskName());
        assertEquals(testInput4, testManager2.getTodoItemList().get(1).getTaskName());
        assertEquals(testInput1, testManager2.getTodoItemList().get(2).getTaskName());
        
        // Clear
        try {
            testManager2.clearTasks();
        } catch (Exception e) {
            fail();
        }
        
        ModelManager testManager3;
        try {
            testManager3 = new ModelManager();
        } catch (Exception e) {
            fail();
            return;
        }
        
        assertEquals("testDirectory/watdo.json", testManager3.getFullFileName());
        assertEquals(0, testManager3.countTasks());
        
        try {
            testManager3.changeFileDirectory("");
        } catch (Exception e) {
            fail();
        }
    }
}