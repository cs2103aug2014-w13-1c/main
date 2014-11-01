package app.controllers;

import app.Main;
import app.helpers.LoggingService;
import app.helpers.CommandObject;
import app.model.ModelManager;
import app.model.TodoItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.logging.Level;

public class ActionController {
    // Errors
    private final String ERROR_FILE_EMPTY = "Task list is empty.\n";
    private final String ERROR_INVALID_INDEX = "Error. Index is not found.\n";
    private final String ERROR_WRONG_COMMAND_FORMAT = "Command error.\n";
    private final String ERROR_WRONG_ADD_COMMAND_FORMAT = "Error. Incorrect add command format. Click help icon or type help for info.\n";
    private final String ERROR_WRONG_CLEAR_COMMAND_FORMAT = "Error. Incorrect clear command format. Click help icon or type help for info.\n";
    private final String ERROR_WRONG_DELETE_COMMAND_FORMAT = "Error. Incorrect delete command format. Click help icon or type help for info.\n";
    private final String ERROR_WRONG_DISPLAY_COMMAND_FORMAT = "Error. Incorrect display command format. Click help icon or type help for info.\n";
    private final String ERROR_WRONG_DONE_COMMAND_FORMAT = "Error. Incorrect done command format. Click help icon or type help for info.\n";
    private final String ERROR_WRONG_SEARCH_COMMAND_FORMAT = "Error. Incorrect search command format. Click help icon or type help for info.\n";
    private final String ERROR_WRONG_UNDONE_COMMAND_FORMAT = "Error. Incorrect undone command format. Click help icon or type help for info.\n";
    private final String ERROR_WRONG_UPDATE_COMMAND_FORMAT = "Error. Incorrect update command format. Click help icon or type help for info.\n";
    private final String ERROR_SEARCH_TERM_NOT_FOUND = "Search term not found.\n";

    // Messages
    private final String MESSAGE_ADD_COMPLETE = "Added: \"%1$s\"\n";
    private final String MESSAGE_CHANGE_DONE_STATUS_COMPLETE = "Changed done status: \"%1$s\"\n";
    private final String MESSAGE_CLEAR_COMPLETE = "Todo cleared\n";
    private final String MESSAGE_DELETE_COMPLETE = "Deleted: \"%1$s\"\n";
    private final String MESSAGE_SEARCH_COMPLETE = "Search result(s):\n%1$s";
    private final String MESSAGE_UPDATE_COMPLETE = "Updated: \"%1$s\"\n";
    
    private final String MESSAGE_CHANGE_SAVE_FILE_LOCATION = "Save file location is changed\n";
    private final String MESSAGE_OPEN_HELP = "Showing help\n";
    private final String MESSAGE_OPEN_SETTINGS = "Showing settings\n";
    private final String MESSAGE_REDO = "Redo\n";
    private final String MESSAGE_UNDO = "Undo\n";
    
    // Class variables
    private static ModelManager modelManager;
    private static TaskController taskController;
    private static Main main;
    private static ArrayList<TodoItem> returnList;

    // Individual command methods
    // Add command method(s)
    protected String addNewLine(CommandObject commandObject){
        if (commandObject.getCommandString().isEmpty()) {
            return CommandController.showErrorDialog(ERROR_WRONG_ADD_COMMAND_FORMAT);
        }
        try {
            modelManager.addTask(commandObject.getCommandString(), commandObject.getStartDate(), commandObject.getEndDate(), commandObject.getPriority(), null);
        } catch (IOException e) {
            // do something here?
            LoggingService.getLogger().log(Level.SEVERE, "IOException: " + e.getMessage());
        }
        return CommandController.showInfoDialog(String.format(MESSAGE_ADD_COMPLETE, commandObject.getInputString()));
    }

    // Display command method(s)
    protected String display(CommandObject commandObject) {
        if (!commandObject.getCommandString().isEmpty()) {
            System.out.println(commandObject.getCommandString());
            if (commandObject.getCommandString().equals("all")) {
                returnList = taskController.getDoneTasks();
                returnList.addAll(taskController.getUndoneTasks());
            } else if (commandObject.getCommandString().equals("done")) {
                returnList = taskController.getDoneTasks();
            } else if (commandObject.getCommandString().equals("overdue")) {
                returnList = taskController.getOverdueTasks();
            } else {
                return CommandController.showErrorDialog(ERROR_WRONG_DISPLAY_COMMAND_FORMAT);
            }
        } else {
            returnList = taskController.getUndoneTasks();
        }
        main.getPrimaryStage().setTitle("wat do");
        return "displaying tasks\n";
    }

    // Clear command method(s)
    protected String clear(CommandObject commandObject) {
        if (!commandObject.getCommandString().isEmpty()) {
            return CommandController.showErrorDialog(ERROR_WRONG_CLEAR_COMMAND_FORMAT);
        }
        try {
            modelManager.clearTasks();
        } catch (IOException e) {
            // do something here?
            LoggingService.getLogger().log(Level.SEVERE, "IOException: " + e.getMessage());
        }
        return MESSAGE_CLEAR_COMPLETE;
    }
    
    // Delete command method(s)
    protected String deleteEntry(CommandObject commandObject, ArrayList<TodoItem> currentList) {
        if (commandObject.getCommandString().isEmpty()) {
            return CommandController.showErrorDialog(ERROR_WRONG_DELETE_COMMAND_FORMAT);
        }
        // To check that the index input is an integer
        if(!isInt(commandObject.getCommandString())) {
            return CommandController.showErrorDialog(ERROR_WRONG_DELETE_COMMAND_FORMAT);
        }
        int index = Integer.parseInt(commandObject.getCommandString()) - 1;
        // To check that the index is valid
        if (index < 0 || index >= currentList.size()) {
            return CommandController.showErrorDialog(ERROR_INVALID_INDEX);
        }
        String toBeDeleted = currentList.get(index).getTaskName();
        try {
            modelManager.deleteTask(currentList.get(index).getUUID());
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
    protected String search(CommandObject commandObject) {
        if (commandObject.getCommandString().isEmpty()) {
            return CommandController.showErrorDialog(ERROR_WRONG_SEARCH_COMMAND_FORMAT);
        }
        ArrayList<TodoItem> todoList = modelManager.getTodoItemList();
        if (todoList.isEmpty()) {
            return CommandController.showErrorDialog(String.format(ERROR_FILE_EMPTY));
        }
        ArrayList<TodoItem> results = taskController.instantSearch(commandObject.getCommandString());
        if (results.isEmpty()) {
            return CommandController.showErrorDialog(ERROR_SEARCH_TERM_NOT_FOUND);
        } else {
            returnList = results;
            main.getPrimaryStage().setTitle("Search results for: \"" + commandObject.getCommandString() + "\"");
            return String.format(MESSAGE_SEARCH_COMPLETE, "updating task list view with results\n");
        }
    }

    // Update command method(s)
    protected String update(CommandObject commandObject, ArrayList<TodoItem> currentList) {
        if (commandObject.getCommandString().isEmpty()) {
            return CommandController.showErrorDialog(ERROR_WRONG_UPDATE_COMMAND_FORMAT);
        }
        Boolean[] parameters = {false, false, false, false, false};
        
        StringTokenizer st = new StringTokenizer(commandObject.getCommandString());
        String check = st.nextToken();
        // To check that the index input is an integer
        if(!isInt(check)) {
            return CommandController.showErrorDialog(ERROR_WRONG_UPDATE_COMMAND_FORMAT);
        }
        int index = Integer.parseInt(check) - 1;
        // To check that the index is valid
        if (index < 0 || index >= currentList.size()) {
            return CommandController.showErrorDialog(ERROR_INVALID_INDEX);
        }
        String toBeUpdated = "";
        while (st.hasMoreTokens()) {
            toBeUpdated = toBeUpdated.concat(st.nextToken()) + " ";
            parameters[0] = true;
        }
        if (commandObject.getStartDate() != null) {
            parameters[1] = true;
        }
        if (commandObject.getEndDate() != null) {
            parameters[2] = true;
        }
        if (commandObject.getPriority() != null) {
            parameters[3] = true;
        }
        try {
            modelManager.updateTask(currentList.get(index).getUUID(),
                                    parameters, toBeUpdated.trim(), commandObject.getStartDate(), commandObject.getEndDate(), commandObject.getPriority(), null);
        } catch (IOException e) {
            // do something here?
            LoggingService.getLogger().log(Level.SEVERE, "IOException: " + e.getMessage());
        }
        return CommandController.showInfoDialog(String.format(MESSAGE_UPDATE_COMPLETE, index + 1));
    }

    // Done method
    protected String done(CommandObject commandObject, ArrayList<TodoItem> currentList) {
        if (commandObject.getCommandString().isEmpty()) {
            return CommandController.showErrorDialog(ERROR_WRONG_DONE_COMMAND_FORMAT);
        }
        // To check that the index input is an integer
        if (!isInt(commandObject.getCommandString())) {
            return CommandController.showErrorDialog(ERROR_WRONG_DONE_COMMAND_FORMAT);
        }
        int index = Integer.parseInt(commandObject.getCommandString()) - 1;
        // To check that the index is valid
        if (index < 0 || index >= currentList.size()) {
            return CommandController.showErrorDialog(ERROR_INVALID_INDEX);
        }
        Boolean[] parameters = {false, false, false, false, true};
        try {
            modelManager.updateTask(currentList.get(index).getUUID(), parameters, null, null, null, null, true);
        } catch (IOException e) {
            // do something here?
            LoggingService.getLogger().log(Level.SEVERE, "IOException: " + e.getMessage());
        }
        return CommandController.showInfoDialog(String.format(MESSAGE_CHANGE_DONE_STATUS_COMPLETE, commandObject.getCommandString()));
    }

    // Undone method
    protected String undone(CommandObject commandObject, ArrayList<TodoItem> currentList) {
        if (commandObject.getCommandString().isEmpty()) {
            return CommandController.showErrorDialog(ERROR_WRONG_UNDONE_COMMAND_FORMAT);
        }
        // To check that the index input is an integer
        if (!isInt(commandObject.getCommandString())) {
            return CommandController.showErrorDialog(ERROR_WRONG_UNDONE_COMMAND_FORMAT);
        }
        int index = Integer.parseInt(commandObject.getCommandString()) - 1;
        // To check that the index is valid
        if (index < 0 || index >= currentList.size()) {
            return CommandController.showErrorDialog(ERROR_INVALID_INDEX);
        }
        Boolean[] parameters = {false, false, false, false, true};
        try {
            modelManager.updateTask(currentList.get(index).getUUID(), parameters, null, null, null, null, false);
        } catch (IOException e) {
            // do something here?
            LoggingService.getLogger().log(Level.SEVERE, "IOException: " + e.getMessage());
        }
        return CommandController.showInfoDialog(String.format(MESSAGE_CHANGE_DONE_STATUS_COMPLETE, commandObject.getCommandString()));
    }

    // Help method
    protected String help(CommandObject commandObject) {
        if (!commandObject.getCommandString().isEmpty()) {
            return CommandController.showErrorDialog(ERROR_WRONG_COMMAND_FORMAT);
        }
        main.getRootViewManager().openHelp();
        return MESSAGE_OPEN_HELP;
    }

    // Settings method
    protected String settings(CommandObject commandObject) {
        if (!commandObject.getCommandString().isEmpty()) {
            return CommandController.showErrorDialog(ERROR_WRONG_COMMAND_FORMAT);
        }
        main.getRootViewManager().openSettings();
        return MESSAGE_OPEN_SETTINGS;
    }
    
    // Change save file location (for .json)
    protected String changeSaveLocation(CommandObject commandObject) {
        if (commandObject.getCommandString().isEmpty()) {
            return CommandController.showErrorDialog(ERROR_WRONG_COMMAND_FORMAT);
        }
        try {
            modelManager.changeSettings(commandObject.getCommandString(), null, null);
        } catch (IOException e) {
            // do something here?
            LoggingService.getLogger().log(Level.SEVERE, "IOException: " + e.getMessage());
        }
        return MESSAGE_CHANGE_SAVE_FILE_LOCATION;
    }

    // Undo and redo method(s)
    protected String undo(CommandObject commandObject) {
        if (!commandObject.getCommandString().isEmpty()) {
            return CommandController.showErrorDialog(ERROR_WRONG_COMMAND_FORMAT);
        }
        if (main.getCommandController().getUndoController().isUndoEmpty()) {
            return CommandController.showErrorDialog(ERROR_WRONG_COMMAND_FORMAT);
        } else {
            main.getCommandController().getUndoController().saveRedo(modelManager.getTodoItemList());
            try {
                modelManager.loadTodoItems(main.getCommandController().getUndoController().loadUndo());
            } catch (IOException e) {
                // do something here?
                LoggingService.getLogger().log(Level.SEVERE, "IOException: " + e.getMessage());
            }
            return MESSAGE_UNDO;
        }
    }

    protected String redo(CommandObject commandObject) {
        if (!commandObject.getCommandString().isEmpty()) {
            return CommandController.showErrorDialog(ERROR_WRONG_COMMAND_FORMAT);
        }
        if (main.getCommandController().getUndoController().isRedoEmpty()) {
            return CommandController.showErrorDialog(ERROR_WRONG_COMMAND_FORMAT);
        } else {
            main.getCommandController().getUndoController().saveUndo(modelManager.getTodoItemList());
            try {
                modelManager.loadTodoItems(main.getCommandController().getUndoController().loadRedo());
            } catch (IOException e) {
                // do something here?
                LoggingService.getLogger().log(Level.SEVERE, "IOException: " + e.getMessage());
            }
            return MESSAGE_REDO;
        }
    }


    protected ActionController(ModelManager manager) {
        modelManager = manager;
    }

    protected void setTaskController(TaskController controller) {
        taskController = controller;
    }

    protected ArrayList<TodoItem> getReturnList() {
        return returnList;
    }

    protected void setMainApp(Main main) {
        this.main = main;
    }
}
