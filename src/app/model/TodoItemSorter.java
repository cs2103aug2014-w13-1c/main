package app.model;
//@author A0116703N

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * This class holds the comparators for sorting TodoItems. Note that while this
 * class *can* be instantiated, it is not meant to be instantiated. All of its 
 * methods and fields are static.
 *
 */
public class TodoItemSorter {

    public static final int DEFAULT_SORTING_STYLE = 2; // By default, sort by end date. 
    private static int sortingStyle;
    
    // Comparators. Implement your double-criterion sorting logic here.
    public static final Comparator[] todoItemComparators = {
            new Comparator<TodoItem>() { // TaskName
                public int compare(TodoItem todoItem1, TodoItem todoItem2) {
                    int comparisonResult = compareByParameter(todoItem1, todoItem2, 0);
                    if (comparisonResult == 0) {
                        return compareByParameter(todoItem1, todoItem2, 2); // Alphabetically, then end date
                    } else {
                        return comparisonResult;
                    }
                }
            },
            new Comparator<TodoItem>() { // StartDate
                public int compare(TodoItem todoItem1, TodoItem todoItem2) {
                    int comparisonResult = compareByParameter(todoItem1, todoItem2, 1);
                    if (comparisonResult == 0) {
                        return compareByParameter(todoItem1, todoItem2, 3); // Start date, then priority
                    } else {
                        return comparisonResult;
                    }
                }
            },
            new Comparator<TodoItem>() { // EndDate
                public int compare(TodoItem todoItem1, TodoItem todoItem2) {
                    int comparisonResult = compareByParameter(todoItem1, todoItem2, 2);
                    if (comparisonResult == 0) {
                        return compareByParameter(todoItem1, todoItem2, 3); // End date, then priority
                    } else {
                        return comparisonResult;
                    }
                }
            },
            new Comparator<TodoItem>() { // Priority
                public int compare(TodoItem todoItem1, TodoItem todoItem2) {
                    int comparisonResult = compareByParameter(todoItem1, todoItem2, 3);
                    if (comparisonResult == 0) {
                        return compareByParameter(todoItem1, todoItem2, 2); // Priority, then end date
                    } else {
                        return comparisonResult;
                    }
                }
            },
    };
    
    /**
     * Checks if the TodoItem in question has the relevant data or if it just holds
     * a null in the field in question.
     * 
     * @param todoItem The TodoItem to check for data existence.
     * @param parameter The attribute to check. 0 for task name, 1 for start date, 2 for end date, 3 for priority. 
     * @return A Boolean value describing whether the attribute is non-null in the TodoItem.
     */
    private static boolean isInvalid(TodoItem todoItem, int parameter) {
        if (todoItem == null) {
            return true;
        }
        
        switch(parameter) {
            case 0: return todoItem.getTaskName() == null;
            case 1: return todoItem.getStartDate() == null;
            case 2: return todoItem.getEndDate() == null;
            case 3: return todoItem.getPriority() == null;
            default: return false;
        }
    }
    
    /**
     * Comparison method to compare two TodoItems by the specified attribute. The
     * "equals" case is then handled by the double-criterion logic in the Comparators
     * array above.
     * The return value can be negative (for todoItem1 < todoItem2), positive (for todoItem1 > todoItem2)
     * or equal (for todoItem1 == todoItem2). 
     * 
     * @param todoItem1 The first TodoItem to be compared
     * @param todoItem2 The second TodoItem to be compared
     * @param parameter The attribute to compare the two TodoItems by
     * @return An integer value with the same meaning as the result of a compareTo call.
     */
    private static int compareByParameter(TodoItem todoItem1, TodoItem todoItem2, int parameter) {
        boolean todoItem1Invalidity = isInvalid(todoItem1, parameter);
        boolean todoItem2Invalidity = isInvalid(todoItem2, parameter);
        
        // If both invalid, they're equal.
        if (todoItem1Invalidity && todoItem2Invalidity) {
            return 0;
        }
        
        // Push all 'null' to the back. This makes all floating tasks appear at the bottom.
        if (todoItem2Invalidity) {
            return -1;
        }
        if (todoItem1Invalidity) {
            return 1;
        }
        
        switch(parameter) {
            case 0: return todoItem1.getTaskName().toLowerCase().compareTo(todoItem2.getTaskName().toLowerCase());
            case 1: return ((Long) todoItem1.getStartDate().getTime()).compareTo(todoItem2.getStartDate().getTime());
            case 2: return ((Long) todoItem1.getEndDate().getTime()).compareTo(todoItem2.getEndDate().getTime());
            case 3: return todoItem1.getPriority().compareTo(todoItem2.getPriority());
            default: return 0;
        }
    }
    
    /**
     * Sorts an ArrayList of TodoItems by the current sorting style.
     * 
     * @param todoList The ArrayList of TodoItems to be sorted.
     */
    public static void resortTodoList(ArrayList<TodoItem> todoList) {
        Collections.sort(todoList, todoItemComparators[sortingStyle]);
    }
    
    /**
     * Changes the current sorting style in use.
     * 
     * @param newSortingStyle The new sorting style.
     */
    public static void changeSortStyle(int newSortingStyle) {
        sortingStyle = newSortingStyle;
    }
    
    /**
     * @return The current sorting style in use.
     */
    public static int getSortStyle() {
        return sortingStyle;
    }
}
