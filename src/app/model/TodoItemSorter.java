package app.model;

import java.util.Comparator;

public class TodoItemSorter {

    public static final int DEFAULT_SORTING_STYLE = 2; // By default, sort by end date. 
    public static int sortingStyle;
    
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
    

    public static int compareEndDates(TodoItem todoItem1, TodoItem todoItem2) {
        if ((todoItem1 == null || todoItem1.getEndDate() == null) && (todoItem2 == null || todoItem2.getEndDate() == null)) {
            return 0;
        }
        if (todoItem1 == null || todoItem1.getEndDate() == null) {
            return -1;
        }
        if (todoItem2 == null || todoItem2.getEndDate() == null) {
            return 1;
        }
        return ((Long)todoItem1.getEndDate().getTime()).compareTo(todoItem2.getEndDate().getTime());
    }
    
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
    
    private static int compareByParameter(TodoItem todoItem1, TodoItem todoItem2, int parameter) {
        boolean todoItem1Invalidity = isInvalid(todoItem1, parameter);
        boolean todoItem2Invalidity = isInvalid(todoItem2, parameter);
        
        if (todoItem1Invalidity && todoItem2Invalidity) {
            return 0;
        }
        
        // Push all 'null' to the end.
        // More intuitive UX.
        if (todoItem2Invalidity) {
            return -1;
        }
        if (todoItem1Invalidity) {
            return 1;
        }
        
        switch(parameter) {
            case 0: return todoItem1.getTaskName().compareTo(todoItem2.getTaskName());
            case 1: return ((Long) todoItem1.getStartDate().getTime()).compareTo(todoItem2.getStartDate().getTime());
            case 2: return ((Long) todoItem1.getEndDate().getTime()).compareTo(todoItem2.getEndDate().getTime());
            case 3: return todoItem1.getPriority().compareTo(todoItem2.getPriority());
            default: return 0;
        }
    }
    
    public static void resortTodoList(TodoItemList todoList) {
        todoList.sortTodoItems(todoItemComparators[sortingStyle]);
    }
}
