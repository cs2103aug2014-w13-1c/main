//@author A0111987X
package tests;

import app.Main;
import app.controllers.CommandController;
import app.controllers.TaskController;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test class for TaskController, tests filtering of done/undone/overdue tasks.
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
        commandController.parseCommand("clear");
        commandController = null;
        taskController = null;
    }

    /** testing the ability to filter done tasks
     * unable to change done/undone flag because it has not been implemented
     * in commandcontroller yet
     *
     * boundary values:
     * 0 done tasks
     * 2 done tasks (has not been implemented yet)
     * infinitely many done tasks (but not feasible to implement)
     *
     * cannot have a negative number of tasks
     */
    @Test
    public void getDoneTasksTest() {
        // first we test that in an empty task list, the number of done tasks is 0
        assertEquals(taskController.getDoneTasks().size(), 0);

        commandController.parseCommand("add task 111");
        commandController.parseCommand("add task 222");
        commandController.parseCommand("add task 333");
        assertEquals(taskController.getDoneTasks().size(), 0);

        // testing for boundary case of 2 done tasks (has not been implemented yet)
//        commandController.parseCommand("done 1");
//        commandController.parseCommand("done 3");
//        assertEquals(taskController.getDoneTasks().size(), 2);
    }

    /** testing the ability to filter undone tasks
     * unable to change done/undone flag because it has not been implemented
     * in commandcontroller yet
     *
     * boundary values:
     * 0 undone tasks (has not been implemented yet)
     * 2 undone tasks
     * infinitely many undone tasks (but not feasible to implement)
     *
     * cannot have a negative number of tasks
     */
    @Test
    public void getUndoneTasksTest() {
        // first we test that in an empty task list, the number of undone tasks is 0
        assertEquals(taskController.getUndoneTasks().size(), 0);

        commandController.parseCommand("add task 111");
        commandController.parseCommand("add task 222");
        commandController.parseCommand("add task 333");
        assertEquals(taskController.getUndoneTasks().size(), 3);

        // testing for boundary case of 2 undone tasks (has not been implemented yet)
//        commandController.parseCommand("done 3");
//        assertEquals(taskController.getUndoneTasks().size(), 2);

        // testing for boundary case of 2 undone tasks (has not been implemented yet)
//        commandController.parseCommand("undone 3");
//        commandController.parseCommand("done 1");
//        assertEquals(taskController.getUndoneTasks().size(), 2);
    }

    /** testing the ability to filter overdue tasks
     *
     * boundary values:
     * 0 overdue tasks
     * 2 overdue tasks
     * infinitely many overdue tasks (but not feasible to implement)
     *
     * cannot have a negative number of tasks
     */
    @Test
    public void getOverdueTasksTest() {
        // first we test that in an empty task list, the number of overdue tasks is 0
        assertEquals(taskController.getOverdueTasks().size(), 0);

        // testing for boundary case of 2 overdue tasks
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
    public void showInfoNotification(String title, String message) {
        // do nothing
    }
}
