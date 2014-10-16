package app.controllers;

import app.Main;
import app.helpers.Keyword;
import app.model.TodoItem;
import app.model.TodoItemList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

/**
 * Class CommandController
 * 
 * This class is the main controller.
 * Skeleton based on jolly's CE2.
 *
 * @author ryan
 */

public class CommandController {
    protected enum COMMAND_TYPE {
        ADD, DELETE, DISPLAY, CLEAR, EXIT, INVALID, SEARCH, UPDATE, HELP, SETTINGS
    }

    // Errors
    private final String ERROR_FILE_EMPTY = "Task list is empty.\n";
    private final String ERROR_WRONG_COMMAND_FORMAT = "Command error.\n";
    private final String ERROR_SEARCH_TERM_NOT_FOUND = "Search term not found.\n";

    // Messages
    private final String MESSAGE_ADD_COMPLETE = "Added: \"%1$s\"\n";
    private final String MESSAGE_CLEAR_COMPLETE = "Todo cleared\n";
    private final String MESSAGE_DELETE_COMPLETE = "Deleted: \"%1$s\"\n";
    private final String MESSAGE_SEARCH_COMPLETE = "Search result(s):\n%1$s";
    private final String MESSAGE_UPDATE_COMPLETE = "Updated: \"%1$s\"\n";

    // Class variables
    private TodoItemList taskList;
    private Main main;
    private ArrayList<TodoItem> currentList;
    private CommandParser parsedCommand;

    // String manipulation methods
    protected void printString(String message) {
        System.out.print(message);
    }

    protected int firstSpacePosition(String command) {
        return command.indexOf(" ");
    }

    protected String getFirstWord(String command) {
        int firstWordPos = firstSpacePosition(command);
        if(firstWordPos == -1) {
            return command;
        }
        return command.substring(0, firstWordPos);
    }

    // Individual command methods
    // Add command method(s)
    protected String addNewLine(CommandParser parsedCommand){
        if (parsedCommand.getCommandString().isEmpty()) {
            return showErrorDialog(ERROR_WRONG_COMMAND_FORMAT);
        }
        taskList.addTodoItem(new TodoItem(parsedCommand.getCommandString(), parsedCommand.getStartDate(), parsedCommand.getEndDate()));
        resetTaskList();
        return showInfoDialog(String.format(MESSAGE_ADD_COMPLETE, parsedCommand.getInputString()));
    }

    // Display command method(s)
    protected String display(CommandParser parsedCommand) {
        if (!parsedCommand.getCommandString().isEmpty()) {
            return showErrorDialog(ERROR_WRONG_COMMAND_FORMAT);
        }
        currentList = taskList.getTodoItems();
        main.getPrimaryStage().setTitle("wat do");
        updateView();
        return "displaying tasks\n";
    }

    public ObservableList<TodoItem> convertList(ArrayList<TodoItem> todoList) {
        ObservableList<TodoItem> taskData = FXCollections.observableArrayList();
        int index = 1;
        for (TodoItem todo : todoList) {
            taskData.add(new TodoItem(index + ". " + todo.getTaskName(), todo.getStartDate(), todo.getEndDate()));
            index++;
        }
        return taskData;
    }

    // Clear command method(s)
    protected String clear(CommandParser parsedCommand) {
        if (!parsedCommand.getCommandString().isEmpty()) {
            return showErrorDialog(ERROR_WRONG_COMMAND_FORMAT);
        }
        taskList.clearTodoItems();
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
        ArrayList<TodoItem> todoList = taskList.getTodoItems();
        if (index < 0 || index >= todoList.size()) {
            return showErrorDialog(ERROR_WRONG_COMMAND_FORMAT);
        }
        String toBeDeleted = todoList.get(index).getTaskName();
        taskList.deleteTodoItem(index);
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
        ArrayList<TodoItem> todoList = taskList.getTodoItems();
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
        ArrayList<TodoItem> todoList = taskList.getTodoItems();
        if (index < 0 || index >= todoList.size()) {
            return showErrorDialog(ERROR_WRONG_COMMAND_FORMAT);
        }
        String toBeUpdated = parsedCommand.getCommandString().substring(nextSpacePos + 1);
        taskList.updateTodoItem(index, toBeUpdated, parsedCommand.getStartDate(), parsedCommand.getEndDate());
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
            default :
                return showErrorDialog(ERROR_WRONG_COMMAND_FORMAT);
        }
    }

    // CommandController public methods
    public CommandController() {
        taskList = new TodoItemList();
        currentList = new ArrayList<TodoItem>();
    }

    public void parseCommand(String inputString) {
        printString("Parsing: \"" + inputString + "\"\n");
        parsedCommand = new CommandParser(inputString);
        printString(processCommand(parsedCommand));
    }

    public ArrayList<Keyword> parseKeywords(String inputString) {
        return CommandParser.getKeywords(inputString);
    }
        
    public void updateView() {
        main.getRootViewManager().getTaskListViewManager().updateView(convertList(currentList));
    }

    public void updateView(ArrayList<TodoItem> todoItems) {
        main.getRootViewManager().getTaskListViewManager().updateView(convertList(todoItems));
    }

    public ArrayList<TodoItem> getTaskList() {
        return taskList.getTodoItems();
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

        // Add observable list data to the table
        // personTable.setItems(mainApp.getPersonData());
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
