package tests.units;
//@author A0114914L

import java.io.IOException;
import java.util.ArrayList;

import app.controllers.ActionController;
import app.controllers.CommandController;
import app.controllers.TaskController;
import app.services.ParsingService;
import app.controllers.UndoController;
import app.helpers.CommandObject;
import app.model.ModelManager;
import app.model.TodoItem;
import static org.junit.Assert.*;

import org.junit.Test;

public class ActionControllerUnits {
    ActionController actionTest;
    UndoController undoControllerTest = UndoController.getUndoController();
    ParsingService parserTest = new ParsingService();
    CommandObject commandObjectTest = new CommandObject();
    
    @Test
    public void canAddCorrectly() {
        ModelManager manager;
        try {
            manager = new ModelManager();
        } catch (Exception e) {
            fail();
            return;
        }
        
        actionTest = new ActionController(manager);
        actionTest.setUndoController(undoControllerTest);
        
        commandObjectTest = parserTest.parseCommand("saveto testDirectory");
        actionTest.changeSaveLocation(commandObjectTest);
        commandObjectTest = parserTest.parseCommand("clear");
        actionTest.clear(commandObjectTest);
        commandObjectTest = parserTest.parseCommand("add task 1");
        actionTest.add(commandObjectTest);
        commandObjectTest = parserTest.parseCommand("add *&$(*&$)(@ due yesterday");
        actionTest.add(commandObjectTest);
        commandObjectTest = parserTest.parseCommand("add 34987314 due today");
        actionTest.add(commandObjectTest);
        commandObjectTest = parserTest.parseCommand("add hello world due tomorrow");
        actionTest.add(commandObjectTest);
        assertEquals(4, manager.countTasks());
        
        commandObjectTest = parserTest.parseCommand("clear");
        actionTest.clear(commandObjectTest);
        assertEquals(0, manager.countTasks());
        
        commandObjectTest = parserTest.parseCommand("saveto .");
        actionTest.changeSaveLocation(commandObjectTest);
        assertEquals("./", manager.getFileDirectory());
    }
    
    @Test
    public void canUpdateAndDeleteCorrectly() {
        ModelManager manager;
        try {
            manager = new ModelManager();
        } catch (Exception e) {
            fail();
            return;
        }
        
        actionTest = new ActionController(manager);
        undoControllerTest.clear();
        actionTest.setUndoController(undoControllerTest);
        
        commandObjectTest = parserTest.parseCommand("saveto testDirectory");
        actionTest.changeSaveLocation(commandObjectTest);
        commandObjectTest = parserTest.parseCommand("clear");
        actionTest.clear(commandObjectTest);
        commandObjectTest = parserTest.parseCommand("add task 1");
        actionTest.add(commandObjectTest);
        commandObjectTest = parserTest.parseCommand("add *&$(*&$)(@ due yesterday");
        actionTest.add(commandObjectTest);
        commandObjectTest = parserTest.parseCommand("add 34987314 due today");
        actionTest.add(commandObjectTest);
        commandObjectTest = parserTest.parseCommand("add hello world due tomorrow");
        actionTest.add(commandObjectTest);
        
        commandObjectTest = parserTest.parseCommand("update 2 task 2 due 3 days later");
        actionTest.update(commandObjectTest, manager.getTodoItemList());
        assertEquals("task 2", manager.getTodoItemList().get(2).getTaskName());
        commandObjectTest = parserTest.parseCommand("delete 2");
        actionTest.delete(commandObjectTest, manager.getTodoItemList());
        assertEquals(3, manager.countTasks());
        assertEquals("*&$(*&$)(@", manager.getTodoItemList().get(0).getTaskName());
        assertEquals("task 2", manager.getTodoItemList().get(1).getTaskName());
        assertEquals("task 1", manager.getTodoItemList().get(2).getTaskName());
        commandObjectTest = parserTest.parseCommand("saveto .");
        actionTest.changeSaveLocation(commandObjectTest);
        assertEquals("./", manager.getFileDirectory());
    }

    @Test
    public void canUndoAndRedoCorrectly() {
        ModelManager manager;
        try {
            manager = new ModelManager();
        } catch (Exception e) {
            fail();
            return;
        }
        
        actionTest = new ActionController(manager);
        undoControllerTest.clear();
        actionTest.setUndoController(undoControllerTest);
        
        commandObjectTest = parserTest.parseCommand("saveto testDirectory");
        actionTest.changeSaveLocation(commandObjectTest);
        commandObjectTest = parserTest.parseCommand("clear");
        actionTest.clear(commandObjectTest);
        commandObjectTest = parserTest.parseCommand("add task 1");
        actionTest.add(commandObjectTest);
        commandObjectTest = parserTest.parseCommand("add *&$(*&$)(@ due yesterday");
        actionTest.add(commandObjectTest);
        commandObjectTest = parserTest.parseCommand("add 34987314 due today");
        actionTest.add(commandObjectTest);
        commandObjectTest = parserTest.parseCommand("add hello world due tomorrow");
        actionTest.add(commandObjectTest);
        
        commandObjectTest = parserTest.parseCommand("delete 2");
        actionTest.delete(commandObjectTest, manager.getTodoItemList());
        commandObjectTest = parserTest.parseCommand("delete 2");
        actionTest.delete(commandObjectTest, manager.getTodoItemList());
        assertEquals(2, manager.countTasks());
        commandObjectTest = parserTest.parseCommand("undo");
        actionTest.undo(commandObjectTest);
        assertEquals(3, manager.countTasks());
        actionTest.undo(commandObjectTest);
        assertEquals(4, manager.countTasks());
        commandObjectTest = parserTest.parseCommand("redo");
        actionTest.redo(commandObjectTest);
        assertEquals(3, manager.countTasks());
        
        assertEquals("*&$(*&$)(@", manager.getTodoItemList().get(0).getTaskName());
        assertEquals("hello world", manager.getTodoItemList().get(1).getTaskName());
        assertEquals("task 1", manager.getTodoItemList().get(2).getTaskName());

        commandObjectTest = parserTest.parseCommand("clear");
        actionTest.clear(commandObjectTest);
        assertEquals(0, manager.countTasks());
        
        commandObjectTest = parserTest.parseCommand("saveto .");
        actionTest.changeSaveLocation(commandObjectTest);
        assertEquals("./", manager.getFileDirectory());
    }
    
    @Test
    public void canDisplayDoneUndoneCorrectly() {
        CommandController commandControllerTest = new CommandController();
        undoControllerTest.clear();
        TaskController taskControllerTest = new TaskController();
        commandControllerTest.setTaskController(taskControllerTest);
        commandControllerTest.setUndoController(undoControllerTest);
        
        ActionController actionTest = commandControllerTest.getActionController();
        
        commandObjectTest = parserTest.parseCommand("saveto testDirectory");
        actionTest.changeSaveLocation(commandObjectTest);
        commandObjectTest = parserTest.parseCommand("clear");
        actionTest.clear(commandObjectTest);
        commandObjectTest = parserTest.parseCommand("add task 1");
        actionTest.add(commandObjectTest);
        commandObjectTest = parserTest.parseCommand("add *&$(*&$)(@ due yesterday");
        actionTest.add(commandObjectTest);
        commandObjectTest = parserTest.parseCommand("add 34987314 due today");
        actionTest.add(commandObjectTest);
        commandObjectTest = parserTest.parseCommand("add hello world due tomorrow");
        actionTest.add(commandObjectTest);
        
        commandObjectTest = parserTest.parseCommand("display done");
        actionTest.display(commandObjectTest);
        ArrayList<TodoItem> returnListTest = actionTest.getReturnList();
        assertEquals(0, returnListTest.size());
        
        commandObjectTest = parserTest.parseCommand("display");
        actionTest.display(commandObjectTest);
        returnListTest = actionTest.getReturnList();
        assertEquals(4, returnListTest.size());
        
        commandObjectTest = parserTest.parseCommand("done 1");
        actionTest.done(commandObjectTest, actionTest.getReturnList());
        commandObjectTest = parserTest.parseCommand("display");
        actionTest.display(commandObjectTest);
        returnListTest = actionTest.getReturnList();
        assertEquals(3, returnListTest.size());
        
        commandObjectTest = parserTest.parseCommand("display done");
        actionTest.display(commandObjectTest);
        returnListTest = actionTest.getReturnList();
        assertEquals(1, returnListTest.size());
        
        commandObjectTest = parserTest.parseCommand("undone 1");
        actionTest.undone(commandObjectTest, returnListTest);
        commandObjectTest = parserTest.parseCommand("display");
        actionTest.display(commandObjectTest);
        returnListTest = actionTest.getReturnList();
        assertEquals(4, returnListTest.size());
        
        commandObjectTest = parserTest.parseCommand("clear");
        actionTest.clear(commandObjectTest);
        assertEquals(0, commandControllerTest.getModelManager().countTasks());
        
        commandObjectTest = parserTest.parseCommand("saveto .");
        actionTest.changeSaveLocation(commandObjectTest);
        assertEquals("./", commandControllerTest.getModelManager().getFileDirectory());
        
    }
    
    @Test
    public void canSearchCorrectly() {
        CommandController commandControllerTest = new CommandController();
        undoControllerTest.clear();
        TaskController taskControllerTest = new TaskController();
        commandControllerTest.setTaskController(taskControllerTest);
        commandControllerTest.setUndoController(undoControllerTest);
        
        ActionController actionTest = commandControllerTest.getActionController();
        
        commandObjectTest = parserTest.parseCommand("saveto testDirectory");
        actionTest.changeSaveLocation(commandObjectTest);
        commandObjectTest = parserTest.parseCommand("clear");
        actionTest.clear(commandObjectTest);
        commandObjectTest = parserTest.parseCommand("add task 1");
        actionTest.add(commandObjectTest);
        commandObjectTest = parserTest.parseCommand("add tusk 1 due yesterday");
        actionTest.add(commandObjectTest);
        commandObjectTest = parserTest.parseCommand("add tesk 1 due today");
        actionTest.add(commandObjectTest);
        commandObjectTest = parserTest.parseCommand("add task 2 priority high");
        actionTest.add(commandObjectTest);
        
        commandObjectTest = parserTest.parseCommand("search task");
        actionTest.search(commandObjectTest);
        ArrayList<TodoItem> returnListTest = actionTest.getReturnList();
        assertEquals(2, returnListTest.size());
        assertEquals("task 2", returnListTest.get(0).getTaskName());
        assertEquals("task 1", returnListTest.get(1).getTaskName());
        
        commandObjectTest = parserTest.parseCommand("clear");
        actionTest.clear(commandObjectTest);
        assertEquals(0, commandControllerTest.getModelManager().countTasks());
        
        commandObjectTest = parserTest.parseCommand("saveto .");
        actionTest.changeSaveLocation(commandObjectTest);
        assertEquals("./", commandControllerTest.getModelManager().getFileDirectory());
        
    }
    
    @Test
    public void canChangeSettingsCorrectly() {
        CommandController commandControllerTest = new CommandController();
        undoControllerTest.clear();
        TaskController taskControllerTest = new TaskController();
        commandControllerTest.setTaskController(taskControllerTest);
        commandControllerTest.setUndoController(undoControllerTest);
        
        ActionController actionTest = commandControllerTest.getActionController();
        
        String oldDirectory = commandControllerTest.getModelManager().getFileDirectory();
        Boolean oldRandomColors = commandControllerTest.getModelManager().areRandomColorsEnabled();
        Boolean oldNotifications = commandControllerTest.getModelManager().areNotificationsEnabled();
        actionTest.changeSettings("testDirectory", true, true);
        
        assertEquals("testDirectory/", commandControllerTest.getModelManager().getFileDirectory());
        assertEquals(true, commandControllerTest.areRandomColorsEnabled());
        assertEquals(true, commandControllerTest.areNotificationsEnabled());
        
        actionTest.changeSettings(oldDirectory, oldRandomColors, oldNotifications);
        
    }
}
