package app.controllers;

import app.Main;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by jolly on 21/10/14.
 */
public class TaskControllerTest extends Main {

    private TaskController taskController;
    private CommandController commandController;

    @Before
    public void setUp() {
        commandController = new CommandController();
        commandController.setMainApp(this);
        taskController = taskController.getTaskController();
        taskController.setMainApp(this);
        commandController.parseCommand("clear");
    }

    @After
    public void tearDown() {
        commandController = null;
        taskController = null;
    }

    // testing the ability to filter done tasks
    @Test
    public void getDoneTasksTest() {
        commandController.parseCommand("add task 111");
        commandController.parseCommand("add task 222");
        commandController.parseCommand("add task 333");
        assertEquals(taskController.getDoneTasks().size(), 0);

        commandController.parseCommand("done 1");
        commandController.parseCommand("done 3");
        assertEquals(taskController.getDoneTasks().size(), 2);
    }

    // testing the ability to filter undone tasks
    @Test
    public void getUndoneTasksTest() {
        commandController.parseCommand("add task 111");
        commandController.parseCommand("add task 222");
        commandController.parseCommand("add task 333");
        assertEquals(taskController.getUndoneTasks().size(), 3);

        commandController.parseCommand("done 3");
        assertEquals(taskController.getUndoneTasks().size(), 2);

        commandController.parseCommand("undone 3");
        commandController.parseCommand("done 1");
        assertEquals(taskController.getUndoneTasks().size(), 2);
    }

    // testing the ability to filter undone tasks
    @Test
    public void getOverdueTasksTest() {
        commandController.parseCommand("add task 111 end yesterday");
        commandController.parseCommand("add task 222 end tomorrow");
        commandController.parseCommand("add task 333 end yesterday");
        assertEquals(taskController.getOverdueTasks().size(), 2);
    }

    @Override
    public CommandController getCommandController() {
        return commandController;
    }

    @Override
    public void showInfoDialog(String title, String message) {
        // do nothing
    }
}
