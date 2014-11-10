package app.model;
//@author A0116703N

import app.exceptions.InvalidInputException;
import app.services.LoggingService;

import org.json.JSONException;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.ListIterator;
import java.util.UUID;
import java.util.logging.Level;

/**
 * The main class in the Model. This class controls the model's logic,
 * and calls methods from other Model classes.
 */
public class ModelManager {
    // Error messages to be thrown to controller.
    public static final String LOAD_SUCCESS = "Successfully loaded file!";
    public static final String LOAD_FAILED = "Failed to load file.";
    public static final String PARSE_FAILED = "File data is corrupted.";
    public static final String SETTINGS_PARSE_FAILED = "Settings data is corrupted.";
    public static final String WRITE_SUCCESS = "Successfully written to file!";
    public static final String WRITE_FAILED = "Failed to write to file.";
    public static final String WRITE_SETTINGS_FAILED = "Failed to write to settings file.";
    public static final String LOAD_SETTINGS_FAILED = "Failed to load settings file.";
    public static final String START_DATE_AFTER_END_DATE = "Start date cannot be after end date.";
    
    // Logger messages
    private static final String ADD_MESSAGE = "Adding new task ";
    private static final String UPDATE_MESSAGE = "Updating task.";
    private static final String DELETE_MESSAGE = "Deleting task.";
    private static final String CLEAR_MESSAGE = "Clearing all tasks.";
    
    // Model class instances
    private TodoItemList todoList;
    private FileStorage dataStorage;
    
    private UUID latestModified;
    
    /**
     * Upon construction of a ModelManager, file I/O are instantly carried out. The data files are first loaded here.
     * 
     * @throws IOException with LOAD_SETTINGS_FAILED, SETTINGS_PARSE_FAILED, PARSE_FAILED and LOAD_FAILED messages
     */
    public ModelManager() throws IOException {
        // Don't load the any files yet
        this.dataStorage = new FileStorage();
        this.latestModified = null;
        
        // First load the settings
        try {
            dataStorage.loadSettings();
        } catch (JSONException e) {
            LoggingService.getLogger().log(Level.SEVERE, SETTINGS_PARSE_FAILED);
            throw new IOException(SETTINGS_PARSE_FAILED);
        } catch (IOException e) {
            LoggingService.getLogger().log(Level.SEVERE, LOAD_SETTINGS_FAILED);
            throw new IOException(LOAD_SETTINGS_FAILED);
        }
        
        // Then load the data from target directory
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
        
        // Finally, resort the task list based on end date
        TodoItemSorter.changeSortStyle(TodoItemSorter.DEFAULT_SORTING_STYLE);
        TodoItemSorter.resortTodoList(todoList.getTodoItems());
    }
    
    // CRUD
    /**
     * Adds a task into the program data and then writes it into the database file. 
     * 
     * @param newTaskName The name of the new task
     * @param newStartDate The start date of the new task
     * @param newEndDate The end date of the new task
     * @param newPriority The priority level of the new task. If this is not TodoItem.HIGH/MEDIUM/LOW, priority is set to MEDIUM.
     * @param newDoneStatus The done status of the new task. If this is null, done status is set to false.
     * @throws IOException with LOAD_FAILED, PARSE_FAILED, WRITE_FAILED
     */
    public void addTask(String newTaskName, Date newStartDate, Date newEndDate, String newPriority, Boolean newDoneStatus) throws IOException {
        // First, log the method call
        LoggingService.getLogger().log(Level.INFO, ADD_MESSAGE + newTaskName);
        
        // Then update the program data
        TodoItem newTodoItem = new TodoItem(newTaskName, newStartDate, newEndDate, newPriority, newDoneStatus);
        todoList.addTodoItem(newTodoItem);
        TodoItemSorter.resortTodoList(todoList.getTodoItems());
        
        // Then update file
        dataStorage.updateFile(todoList.getTodoItems());
        
        // Finally, update the last modified UUID
        latestModified = newTodoItem.getUUID();
    }
    
    /**
     * Updates a task within the program data and then updates database file.
     * 
     * @param itemID The UUID of the item to be updated
     * @param parameters An array specifying which attribute of the TodoItem to update
     * @param newTaskName (Can be null) The new task name of the item.
     * @param newStartDate (Can be null) The new start date of the item.
     * @param newEndDate (Can be null) The new end date of the item.
     * @param newPriority (Can be null) The new priority level of the item. If this is not TodoItem.LOW/MEDIUM/HIGH, an assertion error will occur.
     * @param newDoneStatus (Can be null) The new done status of the item.
     * @throws IOException with LOAD_FAILED, PARSE_FAILED, WRITE_FAILED
     * @throws InvalidInputException with START_DATE_AFTER_END_DATE  
     */
    public void updateTask(UUID itemID, Boolean[] parameters, String newTaskName, Date newStartDate, Date newEndDate, String newPriority, Boolean newDoneStatus) throws IOException, InvalidInputException {
        // First, log the method call
        LoggingService.getLogger().log(Level.INFO, UPDATE_MESSAGE);
        
        // parameters array should always be length 5
        assert parameters.length == 5;
        
        // Search for the task to update by its UUID
        TodoItem toChange = todoList.getByUUID(itemID);
        
        assert toChange != null; // UUID should always exist
        
        // Now we update the program data based on the parameters
        // Both start and end dates
        if (parameters[1] && parameters[2]) {
            if (newStartDate.getTime() > newEndDate.getTime()) throw new InvalidInputException(START_DATE_AFTER_END_DATE);
        }
        // Just start date
        if (parameters[1]) {
            if (toChange.getEndDate() != null) {
                if (newStartDate.getTime() > toChange.getEndDate().getTime()) {
                    throw new InvalidInputException(START_DATE_AFTER_END_DATE);
                }
            }
            toChange.setStartDate(newStartDate);
        }
        // Just end date
        if (parameters[2]) {
            if (toChange.getStartDate() != null) {
                if (toChange.getStartDate().getTime() > newEndDate.getTime()) {
                    throw new InvalidInputException(START_DATE_AFTER_END_DATE);
                }
            }
            toChange.setEndDate(newEndDate);
        }
        // If START_DATE_AFTER_END_DATE occurs, we don't want to update the TodoItem at all.
        // So we try to update start date and end date first before anything.
        
        // Task name
        if (parameters[0]) {
            toChange.setTaskName(newTaskName);
        }
        
        // Priority
        if (parameters[3]) {
            toChange.setPriority(newPriority);
        }
        // Done status
        if (parameters[4]) {
            toChange.setDoneStatus(newDoneStatus);
        }
        
        TodoItemSorter.resortTodoList(todoList.getTodoItems());

        // Then, update the file
        dataStorage.updateFile(todoList.getTodoItems());
        
        // Finally, update the last modified UUID
        latestModified = toChange.getUUID();
    }
    
    /**
     * Deletes a task within the program data and then updates the file.
     * 
     * @param itemID The UUID of the item to be deleted
     * @throws IOException with LOAD_FAILED, PARSE_FAILED, WRITE_FAILED
     * @return The deleted TodoItem
     */
    public TodoItem deleteTask(UUID itemID) throws IOException {
        // First, log the method call
        LoggingService.getLogger().log(Level.INFO, DELETE_MESSAGE);
        
        // Then update the data in memory
        TodoItem deletedItem = todoList.deleteByUUID(itemID);
        assert deletedItem != null; // UUID should always exist
        TodoItemSorter.resortTodoList(todoList.getTodoItems());

        // Then update the file
        dataStorage.updateFile(todoList.getTodoItems());
        
        return deletedItem;
    }
    
    /**
     * clearTasks
     * 
     * Clears all tasks in the program data and in the database file.
     * 
     * @throws IOException with LOAD_FAILED, PARSE_FAILED, WRITE_FAILED 
     */
    public void clearTasks() throws IOException {
        todoList.clearTodoItems();

        LoggingService.getLogger().log(Level.INFO, CLEAR_MESSAGE);
        
        dataStorage.updateFile(todoList.getTodoItems());
    }
    
    // Sorting
    /**
     * Changes the sortingStyle in the TodoItemSorter.
     * 
     * @param newSortingStyle The new sorting style index.
     */
    public void setSortingStyle(int newSortingStyle) {
        TodoItemSorter.changeSortStyle(newSortingStyle);
        TodoItemSorter.resortTodoList(todoList.getTodoItems());
    }

    /**
     * @return The current sorting style of the program.
     */
    public int getSortingStyle() {
        return TodoItemSorter.getSortStyle();
    }
    
    // Data exposure
    /**
     * Returns the data held by Model.
     * 
     * @return The ArrayList of TodoItems currently in the program memory.
     */
    public ArrayList<TodoItem> getTodoItemList() {
        return todoList.getTodoItems();
    }
    
    /**
     * Returns the data held by Model as a ListIterator
     * 
     * @return The iterator of the ArrayList of TodoItems currently in the program memory.
     */
    public ListIterator<TodoItem> getTodoItemIterator() {
        return getTodoItemList().listIterator();
    }
    
    // Settings
    /**
     * Changes the settings stored in the FileStorage, and then updates the settings.json file.
     * 
     * @param fileDirectory The new directory to switch to
     * @param randomColorsEnabled The new setting for random colors
     * @param notificationsEnabled The new setting for notifications
     * @throws IOException with WRITE_SETTINGS_FAILED
     */
    public void changeSettings(String fileDirectory, Boolean randomColorsEnabled, Boolean notificationsEnabled) throws IOException {
        todoList = new TodoItemList(dataStorage.changeSettings(fileDirectory, randomColorsEnabled, notificationsEnabled));
        
        // Reloading the data repopulates the item list with different items, so the UUID is not valid any more.
        latestModified = null;
    }
    
    /**
     * Returns the current file directory in use by the program.
     * 
     * @return The file directory in use
     */
    public String getFileDirectory() {
        return dataStorage.getFileDirectory();
    }
    
    /**
     * Returns the current setting of random task item colors.
     * 
     * @return The current random color setting
     */
    public Boolean areRandomColorsEnabled() {
        return dataStorage.areRandomColorsEnabled();
    }
    
    /**
     * Returns the current setting of notification display.
     * 
     * @return The current notification setting
     */
    public Boolean areNotificationsEnabled() {
        return dataStorage.areNotificationsEnabled();
    }
    
    // Last modified
    /**
     * @return The index within the program data of the last modified task item.
     */
    public int getLastModifiedIndex() {
        return todoList.searchIndexByUUID(latestModified);
    }
    
    /**
     * @return The last modified task item's UUID.
     */
    public UUID getLastModifiedUUID() {
        return latestModified;
    }
    
    // For use by Undo/Redo functionalities
    /**
     * Reloads a snapshot of the program data. This has the same effect on the
     * program data as switching to a new directory containing the given data.
     * 
     * @param newTodoItems The new set of TodoItems to be loaded to.
     * @throws IOException with LOAD_FAILED, PARSE_FAILED, WRITE_FAILED 
     */
    public void loadTodoItems(ArrayList<TodoItem> newTodoItems) throws IOException {
        // We attempt to write to the database file first. If there are
        // any I/O problems, we still have the old data intact.
        dataStorage.updateFile(newTodoItems);
        
        this.todoList = new TodoItemList(newTodoItems);
        this.latestModified = null;
        
        TodoItemSorter.resortTodoList(this.todoList.getTodoItems());
    }
    
    // Miscellaneous
    /**
     * @return The full filepath to the program.
     */
    public String getFullFileName() {
        return dataStorage.getFullFileName();
    }
    
    /**
     * @return The number of tasks held in program memory.
     */
    public int countTasks() {
        return todoList.countTodoItems();
    }
}
