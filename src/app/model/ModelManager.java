package app.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.ListIterator;
import java.util.UUID;
import java.util.logging.Level;

import org.json.simple.parser.ParseException;

import app.helpers.LoggingService;

public class ModelManager {
    
    private TodoItemList todoList;
    private FileStorage dataStorage;
    
    public ModelManager() throws ParseException, IOException {
        this.dataStorage = new FileStorage();
        this.todoList = new TodoItemList();
        
        try {
            dataStorage.loadFile(todoList);
        } catch (ParseException e) {
            LoggingService.getLogger().log(Level.INFO, "Failed to parse JSON data.");
            throw new IOException("Failed to parse JSON data.");
        } catch (IOException e) {
            LoggingService.getLogger().log(Level.INFO, "Failed to load file at " + dataStorage.getFullFileName() + ".");
            throw new IOException("Failed to load file at " + dataStorage.getFullFileName() + ".");
        }
        
        TodoItemSorter.sortingStyle = TodoItemSorter.DEFAULT_SORTING_STYLE;
        TodoItemSorter.resortTodoList(todoList);
    }
    
    public void addTask(String newTaskName, Date newStartDate, Date newEndDate, String newPriority, Boolean newDoneStatus) throws IOException {
        TodoItem newTodoItem = new TodoItem(newTaskName, newStartDate, newEndDate, newPriority, newDoneStatus);
        
        todoList.addTodoItem(newTodoItem);
        
        TodoItemSorter.resortTodoList(todoList);
        LoggingService.getLogger().log(Level.INFO, "Adding new task " + newTaskName);
        
        dataStorage.updateFile(todoList.getTodoItems());
    }
    
    public void updateTask(UUID itemID, Boolean[] parameters, String newTaskName, Date newStartDate, Date newEndDate, String newPriority, Boolean newDoneStatus) throws IOException {
        
        // parameters array should be length 5
        assert parameters.length == 5;
        
        TodoItem toChange = todoList.getByUUID(itemID);
        
        // UUID not found
        assert toChange != null;
        
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

        LoggingService.getLogger().log(Level.INFO, "Updating task " + toChange.getTaskName());
        
        dataStorage.updateFile(todoList.getTodoItems());
    }
    
    public TodoItem deleteTask(UUID itemID) throws IOException {
        TodoItem deletedItem = todoList.deleteByUUID(itemID);
        
        TodoItemSorter.resortTodoList(todoList);

        LoggingService.getLogger().log(Level.INFO, "Deleting task " + deletedItem.getTaskName());
        
        dataStorage.updateFile(todoList.getTodoItems());
        
        return deletedItem;
    }
    
    public void changeFileDirectory(String fileDirectory) throws IOException {
        dataStorage.changeDirectory(fileDirectory, todoList);
        assert dataStorage.getFileDirectory().equals(fileDirectory);
    }
    
    public void setSortingStyle(int newSortingStyle) {
        TodoItemSorter.sortingStyle = newSortingStyle;
    }

    public ArrayList<TodoItem> getTodoItemList() {
        return todoList.getTodoItems();
    }
    
    public ListIterator<TodoItem> getTodoItemIterator() {
        return todoList.getTodoItems().listIterator();
    }
}
