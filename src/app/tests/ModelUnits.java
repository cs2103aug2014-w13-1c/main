package app.tests;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

import app.model.TodoItem;
import app.model.TodoItemList;

public class ModelUnits {

    // Tests add operation for all three data types
    @Test
    public void testAdd() {
        TodoItemList testedList = new TodoItemList();
        
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
    }

    // Tests delete operation
    @Test
    public void testDelete() {
        TodoItemList testedList = new TodoItemList();
        
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
    }
    

    // Tests delete operation
    @Test
    public void testAddAndDelete() {
        TodoItemList testedList = new TodoItemList();
        
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
    }
    
 // Tests file loading
    @Test
    public void testFileLoad() {
        TodoItemList testedList = new TodoItemList();
        
        assertEquals(TodoItemList.LOAD_SUCCESS, testedList.getLoadStatus());
    }
}
