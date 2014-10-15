package app.controllers;

import app.Main;
import app.model.TodoItem;

import java.util.ArrayList;

/**
 * in charge of sorting and searching of tasks
 * implemented as a singleton
 *
 * Created by jolly on 15/10/14.
 */
public class TaskController {

    private static TaskController self;
    private Main main;

    private TaskController() {
        // nothing to do here
    }

    public static TaskController getTaskController() {
        if (self == null) {
            self = new TaskController();
        }
        return self;
    }

    public void setMainApp(Main main) {
        this.main = main;
    }

    public ArrayList<TodoItem> instantSearch(String query) {
        ArrayList<TodoItem> results = new ArrayList<TodoItem>();
        for (TodoItem todo : main.getCommandController().getTaskList()) {
            if (todo.getTaskName().toLowerCase().
                    contains(query.toLowerCase())) {
                results.add(todo);
            }
        }
        return results;
    }



}
