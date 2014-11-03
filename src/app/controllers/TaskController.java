package app.controllers;

import app.Main;
import app.model.TodoItem;

import java.util.ArrayList;
import java.util.Date;

/**
 * in charge of sorting and searching of tasks
 * implemented as a singleton
 *
 * Created by jolly on 15/10/14.
 */
public class TaskController {

    private static TaskController self;
    private static Main main;
    private static DisplayType displayType;
    private static SortingStyle sortingStyle;

    public static enum DisplayType {
        ALL, DONE, UNDONE, OVERDUE, SEARCH
    }

    public static enum SortingStyle {
        TASKNAME_ENDDATE, STARTDATE_PRIORITY, ENDDATE_PRIORITY, PRIORITY_ENDDATE
    }

    private TaskController() {
        // nothing to do here
    }

    public static TaskController getTaskController() {
        if (self == null) {
            self = new TaskController();
            displayType = DisplayType.ALL;
            sortingStyle = SortingStyle.ENDDATE_PRIORITY;
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

    protected ArrayList<TodoItem> getDoneTasks() {
        ArrayList<TodoItem> results = new ArrayList<TodoItem>();
        for (TodoItem todo : main.getCommandController().getTaskList()) {
            if (todo.isDone()) {
                results.add(todo);
            }
        }
        displayType = DisplayType.DONE;
        return results;
    }

    protected ArrayList<TodoItem> getUndoneTasks() {
        ArrayList<TodoItem> results = new ArrayList<TodoItem>();
        for (TodoItem todo : main.getCommandController().getTaskList()) {
            if (!todo.isDone()) {
                results.add(todo);
            }
        }
        displayType = DisplayType.UNDONE;
        return results;
    }

    protected ArrayList<TodoItem> getOverdueTasks() {
        ArrayList<TodoItem> results = new ArrayList<TodoItem>();
        for (TodoItem todo : main.getCommandController().getTaskList()) {
            if (todo.isOverdue()) {
                results.add(todo);
            }
        }
        displayType = DisplayType.OVERDUE;
        return results;
    }

    protected ArrayList<TodoItem> getTasksStartingFrom(Date date) {
        ArrayList<TodoItem> results = new ArrayList<TodoItem>();
        for (TodoItem todo : main.getCommandController().getTaskList()) {
            if (!todo.getStartDate().before(date)) {
                results.add(todo);
            }
        }
        displayType = DisplayType.SEARCH;
        return results;
    }

    protected ArrayList<TodoItem> getTasksEndingBy(Date date) {
        ArrayList<TodoItem> results = new ArrayList<TodoItem>();
        for (TodoItem todo : main.getCommandController().getTaskList()) {
            if (!todo.getEndDate().after(date)) {
                results.add(todo);
            }
        }
        displayType = DisplayType.SEARCH;
        return results;
    }

    protected ArrayList<TodoItem> getTasksWithinDateRange(Date start, Date end) {
        ArrayList<TodoItem> results = new ArrayList<TodoItem>();
        for (TodoItem todo : main.getCommandController().getTaskList()) {
            if (!todo.getStartDate().before(start) && !todo.getEndDate().after(end)) {
                results.add(todo);
            }
        }
        displayType = DisplayType.SEARCH;
        return results;
    }

    protected void setSortingStyle(int newSortingStyle) {
        switch (newSortingStyle) {
            case 0 :
                sortingStyle = SortingStyle.TASKNAME_ENDDATE;
            case 1 :
                sortingStyle = SortingStyle.STARTDATE_PRIORITY;
            case 2 :
                // default sorting style
                sortingStyle = SortingStyle.ENDDATE_PRIORITY;
            case 3 :
                sortingStyle = SortingStyle.PRIORITY_ENDDATE;
            default :
                sortingStyle = SortingStyle.ENDDATE_PRIORITY;
        }
        main.getCommandController().getModelManager().setSortingStyle(newSortingStyle);
        main.getCommandController().updateView();
    }

    public int getLastModifiedIndex() {
        return main.getCommandController().getModelManager().getLastModifiedIndex();
    }

    public DisplayType getDisplayType() {
        return displayType;
    }

    public void setDisplayType(DisplayType type) {
        displayType = type;
    }

    public SortingStyle getSortingStyle() {
        return sortingStyle;
    }

}
