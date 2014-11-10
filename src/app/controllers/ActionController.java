package app.controllers;

import app.exceptions.InvalidInputException;
import app.helpers.CommandObject;
import app.services.LoggingService;
import app.model.ModelManager;
import app.model.TodoItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.logging.Level;

/**
 * This class takes in a command object, which specifies the details of the action to be carried out,
 * and then interacts with the model through ModelManager to carry out the action. The data structure can then
 * be extracted from this class by the CommandController.
 */

public class ActionController {
    // Errors
    private final String ERROR_FILE_EMPTY = "Task list is empty.\n";
    private final String ERROR_INVALID_INDEX = "Error. Index is not found.\n";
    private final String ERROR_WRONG_COMMAND_FORMAT = "Error. Incorrect %1$s command format. Click help icon or type help for info.\n";
    private final String ERROR_SEARCH_TERM_NOT_FOUND = "Search term not found.\n";

    // Messages
    private final String MESSAGE_ADD_COMPLETE = "Added: \"%1$s\"\n";
    private final String MESSAGE_CHANGE_DONE_STATUS_COMPLETE = "Changed done status: \"%1$s\"\n";
    private final String MESSAGE_CLEAR_COMPLETE = "Todo cleared\n";
    private final String MESSAGE_DELETE_COMPLETE = "Deleted: \"%1$s\"\n";
    private final String MESSAGE_SEARCH_COMPLETE = "Serch complete. \n%1$s";
    private final String MESSAGE_UPDATE_COMPLETE = "Updated: \"%1$s\"\n";
    
    private final String MESSAGE_CHANGE_SAVE_FILE_LOCATION = "Save file location is changed\n";
    private final String MESSAGE_DISPLAY = "Displaying tasks\n";
    private final String MESSAGE_OPEN_HELP = "Showing help\n";
    private final String MESSAGE_OPEN_SETTINGS = "Showing settings\n";
    private final String MESSAGE_REDO = "Redo\n";
    private final String MESSAGE_UNDO = "Undo\n";
    
    // Class variables
    private static CommandController commandController;
    private static ModelManager modelManager;
    private static TaskController taskController;
    private static UndoController undoController;
    private static ArrayList<TodoItem> returnList;

    // Individual command methods
    /**
     * This method is used by CommandController to add a new TodoItem.
     * It takes a CommandObject and check whether the CommandObject is valid.
     * It calls UndoController to save the state for undo command.
     * It then calls ModelManager to add a TodoItem based on the CommandObject.
     * 
     * @param commandObject
     * @return A feedback string to notify whether the method has carried out successfully.
     */
    public String add(CommandObject commandObject){
        // To check if CommandString is empty
        if (commandObject.getCommandString().isEmpty()) {
            return CommandController.notifyWithError(String.format(ERROR_WRONG_COMMAND_FORMAT, "add"));
        }
        try {
            undoController.saveUndo(modelManager.getTodoItemList());
            undoController.clearRedo();
            modelManager.addTask(commandObject.getCommandString(), commandObject.getStartDate(), commandObject.getEndDate(), commandObject.getPriority(), null);
        } catch (IOException e) {
            CommandController.notifyWithError("Failed to write to file.");
            LoggingService.getLogger().log(Level.SEVERE, "IOException: " + e.getMessage());
        } catch (NullPointerException e) {
            LoggingService.getLogger().log(Level.SEVERE, "NullPointerException" + e.getMessage());
        }
        return CommandController.notifyWithInfo(String.format(MESSAGE_ADD_COMPLETE, commandObject.getInputString()));
    }

    /**
     * This method is used by CommandController to change the display option of the current view.
     * It takes a CommandObject and check whether the CommandObject is valid.
     * It calls TaskController to select the display option depending on the CommandObject.
     * 
     * @param commandObject
     * @return A feedback string to notify whether the method has carried out successfully.
     */
    public String display(CommandObject commandObject) {
        if (!commandObject.getCommandString().isEmpty()) {
            if (commandObject.getCommandString().equals("all")) {
                returnList = taskController.getAllTasks();
            } else if (commandObject.getCommandString().equals("done")) {
                returnList = taskController.getDoneTasks();
            } else if (commandObject.getCommandString().equals("overdue")) {
                returnList = taskController.getOverdueTasks();
            } else if (commandObject.getCommandString().equals("overdue")) {
                returnList = taskController.getUndoneTasks();
            } else {    // If CommandString is something else, it is an invalid command
                return CommandController.notifyWithError(String.format(ERROR_WRONG_COMMAND_FORMAT, "display"));
            }
        } else {
            returnList = taskController.getUndoneTasks();
        }
        return MESSAGE_DISPLAY;
    }

    /**
     * This method is used by CommandController to clear all TodoItem(s).
     * It takes a CommandObject and check whether the CommandObject is valid.
     * It calls UndoController to save the state for undo command.
     * It then calls ModelManager to clear the TodoItem(s).
     * 
     * @param commandObject
     * @return A feedback string to notify whether the method has carried out successfully.
     */
    public String clear(CommandObject commandObject) {
        // To check whether CommandString is empty
        if (!commandObject.getCommandString().isEmpty()) {
            return CommandController.notifyWithError(String.format(ERROR_WRONG_COMMAND_FORMAT, "clear"));
        }
        try {
            undoController.saveUndo(modelManager.getTodoItemList());
            undoController.clearRedo();
            modelManager.clearTasks();
        } catch (IOException e) {
            CommandController.notifyWithError("Failed to write to file.");
            LoggingService.getLogger().log(Level.SEVERE, "IOException: " + e.getMessage());
        } catch (NullPointerException e) {
            LoggingService.getLogger().log(Level.SEVERE, "NullPointerException" + e.getMessage());
        }
        return MESSAGE_CLEAR_COMPLETE;
    }
    
    /**
     * This method is used by CommandController to delete a TodoItem.
     * It takes a CommandObject and check whether the CommandObject is valid. It also takes the current list of
     * TodoItem to be able to delete the TodoItem from the current list.
     * It calls UndoController to save the state for undo command.
     * It then calls ModelManager to delete the TodoItem by a given index (from current list) retrieved from CommandObject.
     * 
     * @param commandObject
     * @param currentList
     * @return A feedback string to notify whether the method has carried out successfully.
     */
    public String delete(CommandObject commandObject, ArrayList<TodoItem> currentList) {
        // To check whether CommandString is empty
        if (commandObject.getCommandString().isEmpty()) {
            return CommandController.notifyWithError(String.format(ERROR_WRONG_COMMAND_FORMAT, "delete"));
        }
        // To check that the index input is an integer
        if(!isInt(commandObject.getCommandString())) {
            return CommandController.notifyWithError(String.format(ERROR_WRONG_COMMAND_FORMAT, "delete"));
        }
        int index = Integer.parseInt(commandObject.getCommandString()) - 1;
        // To check that the index is valid
        if (index < 0 || index >= currentList.size()) {
            return CommandController.notifyWithError(ERROR_INVALID_INDEX);
        }
        try {
            String toBeDeleted = currentList.get(index).getTaskName();
            try {
                undoController.saveUndo(modelManager.getTodoItemList());
                undoController.clearRedo();
                modelManager.deleteTask(currentList.get(index).getUUID());
            } catch (IOException e) {
                CommandController.notifyWithError("Failed to write to file.");
                LoggingService.getLogger().log(Level.SEVERE, "IOException: " + e.getMessage());
            } catch (NullPointerException e) {
                LoggingService.getLogger().log(Level.SEVERE, "NullPointerException" + e.getMessage());
            }

            return CommandController.notifyWithInfo(String.format(MESSAGE_DELETE_COMPLETE, toBeDeleted));
        } catch (IndexOutOfBoundsException e) {
            return CommandController.notifyWithError("Index out of bounds.");
        }
    }

    /**
     * A simple method to test whether a given string is a number.
     * 
     * @param number
     * @return boolean true or false
     */
    private boolean isInt(String number) {
        try {
            Integer.parseInt(number);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    /**
     * This method is used by CommandController to sort the TodoItem(s).
     * It takes a CommandObject and check whether the CommandObject is valid.
     * It calls TaskController to sort the TodoItem(s) based on the option given by CommandObject.
     * 
     * @param commandObject
     * @return A feedback string to notify whether the method has carried out successfully.
     */
    public String sort(CommandObject commandObject) {
        // To check whether CommandString is empty
        if (commandObject.getCommandString().isEmpty()) {
            return CommandController.notifyWithError(String.format(ERROR_WRONG_COMMAND_FORMAT, "sort"));
        }
        if (commandObject.getCommandString().equalsIgnoreCase("name")) {
            taskController.setSortingStyle(0);
            return "Sorting by task name\n";
        } else if (commandObject.getCommandString().equalsIgnoreCase("start")) {
            taskController.setSortingStyle(1);
            return "Sorting by start date\n";
        } else if (commandObject.getCommandString().equalsIgnoreCase("end")) {
            taskController.setSortingStyle(2);
            return "Sorting by end date\n";
        } else if (commandObject.getCommandString().equalsIgnoreCase("priority")) {
            taskController.setSortingStyle(3);
            return "Sorting by priority\n";
        } else {    // If CommandString is something else, it is an invalid command
            return CommandController.notifyWithError(ERROR_WRONG_COMMAND_FORMAT);
        }
        
    }
    
    /**
     * This method is used by CommandController to search for a query.
     * It takes a CommandObject and check whether the CommandObject is valid.
     * Calls taskController to search for query, then updates resultList (which will be used by CommandController
     * to show to view).
     * 
     * @param commandObject
     * @return A feedback string to notify whether the method has carried out successfully.
     */
    public String search(CommandObject commandObject) {
        // To check whether CommandString is empty and there is no indication of searching by dates
        if (commandObject.getCommandString().isEmpty() && !commandObject.hasStartDateKeyword() && !commandObject.hasEndDateKeyword()) {
            return CommandController.notifyWithError(String.format(ERROR_WRONG_COMMAND_FORMAT, "search"));
        }
        try {
            ArrayList<TodoItem> todoList = modelManager.getTodoItemList();
            if (todoList.isEmpty()) {
                return CommandController.notifyWithError(String.format(ERROR_FILE_EMPTY));
            }
        } catch (NullPointerException e) {
            LoggingService.getLogger().log(Level.SEVERE, "NullPointerException" + e.getMessage());
        }
        ArrayList<TodoItem> results = new ArrayList<TodoItem>();
        if (commandObject.hasStartDateKeyword()) {
            if (commandObject.hasEndDateKeyword()) {
                results = taskController.getTasksWithinDateRange(commandObject.getStartDate(), commandObject.getEndDate());
            } else {
                results = taskController.getTasksStartingOn(commandObject.getStartDate());
            }
        } else if (commandObject.hasEndDateKeyword()) {
            results = taskController.getTasksEndingOn(commandObject.getEndDate());
        } else {
            results = taskController.instantSearch(commandObject.getCommandString());
        }
        returnList = results;
        if (results.isEmpty()) {
            // Error handling for when I/O with database failed.
            if (modelManager != null) {
                return CommandController.notifyWithError(ERROR_SEARCH_TERM_NOT_FOUND);
            } else {
                return null;
            }
        } else {
//            main.getPrimaryStage().setTitle("Search results for: \"" + commandObject.getCommandString() + "\"");
            taskController.setDisplayType(TaskController.DisplayType.SEARCH);
            return String.format(MESSAGE_SEARCH_COMPLETE, "updating task list view with results\n");
        }
    }

    /**
     * This method is used by CommandController to update a TodoItem.
     * It takes a CommandObject and check whether the CommandObject is valid. It also takes the current list of
     * TodoItem to be able to update the TodoItem from the current list.
     * It calls UndoController to save the state for undo command.
     * It calls ModelManager to update the TodoItem by a given index (from current list) and gets new data based
     * on the CommandObject.
     * 
     * @param commandObject
     * @param currentList The current data to be passed to display.
     * @return A feedback string to notify whether the method has carried out successfully.
     */
    public String update(CommandObject commandObject, ArrayList<TodoItem> currentList) {
        // To check whether CommandString is empty
        if (commandObject.getCommandString().isEmpty()) {
            return CommandController.notifyWithError(String.format(ERROR_WRONG_COMMAND_FORMAT, "update"));
        }
        Boolean[] parameters = {false, false, false, false, false};
        
        StringTokenizer st = new StringTokenizer(commandObject.getCommandString());
        String check = st.nextToken();
        // To check that the index input is an integer
        if(!isInt(check)) {
            return CommandController.notifyWithError(String.format(ERROR_WRONG_COMMAND_FORMAT, "update"));
        }
        int index = Integer.parseInt(check) - 1;
        // To check that the index is valid
        if (index < 0 || index >= currentList.size()) {
            return CommandController.notifyWithError(ERROR_INVALID_INDEX);
        }
        // Checking which parameters are updated
        String toBeUpdated = "";
        while (st.hasMoreTokens()) {
            toBeUpdated = toBeUpdated.concat(st.nextToken()) + " ";
            parameters[0] = true;
        }
        if (commandObject.hasStartDateKeyword()) {
            parameters[1] = true;
        }
        if (commandObject.hasEndDateKeyword()) {
            parameters[2] = true;
        }
        if (commandObject.getPriority() != null) {
            parameters[3] = true;
        }
        try {
            undoController.saveUndo(modelManager.getTodoItemList());
            undoController.clearRedo();
            modelManager.updateTask(currentList.get(index).getUUID(),
                                    parameters, toBeUpdated.trim(), commandObject.getStartDate(), commandObject.getEndDate(), commandObject.getPriority(), null);
        } catch (IOException e) {
            CommandController.notifyWithError("Failed to write to file.");
            LoggingService.getLogger().log(Level.SEVERE, "IOException: " + e.getMessage());
        } catch (NullPointerException e) {
            LoggingService.getLogger().log(Level.SEVERE, "NullPointerException" + e.getMessage());
        } catch (InvalidInputException e) {
            LoggingService.getLogger().log(Level.SEVERE, "InvalidInputException" + e.getMessage());
        }
        return CommandController.notifyWithInfo(String.format(MESSAGE_UPDATE_COMPLETE, commandObject.getInputString()));
    }

    /**
     * This method is used by CommandController to mark a TodoItem as done.
     * It takes a CommandObject and check whether the CommandObject is valid. It also takes the current list of
     * TodoItem to be able to update the TodoItem from the current list.
     * It calls UndoController to save the state for undo command.
     * It calls ModelManager to mark the TodoItem by a given index (from current list) as done.
     * 
     * @param commandObject
     * @param currentList
     * @return A feedback string to notify whether the method has carried out successfully.
     */
    public String done(CommandObject commandObject, ArrayList<TodoItem> currentList) {
        if (commandObject.getCommandString().isEmpty()) {
            return CommandController.notifyWithError(String.format(ERROR_WRONG_COMMAND_FORMAT, "done"));
        }
        // To check that the index input is an integer
        if (!isInt(commandObject.getCommandString())) {
            return CommandController.notifyWithError(String.format(ERROR_WRONG_COMMAND_FORMAT, "done"));
        }
        int index = Integer.parseInt(commandObject.getCommandString()) - 1;
        // To check that the index is valid
        if (index < 0 || index >= currentList.size()) {
            return CommandController.notifyWithError(ERROR_INVALID_INDEX);
        }
        Boolean[] parameters = {false, false, false, false, true};
        try {
            undoController.saveUndo(modelManager.getTodoItemList());
            undoController.clearRedo();
            modelManager.updateTask(currentList.get(index).getUUID(), parameters, null, null, null, null, true);
        } catch (IOException e) {
            CommandController.notifyWithError("Failed to write to file.");
            LoggingService.getLogger().log(Level.SEVERE, "IOException: " + e.getMessage());
        } catch (NullPointerException e) {
            LoggingService.getLogger().log(Level.SEVERE, "NullPointerException" + e.getMessage());
        } catch (InvalidInputException e) {
            // Should never happen at all.
            LoggingService.getLogger().log(Level.SEVERE, "InvalidInputException" + e.getMessage());
        }
        return CommandController.notifyWithInfo(String.format(MESSAGE_CHANGE_DONE_STATUS_COMPLETE, commandObject.getCommandString()));
    }

    /**
     * This method is used by CommandController to mark a TodoItem as undone.
     * It takes a CommandObject and check whether the CommandObject is valid. It also takes the current list of
     * TodoItem to be able to update the TodoItem from the current list.
     * It calls UndoController to save the state for undo command.
     * It calls ModelManager to mark the TodoItem by a given index (from current list) as undone.
     * 
     * @param commandObject
     * @param currentList
     * @return A feedback string to notify whether the method has carried out successfully.
     */
    public String undone(CommandObject commandObject, ArrayList<TodoItem> currentList) {
        if (commandObject.getCommandString().isEmpty()) {
            return CommandController.notifyWithError(String.format(ERROR_WRONG_COMMAND_FORMAT, "undone"));
        }
        // To check that the index input is an integer
        if (!isInt(commandObject.getCommandString())) {
            return CommandController.notifyWithError(String.format(ERROR_WRONG_COMMAND_FORMAT, "undone"));
        }
        int index = Integer.parseInt(commandObject.getCommandString()) - 1;
        // To check that the index is valid
        if (index < 0 || index >= currentList.size()) {
            return CommandController.notifyWithError(ERROR_INVALID_INDEX);
        }
        Boolean[] parameters = {false, false, false, false, true};
        try {
            undoController.saveUndo(modelManager.getTodoItemList());
            undoController.clearRedo();
            modelManager.updateTask(currentList.get(index).getUUID(), parameters, null, null, null, null, false);
        } catch (IOException e) {
            CommandController.notifyWithError("Failed to write to file.");
            LoggingService.getLogger().log(Level.SEVERE, "IOException: " + e.getMessage());
        } catch (NullPointerException e) {
            LoggingService.getLogger().log(Level.SEVERE, "NullPointerException" + e.getMessage());
        } catch (InvalidInputException e) {
            // Should never happen at all.
            LoggingService.getLogger().log(Level.SEVERE, "InvalidInputException" + e.getMessage());
        }
        return CommandController.notifyWithInfo(String.format(MESSAGE_CHANGE_DONE_STATUS_COMPLETE, commandObject.getCommandString()));
    }

    /**
     * This method is used by CommandController to open the help view.
     * It takes a CommandObject and check whether the CommandObject is valid.
     * It calls the commandController to open the help view.
     * 
     * @param commandObject
     * @return A feedback string to notify whether the method has carried out successfully.
     */
    public String help(CommandObject commandObject) {
        // To check whether CommandString is empty
        if (!commandObject.getCommandString().isEmpty()) {
            return CommandController.notifyWithError(ERROR_WRONG_COMMAND_FORMAT);
        }
        commandController.openHelp();
        return MESSAGE_OPEN_HELP;
    }

    /**
     * This method is used by CommandController to open the settings view.
     * It takes a CommandObject and check whether the CommandObject is valid.
     * It calls the commandController to open the settings view.
     * 
     * @param commandObject
     * @return A feedback string to notify whether the method has carried out successfully.
     */
    public String settings(CommandObject commandObject) {
     // To check whether CommandString is not empty
        if (!commandObject.getCommandString().isEmpty()) {
            return CommandController.notifyWithError(ERROR_WRONG_COMMAND_FORMAT);
        }
        commandController.openSettings();
        return MESSAGE_OPEN_SETTINGS;
    }
    
    /**
     * This method is used by CommandController to change save file location (for .json).
     * It takes a CommandObject and check whether the CommandObject is valid.
     * It calls ModelManager to change the save file location.
     * 
     * @param commandObject
     * @return A feedback string to notify whether the method has carried out successfully.
     */
    public String changeSaveLocation(CommandObject commandObject) {
        // To check whether CommandString is empty
        if (commandObject.getCommandString().isEmpty()) {
            return CommandController.notifyWithError(ERROR_WRONG_COMMAND_FORMAT);
        }
        try {
            modelManager.changeSettings(commandObject.getCommandString(), null, null);
        } catch (IOException e) {
            if (e.getMessage().equals(ModelManager.WRITE_SETTINGS_FAILED)) {
                CommandController.notifyWithError("Failed to write to settings.json file.");
            } else {
                CommandController.notifyWithError("Failed to load new file.");
            }
            LoggingService.getLogger().log(Level.SEVERE, "IOException: " + e.getMessage());
        } catch (NullPointerException e) {
            LoggingService.getLogger().log(Level.SEVERE, "NullPointerException" + e.getMessage());
        }
        return MESSAGE_CHANGE_SAVE_FILE_LOCATION;
    }

    //@author A0111987X
    // Undo and redo method(s)
    public String undo(CommandObject commandObject) {
        if (!commandObject.getCommandString().isEmpty()) {
            return CommandController.notifyWithError(ERROR_WRONG_COMMAND_FORMAT);
        }
        if (undoController.isUndoEmpty()) {
            return CommandController.notifyWithError(ERROR_WRONG_COMMAND_FORMAT);
        } else {
            try {
                undoController.saveRedo(modelManager.getTodoItemList());
                modelManager.loadTodoItems(undoController.loadUndo());
            } catch (IOException e) {
                CommandController.notifyWithError("Failed to write to file.");
                LoggingService.getLogger().log(Level.SEVERE, "IOException: " + e.getMessage());
            } catch (NullPointerException e) {
                LoggingService.getLogger().log(Level.SEVERE, "NullPointerException" + e.getMessage());
            }
            return MESSAGE_UNDO;
        }
    }

    //@author A0111987X
    public String redo(CommandObject commandObject) {
        if (!commandObject.getCommandString().isEmpty()) {
            return CommandController.notifyWithError(ERROR_WRONG_COMMAND_FORMAT);
        }
        if (undoController.isRedoEmpty()) {
            return CommandController.notifyWithError(ERROR_WRONG_COMMAND_FORMAT);
        } else {
            try {
                undoController.saveUndo(modelManager.getTodoItemList());
                modelManager.loadTodoItems(undoController.loadRedo());
            } catch (IOException e) {
                CommandController.notifyWithError("Failed to write to file.");
                LoggingService.getLogger().log(Level.SEVERE, "IOException: " + e.getMessage());
            } catch (NullPointerException e) {
                LoggingService.getLogger().log(Level.SEVERE, "NullPointerException" + e.getMessage());
            }
            return MESSAGE_REDO;
        }
    }

    //@author A0116703N
    /**
     * Calls modelManager to update with new settings.
     * 
     * @param filePath The new file directory to be used
     * @param randomColorsEnabled The new setting for random color display
     * @param notificationsEnabled The new setting for notifications display
     * @return The result string to be printed to the console
     */
    public String changeSettings(String filePath, Boolean randomColorsEnabled, Boolean notificationsEnabled) {
        assert filePath != null;
        
        try {
            modelManager.changeSettings(filePath, randomColorsEnabled, notificationsEnabled);
        } catch (IOException e) {
            if (e.getMessage().equals(ModelManager.WRITE_SETTINGS_FAILED)) {
                CommandController.notifyWithError("Failed to write to settings.json file.");
            } else {
                CommandController.notifyWithError("Failed to load new file.");
            }
            LoggingService.getLogger().log(Level.SEVERE, "IOException: " + e.getMessage());
        } catch (NullPointerException e) {
            LoggingService.getLogger().log(Level.SEVERE, "NullPointerException" + e.getMessage());
        }
        return "changed settings\n";
    }

    //@author A0114914L
    /**
     * Constructor for ActionController. It is called by CommandController. It is passed a ModelManager and
     * set the ModelManager inside this class.
     * 
     * @param manager
     */
    public ActionController(ModelManager manager) {
        modelManager = manager;
        if (manager != null) {
            returnList = modelManager.getTodoItemList();
        } else {
            returnList = null;
        }
    }
    
    /**
     * Method to set the UndoController. It is called by CommandController.
     * 
     * @param controller
     */
    public void setUndoController(UndoController controller) {
        undoController = controller;
    }

    /**
     * Method to set the TaskController. It is called by CommandController.
     * 
     * @param controller
     */
    protected void setTaskController(TaskController controller) {
        taskController = controller;
    }

    /**
     * Method to refer back to the CommandController.
     * 
     * @param controller
     */
    protected void setCommandController(CommandController controller) {
        commandController = controller;
    }

    /**
     * Getter for returnList.
     * 
     * @return returnList ArrayList of TodoItem of the current return list.
     */
    public ArrayList<TodoItem> getReturnList() {
        return returnList;
    }
}
