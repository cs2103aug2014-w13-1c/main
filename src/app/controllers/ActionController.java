package app.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import app.Main;
import app.controllers.CommandController.COMMAND_TYPE;
import app.helpers.LoggingService;
import app.model.ModelManager;
import app.model.TodoItem;

public class ActionController {
    protected enum COMMAND_TYPE {
        ADD, DELETE, DISPLAY, CLEAR, EXIT, SEARCH, UPDATE, HELP, SETTINGS, INVALID, INVALID_DATE,
    }

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
    private CommandParser parsedCommand;

    // Print string methods
    protected void printString(String message) {
        System.out.print(message);
    }
    
 // Individual command methods
    // Add command method(s)
    protected String addNewLine(CommandParser parsedCommand){
        if (parsedCommand.getCommandString().isEmpty()) {
            return showErrorDialog(ERROR_WRONG_COMMAND_FORMAT);
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
        resetTaskList();
        return showInfoDialog(String.format(MESSAGE_ADD_COMPLETE, parsedCommand.getInputString()));
    }

    // Display command method(s)
    protected String display(CommandParser parsedCommand) {
        if (!parsedCommand.getCommandString().isEmpty()) {
            return showErrorDialog(ERROR_WRONG_COMMAND_FORMAT);
        }
        currentList = taskList.getTodoItemList();
        main.getPrimaryStage().setTitle("wat do");
        updateView();
        return "displaying tasks\n";
    }

    public ObservableList<TodoItem> convertList(ArrayList<TodoItem> todoList) {
        ObservableList<TodoItem> taskData = FXCollections.observableArrayList();
        int index = 1;
        for (TodoItem todo : todoList) {
            taskData.add(new TodoItem(index + ". " + todo.getTaskName(), todo.getStartDate(), todo.getEndDate(), todo.getPriority(), null));
            index++;
        }
        return taskData;
    }

    // Clear command method(s)
    protected String clear(CommandParser parsedCommand) {
        if (!parsedCommand.getCommandString().isEmpty()) {
            return showErrorDialog(ERROR_WRONG_COMMAND_FORMAT);
        }
        try {
            taskList.clearTasks();
        } catch (IOException e) {
            // do something here?
            LoggingService.getLogger().log(Level.SEVERE, "IOException: " + e.getMessage());
        }
        resetTaskList();
        return MESSAGE_CLEAR_COMPLETE;
    }
    
    // Delete command method(s)
    protected String deleteEntry(CommandParser parsedCommand) {
        if (parsedCommand.getCommandString().isEmpty()) {
            return showErrorDialog(ERROR_WRONG_COMMAND_FORMAT);
        }
        int index = -1;
        index = Integer.parseInt(parsedCommand.getCommandString()) - 1;
        if(isInt(parsedCommand.getCommandString())) {
            index = Integer.parseInt(parsedCommand.getCommandString()) - 1;
        }
        ArrayList<TodoItem> todoList = taskList.getTodoItemList();
        if (index < 0 || index >= todoList.size()) {
            return showErrorDialog(ERROR_WRONG_COMMAND_FORMAT);
        }
        String toBeDeleted = todoList.get(index).getTaskName();
        try {
            taskList.deleteTask(todoList.get(index).getUUID());
        } catch (IOException e) {
            // do something here?
            LoggingService.getLogger().log(Level.SEVERE, "IOException: " + e.getMessage());
        }
        resetTaskList();
        return showInfoDialog(String.format(MESSAGE_DELETE_COMPLETE, toBeDeleted));
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
            return showErrorDialog(ERROR_WRONG_COMMAND_FORMAT);
        }
        ArrayList<TodoItem> todoList = taskList.getTodoItemList();
        if (todoList.isEmpty()) {
            return showErrorDialog(String.format(ERROR_FILE_EMPTY));
        }
        ArrayList<TodoItem> results = main.getTaskController().instantSearch(parsedCommand.getCommandString());
        if (results.isEmpty()) {
            return showErrorDialog(ERROR_SEARCH_TERM_NOT_FOUND);
        } else {
            currentList = results;
            main.getPrimaryStage().setTitle("Search results for: \"" + parsedCommand.getCommandString() + "\"");
            updateView();
            return String.format(MESSAGE_SEARCH_COMPLETE, "updating task list view with results\n");
        }
    }

    // Update command method(s)
    protected String update(CommandParser parsedCommand) {
        if (parsedCommand.getCommandString().isEmpty()) {
            return showErrorDialog(ERROR_WRONG_COMMAND_FORMAT);
        }
        int nextSpacePos = parsedCommand.getCommandString().indexOf(" ");
        if (nextSpacePos == -1) {
            return showErrorDialog(ERROR_WRONG_COMMAND_FORMAT);
        }
        int index = -1;
        if(isInt(parsedCommand.getCommandString().substring(0, nextSpacePos))) {
            index = Integer.parseInt(parsedCommand.getCommandString().substring(0, nextSpacePos)) - 1;
        }
        ArrayList<TodoItem> todoList = taskList.getTodoItemList();
        if (index < 0 || index >= todoList.size()) {
            return showErrorDialog(ERROR_WRONG_COMMAND_FORMAT);
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
        resetTaskList();
        return showInfoDialog(String.format(MESSAGE_UPDATE_COMPLETE, toBeUpdated));
    }

    // Help method
    protected String help(CommandParser parsedCommand) {
        if (!parsedCommand.getCommandString().isEmpty()) {
            return showErrorDialog(ERROR_WRONG_COMMAND_FORMAT);
        }
        main.getRootViewManager().openHelp();
        return "showing help\n";
    }

    // Settings method
    protected String settings(CommandParser parsedCommand) {
        if (!parsedCommand.getCommandString().isEmpty()) {
            return showErrorDialog(ERROR_WRONG_COMMAND_FORMAT);
        }
        main.getRootViewManager().openSettings();
        return "showing settings\n";
    }

    // Command processing methods
    protected COMMAND_TYPE determineCommandType(String commandWord) {
        if (commandWord.equalsIgnoreCase("add")) {
            return COMMAND_TYPE.ADD;
        } else if (commandWord.equalsIgnoreCase("delete")) {
            return COMMAND_TYPE.DELETE;
        } else if (commandWord.equalsIgnoreCase("display")) {
            return COMMAND_TYPE.DISPLAY;
        } else if (commandWord.equalsIgnoreCase("clear")) {
            return COMMAND_TYPE.CLEAR;
        } else if (commandWord.equalsIgnoreCase("exit")) {
            return COMMAND_TYPE.EXIT;
        } else if (commandWord.equalsIgnoreCase("search")) {
            return COMMAND_TYPE.SEARCH;
        } else if (commandWord.equalsIgnoreCase("update")) {
            return COMMAND_TYPE.UPDATE;
        } else if (commandWord.equalsIgnoreCase("help")) {
            return COMMAND_TYPE.HELP;
        } else if (commandWord.equalsIgnoreCase("settings")) {
            return COMMAND_TYPE.SETTINGS;
        } else if (commandWord.equalsIgnoreCase("dateError")) {
            return COMMAND_TYPE.INVALID_DATE;
        } else {
            return COMMAND_TYPE.INVALID;
        }
    }

    protected String processCommand(CommandParser parsedCommand) {
        String commandWord = parsedCommand.getCommandWord();
        COMMAND_TYPE commandType = determineCommandType(commandWord);
        switch (commandType) {
            case ADD :
                return addNewLine(parsedCommand);
            case DISPLAY :
                return display(parsedCommand);
            case CLEAR :
                return clear(parsedCommand);
            case DELETE :
                return deleteEntry(parsedCommand);
            case EXIT :
                showInfoDialog("Bye!");
                System.exit(0);
            case SEARCH :
                return search(parsedCommand);
            case UPDATE :
                return update(parsedCommand);
            case HELP :
                return help(parsedCommand);
            case SETTINGS :
                return settings(parsedCommand);
            case INVALID_DATE :
                return showErrorDialog(ERROR_INVALID_DATE);
            default :
                return showErrorDialog(ERROR_WRONG_COMMAND_FORMAT);
        }
    }

    public void updateView() {
        main.getRootViewManager().getTaskListViewManager().updateView(convertList(currentList));
    }

    public void updateView(ArrayList<TodoItem> todoItems) {
        main.getRootViewManager().getTaskListViewManager().updateView(convertList(todoItems));
    }

    public ArrayList<TodoItem> getTaskList() {
        return taskList.getTodoItemList();
    }

    public void setTaskList(ArrayList<TodoItem> todoList) {
        currentList = todoList;
    }

    public void resetTaskList() {
        main.getPrimaryStage().setTitle("wat do");
        setTaskList(getTaskList());
    }

    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param main
     */
    public void setMainApp(Main main) {
        this.main = main;
    }

    public String showErrorDialog(String error) {
        main.showErrorDialog("Error", error);
        return error;
    }

    public String showInfoDialog(String message) {
        main.showInfoDialog("Information", message);
        return message;
    }
}
