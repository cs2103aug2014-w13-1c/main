package app.tests;

import app.controllers.CommandController;
import app.model.TodoItemList;
import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.assertEquals;

public class CommandControllerUnits {
    
    CommandController commandTest = new CommandController();
    
    // Testing if tasks can be added
    @org.junit.Test
    public void canAddTasks() throws Exception {
        commandTest.parseCommand("clear");
        commandTest.parseCommand("add task 1");
        commandTest.parseCommand("add *&$(*&$)(@");
        commandTest.parseCommand("add 34987314");
        commandTest.parseCommand("add hello world");
        TodoItemList thing = new TodoItemList();
        assertEquals(4, thing.countTodoItems());
        
        commandTest.parseCommand("clear");
    }
    
    // Testing whether add todo with start and end date
    @org.junit.Test
    public void canParseStartEndDate() throws Exception {
        Calendar expectedStartDate = Calendar.getInstance();
        expectedStartDate.set(2000, 5, 6);
        Calendar expectedEndDate = Calendar.getInstance();
        expectedEndDate.set(2015, 9, 13);
        commandTest.parseCommand("add dummy start 6 June 2000 end 13 October 2015");
        TodoItemList thing = new TodoItemList();
        Calendar outputStartDate = Calendar.getInstance();
        outputStartDate.setTime(thing.readTodoItem(thing.countTodoItems()-1).getStartDate());
        Calendar outputEndDate = Calendar.getInstance();
        outputEndDate.setTime(thing.readTodoItem(thing.countTodoItems()-1).getEndDate());
        
        assertEquals(expectedStartDate.get(Calendar.DAY_OF_MONTH), outputStartDate.get(Calendar.DAY_OF_MONTH));
        assertEquals(expectedStartDate.get(Calendar.MONTH), outputStartDate.get(Calendar.MONTH));
        assertEquals(expectedStartDate.get(Calendar.YEAR), outputStartDate.get(Calendar.YEAR));
        
        assertEquals(expectedEndDate.get(Calendar.DAY_OF_MONTH), outputEndDate.get(Calendar.DAY_OF_MONTH));
        assertEquals(expectedEndDate.get(Calendar.MONTH), outputEndDate.get(Calendar.MONTH));
        assertEquals(expectedEndDate.get(Calendar.YEAR), outputEndDate.get(Calendar.YEAR));
        
        commandTest.parseCommand("clear");
    }
}