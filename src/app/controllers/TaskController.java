package app.controllers;

import app.Main;
import app.model.TodoItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

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

    public ArrayList<TodoItem> getAllTasks() {
        displayType = DisplayType.ALL;
        return main.getCommandController().getTaskList();
    }

    public ArrayList<TodoItem> getDoneTasks() {
        ArrayList<TodoItem> results = new ArrayList<TodoItem>();
        for (TodoItem todo : main.getCommandController().getTaskList()) {
            if (todo.isDone()) {
                results.add(todo);
            }
        }
        displayType = DisplayType.DONE;
        return results;
    }

    public ArrayList<TodoItem> getUndoneTasks() {
        ArrayList<TodoItem> results = new ArrayList<TodoItem>();
        for (TodoItem todo : main.getCommandController().getTaskList()) {
            if (!todo.isDone()) {
                results.add(todo);
            }
        }
        displayType = DisplayType.UNDONE;
        return results;
    }

    public ArrayList<TodoItem> getOverdueTasks() {
        ArrayList<TodoItem> results = new ArrayList<TodoItem>();
        for (TodoItem todo : main.getCommandController().getTaskList()) {
            if (!todo.isDone() && todo.isOverdue()) {
                results.add(todo);
            }
        }
        displayType = DisplayType.OVERDUE;
        return results;
    }

    public ArrayList<TodoItem> getTasksStartingOn(Date date) {
        ArrayList<TodoItem> results = new ArrayList<TodoItem>();
        for (TodoItem todo : main.getCommandController().getTaskList()) {
            if (todo.getStartDate() != null &&
                todo.getStartDate().getDay() == date.getDay() &&
                todo.getStartDate().getMonth() == date.getMonth() &&
                todo.getStartDate().getYear() == date.getYear()) {
                results.add(todo);
            }
        }
        displayType = DisplayType.SEARCH;
        return results;
    }

    public ArrayList<TodoItem> getTasksEndingOn(Date date) {
        ArrayList<TodoItem> results = new ArrayList<TodoItem>();
        for (TodoItem todo : main.getCommandController().getTaskList()) {
            if (todo.getEndDate() != null &&
                    todo.getEndDate().getDay() == date.getDay() &&
                    todo.getEndDate().getMonth() == date.getMonth() &&
                    todo.getEndDate().getYear() == date.getYear()) {
                results.add(todo);
            }
        }
        displayType = DisplayType.SEARCH;
        return results;
    }

    public ArrayList<TodoItem> getTasksWithinDateRange(Date start, Date end) {
        start.setHours(0);
        start.setMinutes(0);
        start.setSeconds(0);
        end.setHours(23);
        end.setMinutes(59);
        end.setSeconds(59);
        ArrayList<TodoItem> results = new ArrayList<TodoItem>();
        for (TodoItem todo : main.getCommandController().getTaskList()) {
            if (todo.getStartDate() != null && todo.getEndDate() != null && todo.getStartDate().before(start) && todo.getEndDate().after(end)) {
                results.add(todo);
            } else if (todo.getStartDate() != null && !todo.getStartDate().before(start) && !todo.getStartDate().after(end)) {
                results.add(todo);
            } else if (todo.getEndDate() != null && !todo.getEndDate().before(start) && !todo.getEndDate().after(end)) {
                results.add(todo);
            }
        }
        displayType = DisplayType.SEARCH;
        return results;
    }

    public void setSortingStyle(int newSortingStyle) {
        System.out.println(newSortingStyle);
        switch (newSortingStyle) {
            case 0 :
                sortingStyle = SortingStyle.TASKNAME_ENDDATE;
                break;
            case 1 :
                sortingStyle = SortingStyle.STARTDATE_PRIORITY;
                break;
            case 2 :
                // default sorting style
                sortingStyle = SortingStyle.ENDDATE_PRIORITY;
                break;
            case 3 :
                sortingStyle = SortingStyle.PRIORITY_ENDDATE;
                break;
            default :
                sortingStyle = SortingStyle.ENDDATE_PRIORITY;
                break;
        }
        System.out.println(sortingStyle);
        main.getCommandController().getModelManager().setSortingStyle(newSortingStyle);
        main.getCommandController().resetTaskList();
        main.getCommandController().updateView();
    }

    public UUID getLastModifiedUUID() {
        return main.getCommandController().getModelManager().getLastModifiedUUID();
    }

    public DisplayType getDisplayType() {
        return displayType;
    }

    public void setDisplayType(DisplayType type) {
        displayType = type;
    }
}
