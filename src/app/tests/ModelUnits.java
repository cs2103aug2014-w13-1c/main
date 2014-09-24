package app.tests;

import app.model.TodoItem;
import app.model.TodoItemList;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;

public class ModelUnits {

    // Tests add operation for all three data types (and clear)
    @Test
    public void testAdd() {
        TodoItemList testedList = new TodoItemList();
        testedList.clearTodoItems();
        
        String testInput1 = "Test String 1";
        Date startDate1 = new Date();
        Date endDate1 = new Date();
        
        testedList.addTodoItem(new TodoItem(testInput1, startDate1, endDate1));
        
        assertEquals(1, testedList.countTodoItems());
        assertEquals(testInput1, testedList.readTodoItem(0).getTaskName());
        assertEquals(TodoItem.EVENT, testedList.readTodoItem(0).getTodoItemType());
        
        String testInput2 = "Test String 2";
        Date endDate2 = new Date();
        
        testedList.addTodoItem(new TodoItem(testInput2, null, endDate2));
        
        assertEquals(2, testedList.countTodoItems());
        assertEquals(testInput2, testedList.readTodoItem(1).getTaskName());
        assertEquals(TodoItem.DEADLINE, testedList.readTodoItem(1).getTodoItemType());
        
        String testInput3 = "Test String 3";
        
        testedList.addTodoItem(new TodoItem(testInput3, null, null));
        
        assertEquals(3, testedList.countTodoItems());
        assertEquals(testInput3, testedList.readTodoItem(2).getTaskName());
        assertEquals(TodoItem.FLOATING, testedList.readTodoItem(2).getTodoItemType());
        
        testedList.clearTodoItems();
    }

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
}
