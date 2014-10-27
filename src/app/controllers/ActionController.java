package app.controllers;

import app.Main;
import app.helpers.LoggingService;
import app.model.ModelManager;
import app.model.TodoItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.logging.Level;

public class ActionController {
    // Errors
    private final String ERROR_FILE_EMPTY = "Task list is empty.\n";
    private final String ERROR_INVALID_DATE = "Error. Invalid Date\n";
    private final String ERROR_WRONG_COMMAND_FORMAT = "Command error.\n";
    private final String ERROR_SEARCH_TERM_NOT_FOUND = "Search term not found.\n";

    // Messages
    private final String MESSAGE_ADD_COMPLETE = "Added: \"%1$s\"\n";
    private final String MESSAGE_CLEAR_COMPLETE = "Todo cleared\n";
    private final String MESSAGE_DELETE_COMPLETE = "Deleted: \"%1$s\"\n";
    private final String MESSAGE_SEARCH_COMPLETE = "Search result(s):\n%1$s";
    private final String MESSAGE_UPDATE_COMPLETE = "Updated: \"%1$s\"\n";
    private final String MESSAGE_CHANGE_DONE_STATUS_COMPLETE = "Changed done status: \"%1$s\"\n";


    // Class variables
    private ModelManager taskList;
    private Main main;
    private ArrayList<TodoItem> currentList;

    // Individual command methods
    // Add command method(s)
    protected String addNewLine(CommandParser parsedCommand){
        if (parsedCommand.getCommandString().isEmpty()) {
            return CommandController.showErrorDialog(ERROR_WRONG_COMMAND_FORMAT);
        }
        try {
            taskList.addTask(parsedCommand.getCommandString(), parsedCommand.getStartDate(), parsedCommand.getEndDate(), parsedCommand.getPriority(), null);
        } catch (IOException e) {
            // do something here?
            LoggingService.getLogger().log(Level.SEVERE, "IOException: " + e.getMessage());
        }
        return CommandController.showInfoDialog(String.format(MESSAGE_ADD_COMPLETE, parsedCommand.getInputString()));
    }

    // Display command method(s)
    protected String display(CommandParser parsedCommand) {
        if (!parsedCommand.getCommandString().isEmpty()) {
            return CommandController.showErrorDialog(ERROR_WRONG_COMMAND_FORMAT);
        }
        currentList = taskList.getTodoItemList();
        main.getPrimaryStage().setTitle("wat do");
        return "displaying tasks\n";
    }

    // Clear command method(s)
    protected String clear(CommandParser parsedCommand) {
        if (!parsedCommand.getCommandString().isEmpty()) {
            return CommandController.showErrorDialog(ERROR_WRONG_COMMAND_FORMAT);
        }
        try {
            taskList.clearTasks();
        } catch (IOException e) {
            // do something here?
            LoggingService.getLogger().log(Level.SEVERE, "IOException: " + e.getMessage());
        }
        return MESSAGE_CLEAR_COMPLETE;
    }
    
    // Delete command method(s)
    protected String deleteEntry(CommandParser parsedCommand) {
        if (parsedCommand.getCommandString().isEmpty()) {
            return CommandController.showErrorDialog(ERROR_WRONG_COMMAND_FORMAT);
        }
        int index = -1;
        index = Integer.parseInt(parsedCommand.getCommandString()) - 1;
        if(isInt(parsedCommand.getCommandString())) {
            index = Integer.parseInt(parsedCommand.getCommandString()) - 1;
        }
        ArrayList<TodoItem> todoList = currentList;
//        System.out.println("currentlist size: " + todoList.size());
        if (index < 0 || index >= todoList.size()) {
            return CommandController.showErrorDialog(ERROR_WRONG_COMMAND_FORMAT);
        }
        String toBeDeleted = todoList.get(index).getTaskName();
        try {
            taskList.deleteTask(todoList.get(index).getUUID());
        } catch (IOException e) {
            // do something here?
            LoggingService.getLogger().log(Level.SEVERE, "IOException: " + e.getMessage());
        }
        return CommandController.showInfoDialog(String.format(MESSAGE_DELETE_COMPLETE, toBeDeleted));
    }

    protected boolean isInt(String number) {
        try {
            Integer.parseInt(number);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    // Search command method(s)
    protected String search(CommandParser parsedCommand) {
        if (parsedCommand.getCommandString().isEmpty()) {
            return CommandController.showErrorDialog(ERROR_WRONG_COMMAND_FORMAT);
        }
        ArrayList<TodoItem> todoList = taskList.getTodoItemList();
        if (todoList.isEmpty()) {
            return CommandController.showErrorDialog(String.format(ERROR_FILE_EMPTY));
        }
        ArrayList<TodoItem> results = main.getTaskController().instantSearch(parsedCommand.getCommandString());
        if (results.isEmpty()) {
            return CommandController.showErrorDialog(ERROR_SEARCH_TERM_NOT_FOUND);
        } else {
            currentList = results;
            main.getPrimaryStage().setTitle("Search results for: \"" + parsedCommand.getCommandString() + "\"");
            return String.format(MESSAGE_SEARCH_COMPLETE, "updating task list view with results\n");
        }
    }

    // Update command method(s)
    protected String update(CommandParser parsedCommand) {
        if (parsedCommand.getCommandString().isEmpty()) {
            return CommandController.showErrorDialog(ERROR_WRONG_COMMAND_FORMAT);
        }
        Boolean[] parameters = {false, false, false, false, false};
        
        StringTokenizer st = new StringTokenizer(parsedCommand.getCommandString());
        String check = st.nextToken();
        int index = -1;
        // To check that the index input is an integer
        if(isInt(check)) {
            index = Integer.parseInt(check) - 1;
        }
        // To check that the index is valid
        ArrayList<TodoItem> todoList = currentList;
        if (index < 0 || index >= todoList.size()) {
            return CommandController.showErrorDialog(ERROR_WRONG_COMMAND_FORMAT);
        }
        String toBeUpdated = "";
        while (st.hasMoreTokens()) {
            toBeUpdated = toBeUpdated.concat(st.nextToken()) + " ";
            parameters[0] = true;
        }
        if (parsedCommand.getStartDate() != null) {
            parameters[1] = true;
        }
        if (parsedCommand.getEndDate() != null) {
            parameters[2] = true;
        }
        if (parsedCommand.getPriority() != null) {
            parameters[3] = true;
        }
        try {
            taskList.updateTask(currentList.get(index).getUUID(),
                                parameters, toBeUpdated.trim(), parsedCommand.getStartDate(), parsedCommand.getEndDate(), parsedCommand.getPriority(), null);
        } catch (IOException e) {
            // do something here?
            LoggingService.getLogger().log(Level.SEVERE, "IOException: " + e.getMessage());
        }
        return CommandController.showInfoDialog(String.format(MESSAGE_UPDATE_COMPLETE, index + 1));
    }

    // Done method
    protected String done(CommandParser parsedCommand) {
        if (parsedCommand.getCommandString().isEmpty()) {
            return CommandController.showErrorDialog(ERROR_WRONG_COMMAND_FORMAT);
        }
        // To check that the index input is an integer
        if (!isInt(parsedCommand.getCommandString())) {
            return CommandController.showErrorDialog(ERROR_WRONG_COMMAND_FORMAT);
        }
        int index = Integer.parseInt(parsedCommand.getCommandString()) - 1;
        // To check that the index is valid
        ArrayList<TodoItem> todoList = currentList;
        if (index < 0 || index >= todoList.size()) {
            return CommandController.showErrorDialog(ERROR_WRONG_COMMAND_FORMAT);
        }
        Boolean[] parameters = {false, false, false, false, true};
        try {
            taskList.updateTask(currentList.get(index).getUUID(), parameters, null, null, null, null, true);
        } catch (IOException e) {
            // do something here?
            LoggingService.getLogger().log(Level.SEVERE, "IOException: " + e.getMessage());
        }
        return CommandController.showInfoDialog(String.format(MESSAGE_CHANGE_DONE_STATUS_COMPLETE, parsedCommand.getCommandString()));
    }

    // Undone method
    protected String undone(CommandParser parsedCommand) {
        if (parsedCommand.getCommandString().isEmpty()) {
            return CommandController.showErrorDialog(ERROR_WRONG_COMMAND_FORMAT);
        }
        // To check that the index input is an integer
        if (!isInt(parsedCommand.getCommandString())) {
            return CommandController.showErrorDialog(ERROR_WRONG_COMMAND_FORMAT);
        }
        int index = Integer.parseInt(parsedCommand.getCommandString()) - 1;
        // To check that the index is valid
        ArrayList<TodoItem> todoList = currentList;
        if (index < 0 || index >= todoList.size()) {
            return CommandController.showErrorDialog(ERROR_WRONG_COMMAND_FORMAT);
        }
        Boolean[] parameters = {false, false, false, false, true};
        try {
            taskList.updateTask(currentList.get(index).getUUID(), parameters, null, null, null, null, false);
        } catch (IOException e) {
            // do something here?
            LoggingService.getLogger().log(Level.SEVERE, "IOException: " + e.getMessage());
        }
        return CommandController.showInfoDialog(String.format(MESSAGE_CHANGE_DONE_STATUS_COMPLETE, parsedCommand.getCommandString()));
    }

    // Help method
    protected String help(CommandParser parsedCommand) {
        if (!parsedCommand.getCommandString().isEmpty()) {
            return CommandController.showErrorDialog(ERROR_WRONG_COMMAND_FORMAT);
        }
        main.getRootViewManager().openHelp();
        return "showing help\n";
    }

    // Settings method
    protected String settings(CommandParser parsedCommand) {
        if (!parsedCommand.getCommandString().isEmpty()) {
            return CommandController.showErrorDialog(ERROR_WRONG_COMMAND_FORMAT);
        }
        main.getRootViewManager().openSettings();
        return "showing settings\n";
    }
    
    // Change save file location (for .json)
    protected String changeSaveLocation(CommandParser parsedCommand) {
        if (parsedCommand.getCommandString().isEmpty()) {
            return CommandController.showErrorDialog(ERROR_WRONG_COMMAND_FORMAT);
        }
        try {
            taskList.changeFileDirectory(parsedCommand.getCommandString());
        } catch (IOException e) {
            // do something here?
            LoggingService.getLogger().log(Level.SEVERE, "IOException: " + e.getMessage());
        }
        return "changed save location\n";
    }


    protected ActionController() {
        try {
            taskList = new ModelManager();
        } catch (IOException e) {
            // do something here?
            LoggingService.getLogger().log(Level.SEVERE, "IOException: " + e.getMessage());
        }
    }

    protected void initCurrentList() {
        currentList = main.getCommandController().getCurrentList();
    }
    
    protected ArrayList<TodoItem> getCurrentList() {
        return currentList;
    }
    
    protected ModelManager getTaskList() {
        return taskList;
    }

    /**
     * Is called by the CommandController to set the main app for ActionController.
     *
     * @param main
     */
    protected void setMainApp(Main main) {
        this.main = main;
    }
}
