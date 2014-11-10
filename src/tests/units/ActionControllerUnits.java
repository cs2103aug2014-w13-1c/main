package tests.units;
//@author A0114914L

import java.io.IOException;

import app.controllers.ActionController;
import app.services.ParsingService;
import app.helpers.CommandObject;
import app.model.ModelManager;
import static org.junit.Assert.*;

import org.junit.Test;

public class ActionControllerUnits {
    ActionController actionTest;
    ParsingService parserTest = new ParsingService();
    CommandObject commandObjectTest = new CommandObject();
    
    @Test
    public void canAddCorrectly() {
        ModelManager manager;
        try {
            manager = new ModelManager();
        } catch (IOException e) {
            e.getMessage();
            fail();
            return;
        }
        actionTest = new ActionController(manager);
        
        commandObjectTest = parserTest.parseCommand("clear");
        actionTest.clear(commandObjectTest);
        commandObjectTest = parserTest.parseCommand("add task 1");
        actionTest.add(commandObjectTest);
        commandObjectTest = parserTest.parseCommand("add *&$(*&$)(@");
        actionTest.add(commandObjectTest);
        commandObjectTest = parserTest.parseCommand("add 34987314");
        actionTest.add(commandObjectTest);
        commandObjectTest = parserTest.parseCommand("add hello world");
        actionTest.add(commandObjectTest);
        System.out.println(manager.getTodoItemList().get(0).getTaskName());
        assertEquals(4, manager.countTasks());
    }

}
