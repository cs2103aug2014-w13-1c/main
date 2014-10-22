package app.controllers;

import app.Main;
import app.helpers.Keyword;
import app.helpers.LoggingService;
import app.model.ModelManager;
import app.model.TodoItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;

/**
 * Class CommandController
 * 
 * This class is the main controller.
 * Skeleton based on jolly's CE2.
 *
 * @author ryan
 */

public class CommandController {
    protected enum CommandType {
        ADD, DELETE, DISPLAY, CLEAR, EXIT, SEARCH, UPDATE, DONE, UNDONE, HELP, SETTINGS, SAVETO, INVALID, INVALID_DATE,
    }

    // Errors
    private final String ERROR_INVALID_DATE = "Error. Invalid Date\n";
    private final String ERROR_WRONG_COMMAND_FORMAT = "Command error.\n";

    // Class variables
    ActionController action;
    private static ModelManager taskList;
    private static Main main;
    private ArrayList<TodoItem> currentList;

    // Print string methods
    protected void printString(String message) {
        System.out.print(message);
    }

    // Command processing methods
    protected CommandType determineCommandType(String commandWord) {
        if (commandWord.equalsIgnoreCase("add")) {
            return CommandType.ADD;
        } else if (commandWord.equalsIgnoreCase("delete")) {
            return CommandType.DELETE;
        } else if (commandWord.equalsIgnoreCase("display")) {
            return CommandType.DISPLAY;
        } else if (commandWord.equalsIgnoreCase("clear")) {
            return CommandType.CLEAR;
        } else if (commandWord.equalsIgnoreCase("exit")) {
            return CommandType.EXIT;
        } else if (commandWord.equalsIgnoreCase("search")) {
            return CommandType.SEARCH;
        } else if (commandWord.equalsIgnoreCase("update")) {
            return CommandType.UPDATE;
        } else if (commandWord.equalsIgnoreCase("done")) {
            return CommandType.DONE;
        } else if (commandWord.equalsIgnoreCase("undone")) {
            return CommandType.UNDONE;
        } else if (commandWord.equalsIgnoreCase("help")) {
            return CommandType.HELP;
        } else if (commandWord.equalsIgnoreCase("settings")) {
            return CommandType.SETTINGS;
        } else if (commandWord.equalsIgnoreCase("saveto")) {
            return CommandType.SAVETO;
        } else if (commandWord.equalsIgnoreCase("dateError")) {
            return CommandType.INVALID_DATE;
        } else {
            return CommandType.INVALID;
        }
    }

    protected String processCommand(CommandParser parsedCommand) {        
        String commandWord = parsedCommand.getCommandWord();
        CommandType commandType = determineCommandType(commandWord);
        String feedback;
        switch (commandType) {
            case ADD :
                feedback = action.addNewLine(parsedCommand);
                currentList = action.getCurrentList();
                taskList = action.getTaskList();
                resetTaskList();
                return feedback;
            case DISPLAY :
                feedback = action.display(parsedCommand);
                currentList = action.getCurrentList();
                taskList = action.getTaskList();
                updateView();
                return feedback;
            case CLEAR :
                feedback = action.clear(parsedCommand);
                currentList = action.getCurrentList();
                taskList = action.getTaskList();
                resetTaskList();
                return feedback;
            case DELETE :
                feedback = action.deleteEntry(parsedCommand);
                currentList = action.getCurrentList();
                taskList = action.getTaskList();
                resetTaskList();
                return feedback;
            case SEARCH :
                feedback = action.search(parsedCommand);
                currentList = action.getCurrentList();
                taskList = action.getTaskList();
                updateView();
                return feedback;
            case UPDATE :
                feedback = action.update(parsedCommand);
                currentList = action.getCurrentList();
                taskList = action.getTaskList();
                resetTaskList();
                return feedback;
            case DONE :
                feedback = action.done(parsedCommand);
                currentList = action.getCurrentList();
                taskList = action.getTaskList();
                return feedback;
            case UNDONE :
                feedback = action.undone(parsedCommand);
                currentList = action.getCurrentList();
                taskList = action.getTaskList();
                return feedback;
            case HELP :
                feedback = action.help(parsedCommand);
                return feedback;
            case SETTINGS :
                feedback = action.settings(parsedCommand);
                return feedback;
            case SAVETO :
                feedback = action.changeSaveLocation(parsedCommand);
                return feedback;
            case EXIT :
                showInfoDialog("Bye!");
                System.exit(0);
            case INVALID_DATE :
                feedback = showErrorDialog(ERROR_INVALID_DATE);
                return feedback;
            default :
                feedback = showErrorDialog(ERROR_WRONG_COMMAND_FORMAT);
                return feedback;
        }
    }

    // CommandController public methods
    public CommandController() {
        action = new ActionController();
        action.setMainApp(main);
        taskList = action.getTaskList();
        currentList = new ArrayList<TodoItem>();
    }

    public void parseCommand(String inputString) {
        printString("Parsing: \"" + inputString + "\"\n");
        CommandParser parsedCommand = new CommandParser(inputString);
        printString(processCommand(parsedCommand));
    }

    public ArrayList<Keyword> parseKeywords(String inputString) {
        return CommandParser.getKeywords(inputString);
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

    public void updateView() {
        main.getRootViewManager().getTaskListViewManager().updateView(convertList(getTaskList()));
    }

    public void updateView(ArrayList<TodoItem> todoItems) {
        main.getRootViewManager().getTaskListViewManager().updateView(convertList(todoItems));
    }

    public static ArrayList<TodoItem> getTaskList() {
        return taskList.getTodoItemList();
    }

    public void setTaskList(ArrayList<TodoItem> todoList) {
        currentList = todoList;
    }

    public void resetTaskList() {
        main.getPrimaryStage().setTitle("wat do");
        setTaskList(getTaskList());
    }

    public void changeSaveLocation(String filePath) {
        String newInputString = "saveto ";
        newInputString = newInputString.concat(filePath);
        CommandParser parsedCommand = new CommandParser(newInputString);
        printString(processCommand(parsedCommand));
    }

    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param main
     */
    public void setMainApp(Main main) {
        CommandController.main = main;
    }

    public static String showErrorDialog(String error) {
        main.showErrorDialog("Error", error);
        return error;
    }

    public static String showInfoDialog(String message) {
        main.showInfoDialog("Information", message);
        return message;
    }

    protected static ModelManager getModelManager() {
        return taskList;
    }
}
