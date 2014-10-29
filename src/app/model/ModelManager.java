package app.model;

import app.helpers.LoggingService;
import org.json.JSONException;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.ListIterator;
import java.util.UUID;
import java.util.logging.Level;

public class ModelManager {
    
    private TodoItemList todoList;
    private FileStorage dataStorage;
    private UUID latestModified;
    
    public ModelManager() throws IOException {
        this.dataStorage = new FileStorage();
        this.latestModified = null;
        
        try {
            dataStorage.loadSettings();
        } catch (JSONException e) {
            LoggingService.getLogger().log(Level.SEVERE, "Failed to parse settings data.");
            throw new IOException("Failed to parse settings JSON data.");
        }
        
        try {
            this.todoList = new TodoItemList(dataStorage.loadFile());
        } catch (ParseException e) {
            LoggingService.getLogger().log(Level.SEVERE, "Failed to parse date format at " + dataStorage.getFullFileName() + " at position " + e.getErrorOffset() + ".");
            throw new IOException("Failed to parse date format at " + dataStorage.getFullFileName() + ".");
        } catch (JSONException e) {
            LoggingService.getLogger().log(Level.SEVERE, "Failed to parse JSON data.");
            throw new IOException("Failed to parse JSON data.");
        } catch (IOException e) {
            LoggingService.getLogger().log(Level.SEVERE, "Failed to load file at " + dataStorage.getFullFileName() + ".");
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
        
        latestModified = newTodoItem.getUUID();
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
        
        latestModified = toChange.getUUID();
    }
    
    public TodoItem deleteTask(UUID itemID) throws IOException {
        TodoItem deletedItem = todoList.deleteByUUID(itemID);
        
        TodoItemSorter.resortTodoList(todoList);

        LoggingService.getLogger().log(Level.INFO, "Deleting task " + deletedItem.getTaskName());
        
        dataStorage.updateFile(todoList.getTodoItems());
        
        return deletedItem;
    }
    
    public void clearTasks() throws IOException {
        todoList.clearTodoItems();

        LoggingService.getLogger().log(Level.INFO, "Clearing all tasks.");
        
        dataStorage.updateFile(todoList.getTodoItems());
    }
    
    public void changeSettings(String fileDirectory, Boolean randomColorsEnabled, Boolean notificationsEnabled) throws IOException {
        todoList = new TodoItemList(dataStorage.changeSettings(fileDirectory, randomColorsEnabled, notificationsEnabled));
    }
    
    public void setSortingStyle(int newSortingStyle) {
        TodoItemSorter.sortingStyle = newSortingStyle;
        TodoItemSorter.resortTodoList(todoList);
    }

    public ArrayList<TodoItem> getTodoItemList() {
        return todoList.getTodoItems();
    }
    
    public ListIterator<TodoItem> getTodoItemIterator() {
        return getTodoItemList().listIterator();
    }
    
    public String getFileDirectory() {
        return dataStorage.getFileDirectory();
    }
    
    public Boolean areRandomColorsEnabled() {
        return dataStorage.areRandomColorsEnabled();
    }
    
    public Boolean areNotificationsEnabled() {
        return dataStorage.areNotificationsEnabled();
    }
    
    public String getFullFileName() {
        return dataStorage.getFullFileName();
    }
    
    public int countTasks() {
        return todoList.countTodoItems();
    }
    
    public int getLastModifiedIndex() {
        return todoList.searchIndexByUUID(latestModified);
    }
    
    public void loadTodoItems(ArrayList<TodoItem> newTodoItems) throws IOException {
        dataStorage.updateFile(newTodoItems);
        
        this.todoList = new TodoItemList(newTodoItems);
        this.latestModified = null;
        
        TodoItemSorter.resortTodoList(this.todoList);
    }
}
