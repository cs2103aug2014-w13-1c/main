//@author A0111987X
package tests;

import app.Main;
import app.controllers.CommandController;
import app.controllers.TaskController;
import app.viewmanagers.RootViewManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * Test class for TaskController, tests filtering of tasks.
 */
public class TaskControllerTest extends Main {

    private TaskController taskController;
    private CommandController commandController;

    /**
     * Setup needed to run tests, includes creating a new CommandController and TaskController.
     * Changes to test directory.
     */
    @Before
    public void setUp() {
        commandController = new CommandController();
        commandController.setMainApp(this);
        taskController = TaskController.getTaskController();
        taskController.setMainApp(this);
        commandController.setTaskController(taskController);
        commandController.parseCommand("saveto ./testDirectory");
        commandController.parseCommand("clear");
    }

    /**
     * Teardown for tests, clears the task list and sets commandController and taskController to null.
     * Changes back to default directory.
     */
    @After
    public void tearDown() {
        commandController.parseCommand("clear");
        commandController.parseCommand("saveto ./");
        commandController = null;
        taskController = null;
    }

    /**
     * Override for showInfoNotification that just prints the message.
     *
     * @param title The title of the notification.
     * @param message The message to be shown.
     */
    @Override
    public void showInfoNotification(String title, String message) {
        System.out.println(title + ": " + message);
    }

    /**
     * Override for showErrorNotification that just prints the message.
     *
     * @param title The title of the notification.
     * @param message The error message to be shown.
     */
    @Override
    public void showErrorNotification(String title, String message) {
        System.out.println(title + ": " + message);
    }

    /**
     * Override for getRootViewManager that returns a RootViewManagerStub instead of a RootViewManager.
     *
     * @return RootViewManagerStub
     */
    @Override
    public RootViewManager getRootViewManager() {
        return new RootViewManagerStub();
    }

    /**
     * Testing the ability to search within all task names.
     *
     * @throws Exception any exception
     */
    @Test
    public void testInstantSearch() throws Exception {
        // testing with an empty task list
        assertEquals(taskController.instantSearch("task").size(), 0);

        // testing search for tasks
        commandController.parseCommand("add task 111");
        commandController.parseCommand("add task 222");
        commandController.parseCommand("add task 333");
        assertEquals(taskController.instantSearch("task").size(), 3);
        assertEquals(taskController.instantSearch("1").size(), 1);
        assertEquals(taskController.instantSearch("222").size(), 1);
    }

    /**
     * Testing the ability to filter all tasks.
     *
     * @throws Exception any exception
     */
    @Test
    public void testGetAllTasks() throws Exception {
        // testing with an empty task list
        assertEquals(taskController.getAllTasks().size(), 0);

        // testing that added tasks are in all tasks
        commandController.parseCommand("add task 111");
        commandController.parseCommand("add task 222");
        commandController.parseCommand("add task 333");
        assertEquals(taskController.getAllTasks().size(), 3);

        // testing that deleting tasks results in less tasks
        commandController.parseCommand("delete 2");
        assertEquals(taskController.getAllTasks().size(), 2);

        // testing for more adding of tasks
        commandController.parseCommand("add task 444");
        commandController.parseCommand("add task 555");
        assertEquals(taskController.getAllTasks().size(), 4);
    }

    /**
     * Testing the ability to filter done tasks.
     *
     * @throws Exception any exception
     */
    @Test
    public void testGetDoneTasks() throws Exception {
        // first we test that in an empty task list, the number of done tasks is 0
        assertEquals(taskController.getDoneTasks().size(), 0);

        commandController.parseCommand("add task 111");
        commandController.parseCommand("add task 222");
        commandController.parseCommand("add task 333");
        assertEquals(taskController.getDoneTasks().size(), 0);

        // testing for 2 done tasks
        commandController.parseCommand("done 1");
        commandController.parseCommand("done 1");
        assertEquals(taskController.getDoneTasks().size(), 2);
    }

    /**
     * Testing the ability to filter undone tasks.
     *
     * @throws Exception any exception
     */
    @Test
    public void testGetUndoneTasks() throws Exception {
        // first we test that in an empty task list, the number of undone tasks is 0
        assertEquals(taskController.getUndoneTasks().size(), 0);

        commandController.parseCommand("add task 111");
        commandController.parseCommand("add task 222");
        commandController.parseCommand("add task 333");
        assertEquals(taskController.getUndoneTasks().size(), 3);

        // testing for 2 undone tasks
        commandController.parseCommand("done 3");
        commandController.parseCommand("done 1");
        commandController.parseCommand("display done");
        commandController.parseCommand("undone 2");
        assertEquals(taskController.getUndoneTasks().size(), 2);
    }

    /**
     * Testing the ability to filter overdue tasks.
     *
     * @throws Exception any exception
     */
    @Test
    public void testGetOverdueTasks() throws Exception {
        // first we test that in an empty task list, the number of overdue tasks is 0
        assertEquals(taskController.getOverdueTasks().size(), 0);

        // testing for 2 overdue tasks
        commandController.parseCommand("add task 111 end yesterday");
        commandController.parseCommand("add task 222 end tomorrow");
        commandController.parseCommand("add task 333 end yesterday");
        assertEquals(taskController.getOverdueTasks().size(), 2);
    }

    /**
     * Testing the ability to filter tasks starting on a certain day.
     *
     * @throws Exception any exception
     */
    @Test
    public void testGetTasksStartingOn() throws Exception {
        // first we test that in an empty task list, the number of tasks falling on today is 0
        assertEquals(taskController.getTasksStartingOn(new Date()).size(), 0);

        // testing that it will only find tasks that end on the day itself
        commandController.parseCommand("add task 111 start today");
        commandController.parseCommand("add task 222 start tomorrow");
        commandController.parseCommand("add task 333 start yesterday");
        assertEquals(taskController.getTasksStartingOn(new Date()).size(), 1);
    }

    /**
     * Testing the ability to filter tasks ending on a certain day.
     *
     * @throws Exception any exception
     */
    @Test
    public void testGetTasksEndingOn() throws Exception {
        // first we test that in an empty task list, the number of tasks falling on today is 0
        assertEquals(taskController.getTasksEndingOn(new Date()).size(), 0);

        // testing that it will only find tasks that end on the day itself
        commandController.parseCommand("add task 111 end today");
        commandController.parseCommand("add task 222 end tomorrow");
        commandController.parseCommand("add task 333 end yesterday");
        assertEquals(taskController.getTasksEndingOn(new Date()).size(), 1);
    }

    /**
     * Testing the ability to filter tasks that fall within the given date range.
     *
     * @throws Exception any exception
     */
    @Test
    public void testGetTasksWithinDateRange() throws Exception {
        // first we test that in an empty task list, the number of tasks falling on today is 0
        assertEquals(taskController.getTasksWithinDateRange(new Date(), new Date()).size(), 0);

        // testing that it will only find tasks within the date range
        commandController.parseCommand("add task 111 start today");
        commandController.parseCommand("add task 222 start tomorrow");
        commandController.parseCommand("add task 333 start yesterday");
        commandController.parseCommand("add task 444 end today");
        commandController.parseCommand("add task 555 end tomorrow");
        commandController.parseCommand("add task 666 end yesterday");
        commandController.parseCommand("add task 777 start last week end next week");
        assertEquals(taskController.getTasksWithinDateRange(new Date(), new Date()).size(), 3);
    }
}
