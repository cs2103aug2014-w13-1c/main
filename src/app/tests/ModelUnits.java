package app.tests;

import app.model.TodoItem;
import app.model.TodoItemList;
import app.model.TodoItemSorter;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

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
    @Test
    public void testTodoItemSorter() {
        String testInput1 = "Test String 1";
        String testInput2 = "Test String 2";
        String testInput3 = "Test String 3";
        String testInput4 = "Test String 4";
        String testInput5 = "Test String 5";
        String testInput6 = "Test String 6";
        Date earlyDate = new Date();
        Date lateDate = new Date();
        
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
        for (int i = 0; i < testedList1.countTodoItems() - 1; i++) {
            TodoItem currentTodoItem = currentTodoItems.get(i);
            TodoItem nextTodoItem = currentTodoItems.get(i + 1);
            System.out.println(currentTodoItem.getTaskName() + " " + nextTodoItem.getTaskName() + " " + currentTodoItem.getStartDate() + " " + nextTodoItem.getStartDate());
            if (currentTodoItem.getTaskName() == null) {
                if (nextTodoItem.getTaskName() == null) {
                   if (currentTodoItem.getEndDate() != null) {
                       if (nextTodoItem.getEndDate() == null) fail();
                       if (currentTodoItem.getEndDate().getTime() > nextTodoItem.getEndDate().getTime()) fail();
                   }
                }
            } else {
                if (nextTodoItem.getTaskName() == null) fail();
                if (currentTodoItem.getTaskName().compareTo(nextTodoItem.getTaskName()) > 0) fail();
                if (currentTodoItem.getTaskName().equals(nextTodoItem.getTaskName())) {
                    if (currentTodoItem.getEndDate() != null) {
                        if (nextTodoItem.getEndDate() == null) fail();
                        if (currentTodoItem.getEndDate().getTime() > nextTodoItem.getEndDate().getTime()) fail();
                    }
                }
            }
        }
        
        // StartDate then Priority
        TodoItemSorter.sortingStyle = 1;
        TodoItemSorter.resortTodoList(testedList1);
        currentTodoItems = testedList1.getTodoItems();
        
        // EndDate then Priority
        TodoItemSorter.sortingStyle = 2;
        TodoItemSorter.resortTodoList(testedList1);
        
        // Priority then EndDate
        TodoItemSorter.sortingStyle = 3;
        TodoItemSorter.resortTodoList(testedList1);
    }
/*
    // Tests delete operation
    @Test
    public void testDelete() {
        TodoItemList testedList = new TodoItemList();
        testedList.clearTodoItems();
        
        String testInput1 = "Test String 1";
        Date startDate1 = new Date();
        Date endDate1 = new Date();
        String testInput2 = "Test String 2";
        Date endDate2 = new Date();
        String testInput3 = "Test String 3";
        String testInput4 = "Test String 4";
        
        testedList.addTodoItem(new TodoItem(testInput1, startDate1, endDate1));
        testedList.addTodoItem(new TodoItem(testInput2, null, endDate2));
        testedList.addTodoItem(new TodoItem(testInput3, null, null));
        testedList.addTodoItem(new TodoItem(testInput4, null, null));
        
        TodoItem result = testedList.deleteTodoItem(1);
        
        assertEquals(testInput2, result.getTaskName());
        assertEquals(3, testedList.countTodoItems());
        assertEquals(testInput1, testedList.readTodoItem(0).getTaskName());
        assertEquals(testInput3, testedList.readTodoItem(1).getTaskName());
        assertEquals(testInput4, testedList.readTodoItem(2).getTaskName());
        
        result = testedList.deleteTodoItem(0);
        
        assertEquals(testInput1, result.getTaskName());
        assertEquals(2, testedList.countTodoItems());
        assertEquals(testInput3, testedList.readTodoItem(0).getTaskName());
        assertEquals(testInput4, testedList.readTodoItem(1).getTaskName());
        
        result = testedList.deleteTodoItem(1);
        
        assertEquals(testInput4, result.getTaskName());
        assertEquals(1, testedList.countTodoItems());
        assertEquals(testInput3, testedList.readTodoItem(0).getTaskName());
        
        result = testedList.deleteTodoItem(1);
        
        assertEquals(null, result);
        assertEquals(1, testedList.countTodoItems());
        assertEquals(testInput3, testedList.readTodoItem(0).getTaskName());
        
        result = testedList.deleteTodoItem(0);
        
        assertEquals(testInput3, result.getTaskName());
        assertEquals(0, testedList.countTodoItems());
        
        testedList.clearTodoItems();
    }
    

    // Tests add and delete in tandem
    @Test
    public void testAddAndDelete() {
        TodoItemList testedList = new TodoItemList();
        testedList.clearTodoItems();
        
        String testInput1 = "Test String 1";
        Date startDate1 = new Date();
        Date endDate1 = new Date();
        String testInput2 = "Test String 2";
        Date endDate2 = new Date();
        String testInput3 = "Test String 3";
        String testInput4 = "Test String 4";
        
        testedList.addTodoItem(new TodoItem(testInput1, startDate1, endDate1));
        testedList.addTodoItem(new TodoItem(testInput2, null, endDate2));
        testedList.addTodoItem(new TodoItem(testInput3, null, null));
        testedList.addTodoItem(new TodoItem(testInput4, null, null));
        
        TodoItem result = testedList.deleteTodoItem(1);
        testedList.addTodoItem(result);
        
        assertEquals(4, testedList.countTodoItems());
        assertEquals(testInput1, testedList.readTodoItem(0).getTaskName());
        assertEquals(testInput3, testedList.readTodoItem(1).getTaskName());
        assertEquals(testInput4, testedList.readTodoItem(2).getTaskName());
        assertEquals(testInput2, testedList.readTodoItem(3).getTaskName());
        
        testedList.clearTodoItems();
    }
    
    // Tests update operation
    @Test
    public void testUpdate() {
        TodoItemList testedList = new TodoItemList();
        testedList.clearTodoItems();
        
        String testInput1 = "Test String 1";
        Date startDate1 = new Date();
        Date endDate1 = new Date();
        String testInput2 = "Test String 2";
        String testInput3 = "Test String 3";
        
        testedList.addTodoItem(new TodoItem(testInput1, startDate1, endDate1));
        testedList.addTodoItem(new TodoItem(testInput2, null, endDate1));
        testedList.addTodoItem(new TodoItem(testInput3, null, null));
        
        String testInput5 = "Test String 5";
        String testInput6 = "Test String 6";
        String testInput7 = "Test String 7";
        
        testedList.updateTodoItem(0, testInput5, null, null);
        testedList.updateTodoItem(1, testInput6, startDate1, endDate1);
        testedList.updateTodoItem(2, testInput7, null, endDate1);
        
        assertEquals(3, testedList.countTodoItems());
        assertEquals(testInput5, testedList.readTodoItem(0).getTaskName());
        assertEquals(testInput6, testedList.readTodoItem(1).getTaskName());
        assertEquals(testInput7, testedList.readTodoItem(2).getTaskName());
        assertEquals(TodoItem.FLOATING, testedList.readTodoItem(0).getTodoItemType());
        assertEquals(TodoItem.EVENT, testedList.readTodoItem(1).getTodoItemType());
        assertEquals(TodoItem.DEADLINE, testedList.readTodoItem(2).getTodoItemType());
        
        testedList.clearTodoItems();
    }
    
    // Tests file loading
    @Test
    public void testFileLoad() {
        TodoItemList testedList = new TodoItemList();
        
        assertEquals(TodoItemList.LOAD_SUCCESS, testedList.getLoadStatus());
    }
    
    // Tests file writing and loading in tandem
    @Test
    public void testMultiFileWriteAndLoad() {
        TodoItemList testedList1 = new TodoItemList("testfile.json");
        testedList1.clearTodoItems();

        assertEquals(TodoItemList.LOAD_SUCCESS, testedList1.getLoadStatus());
        assertEquals(0, testedList1.countTodoItems());
        
        String testInput1 = "Test String 1";
        Date startDate1 = new Date();
        Date endDate1 = new Date();
        String testInput2 = "Test String 2";
        String testInput3 = "Test String 3";
        
        testedList1.addTodoItem(new TodoItem(testInput1, startDate1, endDate1));
        assertEquals(TodoItemList.WRITE_SUCCESS, testedList1.getWriteStatus());
        testedList1.addTodoItem(new TodoItem(testInput2, null, endDate1));
        assertEquals(TodoItemList.WRITE_SUCCESS, testedList1.getWriteStatus());
        testedList1.addTodoItem(new TodoItem(testInput3, null, null));
        assertEquals(TodoItemList.WRITE_SUCCESS, testedList1.getWriteStatus());
        
        TodoItemList testedList2 = new TodoItemList("testfile.json");
        
        assertEquals(3, testedList2.countTodoItems());
        
        assertEquals(testInput1, testedList2.readTodoItem(0).getTaskName());
        assertEquals(testInput2, testedList2.readTodoItem(1).getTaskName());
        assertEquals(testInput3, testedList2.readTodoItem(2).getTaskName());
        
        assertEquals(startDate1.getTime(), testedList2.readTodoItem(0).getStartDate().getTime());
        assertEquals(endDate1.getTime(), testedList2.readTodoItem(0).getEndDate().getTime());
        assertEquals(null, testedList2.readTodoItem(1).getStartDate());
        assertEquals(endDate1.getTime(), testedList2.readTodoItem(0).getEndDate().getTime());
        assertEquals(null, testedList2.readTodoItem(2).getStartDate());
        assertEquals(null, testedList2.readTodoItem(2).getEndDate());
        
        testedList2.deleteTodoItem(1);
        
        TodoItemList testedList3 = new TodoItemList("testfile.json");
        
        assertEquals(2, testedList3.countTodoItems());
        assertEquals(testInput1, testedList3.readTodoItem(0).getTaskName());
        assertEquals(testInput3, testedList3.readTodoItem(1).getTaskName());
        
        testedList1.clearTodoItems();
    }
    
 // Tests priorities
    @Test
    public void testPriorities() {
        TodoItemList testedList = new TodoItemList();
        testedList.clearTodoItems();
        
        String testInput1 = "Test String 1";
        Date startDate1 = new Date();
        Date endDate1 = new Date();
        
        testedList.addTodoItem(new TodoItem(testInput1, startDate1, endDate1));
        
        assertEquals(TodoItem.MEDIUM, testedList.readTodoItem(0).getPriority());
        
        String testInput2 = "Test String 2";
        Date startDate2 = new Date();
        
        testedList.addTodoItem(new TodoItem(testInput2, startDate2, null, TodoItem.HIGH));
        
        assertEquals(TodoItem.HIGH, testedList.readTodoItem(1).getPriority());
        
        String testInput3 = "Test Stringy 3";
        String dummyInput1 = "Very high";
        
        testedList.addTodoItem(new TodoItem(testInput3, null, null, dummyInput1));
        
        assertEquals(TodoItem.MEDIUM, testedList.readTodoItem(2).getPriority());
        
        testedList.addTodoItem(new TodoItem(testInput3, null, null, null));
        
        assertEquals(TodoItem.MEDIUM, testedList.readTodoItem(3).getPriority());
        
        testedList.updateTodoItem(1, testInput2, startDate2, null, TodoItem.HIGH);
        
        assertEquals(TodoItem.HIGH, testedList.readTodoItem(1).getPriority());
        
        testedList.updateTodoItem(1, testInput1, startDate1, endDate1, dummyInput1);
        
        assertEquals(TodoItem.HIGH, testedList.readTodoItem(1).getPriority());
        
        testedList.clearTodoItems();
    }*/
}
