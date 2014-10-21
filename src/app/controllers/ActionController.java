package app.controllers;

import app.Main;
import app.helpers.Keyword;
import app.helpers.LoggingService;
import app.model.ModelManager;
import app.model.TodoItem;

import java.io.IOException;
import java.util.ArrayList;
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
            if (parsedCommand.getPriority().equals("high")) {
                taskList.addTask(parsedCommand.getCommandString(), parsedCommand.getStartDate(), parsedCommand.getEndDate(), TodoItem.HIGH, null);
            }
            else if (parsedCommand.getPriority().equals("low")) {
                taskList.addTask(parsedCommand.getCommandString(), parsedCommand.getStartDate(), parsedCommand.getEndDate(), TodoItem.LOW, null);
            }
            else {
                taskList.addTask(parsedCommand.getCommandString(), parsedCommand.getStartDate(), parsedCommand.getEndDate(), TodoItem.MEDIUM, null);
            }
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
        ArrayList<TodoItem> todoList = taskList.getTodoItemList();
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
        int nextSpacePos = parsedCommand.getCommandString().indexOf(" ");
        if (nextSpacePos == -1) {
            return CommandController.showErrorDialog(ERROR_WRONG_COMMAND_FORMAT);
        }
        int index = -1;
        if(isInt(parsedCommand.getCommandString().substring(0, nextSpacePos))) {
            index = Integer.parseInt(parsedCommand.getCommandString().substring(0, nextSpacePos)) - 1;
        }
        ArrayList<TodoItem> todoList = taskList.getTodoItemList();
        if (index < 0 || index >= todoList.size()) {
            return CommandController.showErrorDialog(ERROR_WRONG_COMMAND_FORMAT);
        }
        String toBeUpdated = parsedCommand.getCommandString().substring(nextSpacePos + 1);
        Boolean[] parameters = {true, true, true, false, false};
        try {
            taskList.updateTask(taskList.getTodoItemList().get(index).getUUID(),
                                parameters, toBeUpdated, parsedCommand.getStartDate(), parsedCommand.getEndDate(), null, null);
        } catch (IOException e) {
            // do something here?
            LoggingService.getLogger().log(Level.SEVERE, "IOException: " + e.getMessage());
        }
        return CommandController.showInfoDialog(String.format(MESSAGE_UPDATE_COMPLETE, toBeUpdated));
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


    protected ActionController() {
        try {
            taskList = new ModelManager();
        } catch (IOException e) {
            // do something here?
            LoggingService.getLogger().log(Level.SEVERE, "IOException: " + e.getMessage());
        }
        currentList = new ArrayList<TodoItem>();
    }
    
    protected ArrayList<TodoItem> getCurrentList() {
        return currentList;
    }
    
    protected ModelManager getTaskList() {
        return taskList;
    }

    private ArrayList<Keyword> parseKeywords(String inputString) {
        return CommandParser.getKeywords(inputString);
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
