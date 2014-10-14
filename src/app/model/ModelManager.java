package app.model;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import org.json.simple.parser.ParseException;

public class ModelManager {
    
    private TodoItemList todoList;
    private FileStorage dataStorage;
    
    public ModelManager() throws ParseException, IOException {
        this.dataStorage = new FileStorage();
        this.todoList = new TodoItemList();
        
        try {
            dataStorage.loadFile(todoList);
        } catch (ParseException e) {
            throw new IOException("Failed to parse JSON data.");
        } catch (IOException e) {
            throw new IOException("Failed to load file at " + dataStorage.getFullFileName() + ".");
        }
        
        TodoItemSorter.sortingStyle = TodoItemSorter.DEFAULT_SORTING_STYLE;
        TodoItemSorter.resortTodoList(todoList);
    }
    
    public TodoItemList getSortedTodoList() {
        return todoList;
    }
    
    public void addTask(String newTaskName, Date newStartDate, Date newEndDate) throws IOException {
        TodoItem newTodoItem = new TodoItem(newTaskName, newStartDate, newEndDate);
        
        todoList.addTodoItem(newTodoItem);
        
        TodoItemSorter.resortTodoList(todoList);
        
        dataStorage.updateFile(todoList.getTodoItems());
    }
    
    public void updateTask(UUID itemID, Boolean[] parameters, String newTaskName, Date newStartDate, Date newEndDate, String newPriority, Boolean newDoneStatus) throws IOException {
        
        // Incorrect parameters
        if (parameters.length != 5) {
            return;
        }
        
        TodoItem toChange = todoList.getByUUID(itemID);
        
        // UUID not found
        if (toChange == null) return;
        
        // Task name
        if (parameters[0]) {
            toChange.setTaskName(newTaskName);
        }
        
        // Start date
        if (parameters[1]) {
            toChange.setStartDate(newStartDate);
        }
        
        // End date
        if (parameters[2]) {
            toChange.setEndDate(newEndDate);
        }
        
        // Priority
        if (parameters[3]) {
            toChange.setPriority(newPriority);
        }
        
        // Done status
        if (parameters[4]) {
            toChange.setDoneStatus(newDoneStatus);
        }
        
        TodoItemSorter.resortTodoList(todoList);
        dataStorage.updateFile(todoList.getTodoItems());
    }
    
    public TodoItem deleteTask(UUID itemID) throws IOException {
        TodoItem deletedItem = todoList.deleteByUUID(itemID);
        
        TodoItemSorter.resortTodoList(todoList);
        dataStorage.updateFile(todoList.getTodoItems());
        
        return deletedItem;
    }
    
    public void changeFileDirectory(String fileDirectory) throws IOException {
        dataStorage.changeDirectory(fileDirectory, todoList);
    }
    
    public void setSortingStyle(int newSortingStyle) {
        TodoItemSorter.sortingStyle = newSortingStyle;
    }
}
