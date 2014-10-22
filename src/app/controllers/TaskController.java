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

    private TaskController() {
        // nothing to do here
    }

    public static TaskController getTaskController() {
        if (self == null) {
            self = new TaskController();
        }
        return self;
    }

    public ArrayList<TodoItem> instantSearch(String query) {
        ArrayList<TodoItem> results = new ArrayList<TodoItem>();
        for (TodoItem todo : CommandController.getTaskList()) {
            if (todo.getTaskName().toLowerCase().
                    contains(query.toLowerCase())) {
                results.add(todo);
            }
        }
        return results;
    }

    public ArrayList<TodoItem> getDoneTasks() {
        ArrayList<TodoItem> results = new ArrayList<TodoItem>();
        for (TodoItem todo : CommandController.getTaskList()) {
            if (todo.isDone()) {
                results.add(todo);
            }
        }
        return results;
    }

    public ArrayList<TodoItem> getUndoneTasks() {
        ArrayList<TodoItem> results = new ArrayList<TodoItem>();
        for (TodoItem todo : CommandController.getTaskList()) {
            if (!todo.isDone()) {
                results.add(todo);
            }
        }
        return results;
    }

    public ArrayList<TodoItem> getOverdueTasks() {
        ArrayList<TodoItem> results = new ArrayList<TodoItem>();
        for (TodoItem todo : CommandController.getTaskList()) {
            if (todo.isOverdue()) {
                results.add(todo);
            }
        }
        return results;
    }

    public int getLastModifiedIndex() {
        return CommandController.getModelManager().getLastModifiedIndex();
    }

}
