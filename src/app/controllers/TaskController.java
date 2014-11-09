//@author A0111987X
package app.controllers;

import app.Main;
import app.model.TodoItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

/**
 * In charge of sorting and searching of tasks.
 * Actual sorting of tasks is done in Model Manager, this just acts as an interface to that.
 * Implemented as a singleton.
 */
public class TaskController {

    private static TaskController self;
    private static Main main;
    private static DisplayType displayType;
    private static SortingStyle sortingStyle;

    /**
     * Enum containing the various display types.
     */
    public static enum DisplayType {
        ALL, DONE, UNDONE, OVERDUE, SEARCH
    }

    /**
     * Enum containing the various sorting styles.
     */
    public static enum SortingStyle {
        TASKNAME_ENDDATE, STARTDATE_PRIORITY, ENDDATE_PRIORITY, PRIORITY_ENDDATE
    }

    /**
     * Constructor for TaskController, sets default display type and sorting style.
     */
    private TaskController() {
        displayType = DisplayType.ALL;
        sortingStyle = SortingStyle.ENDDATE_PRIORITY;
    }

    /**
     * Getter for TaskController.
     *
     * @return TaskController (singleton pattern)
     */
    public static TaskController getTaskController() {
        if (self == null) {
            self = new TaskController();
        }
        return self;
    }

    /**
     * Setter for main.
     *
     * @param main
     */
    public void setMainApp(Main main) {
        this.main = main;
    }

    /**
     * Returns all results whose task name contains the search query.
     *
     * @param query search query
     * @return      tasks whose task name contains the search query
     */
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

    /**
     * Returns all tasks.
     *
     * @return all tasks
     */
    public ArrayList<TodoItem> getAllTasks() {
        displayType = DisplayType.ALL;
        return main.getCommandController().getTaskList();
    }

    /**
     * Returns all done tasks.
     *
     * @return done tasks
     */
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

    /**
     * Returns all undone tasks.
     * Implemented as the default view.
     *
     * @return undone tasks
     */
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

    /**
     * Returns all overdue tasks.
     *
     * @return overdue tasks
     */
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

    /**
     * Return tasks that start on the specified day.
     *
     * @param date  start date
     * @return      tasks that start on the specified day
     */
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

    /**
     * Returns tasks that end on the specified day.
     *
     * @param date  end date
     * @return      tasks that end on the specified day
     */
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

    /**
     * Returns tasks that fall within the given date range.
     * Includes tasks whose start or end dates fall within the date range.
     * Also includes tasks which start before and end after the date range.
     *
     * @param start start date
     * @param end   end date
     * @return      tasks within that date range
     */
    public ArrayList<TodoItem> getTasksWithinDateRange(Date start, Date end) {
        // setting date range to be the full days for both the start and end dates
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

    /**
     * Setter for sortingStyle.
     * Sets sorting style for model manager and calls for a re-sort of the task list.
     * Finally calls for an view update of the task list to reflect the new sorting style.
     *
     * @param newSortingStyle sortingStyle
     */
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

    /**
     * Acts as the interface between the view and the model manager for getting the last modified task's UUID.
     *
     * @return last modified UUID
     */
    public UUID getLastModifiedUUID() {
        return main.getCommandController().getModelManager().getLastModifiedUUID();
    }

    /**
     * Getter for displayType.
     *
     * @return displayType
     */
    public DisplayType getDisplayType() {
        return displayType;
    }

    /**
     * Setter for displayType.
     *
     * @param type displayType
     */
    public void setDisplayType(DisplayType type) {
        displayType = type;
    }
}
