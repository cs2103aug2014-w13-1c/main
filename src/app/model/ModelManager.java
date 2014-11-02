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
    
    public static final String LOAD_SUCCESS = "Successfully loaded file!";
    public static final String LOAD_FAILED = "Failed to load file.";
    public static final String PARSE_FAILED = "File data is corrupted.";
    public static final String SETTINGS_PARSE_FAILED = "Settings data is corrupted.";
    public static final String WRITE_SUCCESS = "Successfully written to file!";
    public static final String WRITE_FAILED = "Failed to write to file.";
    public static final String WRITE_SETTINGS_FAILED = "Failed to write to settings file.";
    public static final String LOAD_SETTINGS_FAILED = "Failed to load settings file.";
    
    private TodoItemList todoList;
    private FileStorage dataStorage;
    private UUID latestModified;
    
    /**
     * Constructor
     * 
     * @throws IOException with LOAD_SETTINGS_FAILED, SETTINGS_PARSE_FAILED, PARSE_FAILED and LOAD_FAILED messages
     */
    public ModelManager() throws IOException {
        System.out.println("showed up");
        
        this.dataStorage = new FileStorage();
        this.latestModified = null;
        
        try {
            dataStorage.loadSettings();
        } catch (JSONException e) {
            LoggingService.getLogger().log(Level.SEVERE, SETTINGS_PARSE_FAILED);
            throw new IOException(SETTINGS_PARSE_FAILED);
        } catch (IOException e) {
            LoggingService.getLogger().log(Level.SEVERE, LOAD_SETTINGS_FAILED);
            throw new IOException(LOAD_SETTINGS_FAILED);
        }
        
        try {
            this.todoList = new TodoItemList(dataStorage.loadFile());
        } catch (ParseException e) {
            LoggingService.getLogger().log(Level.SEVERE, PARSE_FAILED);
            throw new IOException(PARSE_FAILED);
        } catch (JSONException e) {
            LoggingService.getLogger().log(Level.SEVERE, PARSE_FAILED);
            throw new IOException(PARSE_FAILED);
        } catch (IOException e) {
            LoggingService.getLogger().log(Level.SEVERE, LOAD_FAILED);
            throw new IOException(LOAD_FAILED);
        }
        
        TodoItemSorter.sortingStyle = TodoItemSorter.DEFAULT_SORTING_STYLE;
        TodoItemSorter.resortTodoList(todoList);
    }
    
    /**
     * addTask
     * 
     * @throws IOException with LOAD_FAILED, JSON_FAILED, WRITE_FAILED
     */
    public void addTask(String newTaskName, Date newStartDate, Date newEndDate, String newPriority, Boolean newDoneStatus) throws IOException {
        
        TodoItem newTodoItem = new TodoItem(newTaskName, newStartDate, newEndDate, newPriority, newDoneStatus);
        
        todoList.addTodoItem(newTodoItem);
        
        TodoItemSorter.resortTodoList(todoList);
        LoggingService.getLogger().log(Level.INFO, "Adding new task " + newTaskName);
        
        dataStorage.updateFile(todoList.getTodoItems());
        
        latestModified = newTodoItem.getUUID();
    }
    
    /**
     * updateTask
     * 
     * @throws IOException with LOAD_FAILED, JSON_FAILED, WRITE_FAILED 
     */
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
    
    /**
     * deleteTask
     * 
     * @throws IOException with LOAD_FAILED, JSON_FAILED, WRITE_FAILED 
     */
    public TodoItem deleteTask(UUID itemID) throws IOException {
        TodoItem deletedItem = todoList.deleteByUUID(itemID);
        
        assert deletedItem != null;
        
        TodoItemSorter.resortTodoList(todoList);

        LoggingService.getLogger().log(Level.INFO, "Deleting task " + deletedItem.getTaskName());
        
        dataStorage.updateFile(todoList.getTodoItems());
        
        return deletedItem;
    }
    
    /**
     * clearTasks
     * 
     * @throws IOException with LOAD_FAILED, JSON_FAILED, WRITE_FAILED 
     */
    public void clearTasks() throws IOException {
        todoList.clearTodoItems();

        LoggingService.getLogger().log(Level.INFO, "Clearing all tasks.");
        
        dataStorage.updateFile(todoList.getTodoItems());
    }
    
    /**
     * changeSettings
     * 
     * @throws IOException with WRITE_SETTINGS_FAILED
     */
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
    
    /**
     * deleteTask
     * 
     * @throws IOException with LOAD_FAILED, JSON_FAILED, WRITE_FAILED 
     */
    public void loadTodoItems(ArrayList<TodoItem> newTodoItems) throws IOException {
        dataStorage.updateFile(newTodoItems);
        
        this.todoList = new TodoItemList(newTodoItems);
        this.latestModified = null;
        
        TodoItemSorter.resortTodoList(this.todoList);
    }
}
