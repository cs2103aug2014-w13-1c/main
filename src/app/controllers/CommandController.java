package app.controllers;

import app.Main;
import app.helpers.CommandObject;
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
        ADD, DELETE, DISPLAY, CLEAR, EXIT, SEARCH, UPDATE, DONE,
        UNDONE, HELP, SETTINGS, SAVETO, INVALID, INVALID_DATE,
        UNDO, REDO
    }

    // Errors
    private final String ERROR_INVALID_DATE = "Error. Invalid Date\n";
    private final String ERROR_WRONG_COMMAND_FORMAT = "Command error.\n";
    
    private final String ERROR_SETTINGS_PARSE_FAILED = "Seems like there's a problem with your settings.json file.\nPlease modify it using a text editor or delete it.";
    private final String ERROR_PARSE_FAILED = "Seems like there's a problem with your watdo.json file.\nPlease modify it using a text editor or delete it.";
    private final String ERROR_SETTINGS_LOAD_FAILED = "Failed to load settings.json.\nPlease close the program and try again.";
    private final String ERROR_LOAD_FAILED = "Failed to load watdo.json.\nPlease close the program and try again.";
    private final String ERROR_UNKNOWN_MODEL_ERROR = "There has been an unexpected problem with our database.\nPlease close the program and try again.";
    
    // Class variables
    private static ActionController actionController;
    private static ModelManager modelManager;
    private static Main main;
    private static TaskController taskController;
    private static CommandParser commandParser;
    private static UndoController undoController;

    private static String modelManagerError;
    
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
        } else if (commandWord.equalsIgnoreCase("undo")) {
            return CommandType.UNDO;
        } else if (commandWord.equalsIgnoreCase("redo")) {
            return CommandType.REDO;
        } else {
            return CommandType.INVALID;
        }
    }

    protected String processCommand(CommandObject commandObject) {
        String commandWord = commandObject.getCommandWord();
        CommandType commandType = determineCommandType(commandWord);
        String feedback;
        switch (commandType) {
            case ADD :
                feedback = actionController.addNewLine(commandObject);
                resetTaskList();
                updateView();
                return feedback;
            case DISPLAY :
                feedback = actionController.display(commandObject);
                currentList = actionController.getReturnList();
                updateView(actionController.getReturnList());
                return feedback;
            case CLEAR :
                feedback = actionController.clear(commandObject);
                resetTaskList();
                updateView();
                return feedback;
            case DELETE :
                feedback = actionController.deleteEntry(commandObject, currentList);
                resetTaskList();
                updateView();
                return feedback;
            case SEARCH :
                feedback = actionController.search(commandObject);
                currentList = actionController.getReturnList();
                updateView(actionController.getReturnList());
                return feedback;
            case UPDATE :
                feedback = actionController.update(commandObject, currentList);
                resetTaskList();
                updateView();
                return feedback;
            case DONE :
                feedback = actionController.done(commandObject, currentList);
                resetTaskList();
                updateView();
                return feedback;
            case UNDONE :
                feedback = actionController.undone(commandObject, currentList);
                resetTaskList();
                updateView();
                return feedback;
            case HELP :
                feedback = actionController.help(commandObject);
                return feedback;
            case SETTINGS :
                feedback = actionController.settings(commandObject);
                return feedback;
            case SAVETO :
                feedback = actionController.changeSaveLocation(commandObject);
                undoController.clear();
                resetTaskList();
                updateView();
                return feedback;
            case EXIT :
                System.exit(0);
            case INVALID_DATE :
                feedback = notifyWithError(ERROR_INVALID_DATE);
                return feedback;
            case UNDO :
                feedback = actionController.undo(commandObject);
                resetTaskList();
                updateView();
                return feedback;
            case REDO :
                feedback = actionController.redo(commandObject);
                resetTaskList();
                updateView();
                return feedback;
            default :
                feedback = notifyWithError(ERROR_WRONG_COMMAND_FORMAT);
                return feedback;
        }
    }

    // CommandController public methods
    public CommandController() {
        commandParser = new CommandParser();
        try {
            modelManager = new ModelManager();
        } catch (IOException e) {
            switch(e.getMessage()) {
                case ModelManager.LOAD_SETTINGS_FAILED:
                    modelManagerError = ERROR_SETTINGS_LOAD_FAILED;
                    break;
                case ModelManager.SETTINGS_PARSE_FAILED:
                    modelManagerError = ERROR_SETTINGS_PARSE_FAILED;
                    break;
                case ModelManager.LOAD_FAILED:
                    modelManagerError = ERROR_LOAD_FAILED;
                    break;
                case ModelManager.PARSE_FAILED:
                    modelManagerError = ERROR_PARSE_FAILED;
                    break;
                default:
                    modelManagerError = ERROR_UNKNOWN_MODEL_ERROR;
                    break;
            }   
            LoggingService.getLogger().log(Level.SEVERE, "IOException: " + e.getMessage());
        }
        actionController = new ActionController(modelManager);
        actionController.setCommandController(this);
        undoController = UndoController.getUndoController();
    }

    public void parseCommand(String inputString) {
        printString("Parsing: \"" + inputString + "\"\n");
        CommandObject commandObject = commandParser.parseCommand(inputString);
        printString(processCommand(commandObject));
    }

    public ArrayList<Keyword> parseKeywords(String inputString) {
        return CommandParser.getKeywords(inputString);
    }

    public ObservableList<TodoItem> convertList(ArrayList<TodoItem> todoList) {
        ObservableList<TodoItem> taskData = FXCollections.observableArrayList(todoList);
        return taskData;
    }

    public void updateView() {
        main.getPrimaryStage().setTitle("wat do");
        main.getRootViewManager().getTaskListViewManager().updateView(convertList(currentList));
    }

    public void updateView(ArrayList<TodoItem> todoItems) {
        main.getRootViewManager().getTaskListViewManager().updateView(convertList(todoItems));
    }

    public static ArrayList<TodoItem> getTaskList() {
        if (modelManager == null) {
            main.showErrorDialog("FILE ERROR", modelManagerError);
            return new ArrayList<TodoItem>();
        }
        return modelManager.getTodoItemList();
    }

    public void resetTaskList() {
        currentList = taskController.getUndoneTasks();
    }

    public void setMainApp(Main main) {
        CommandController.main = main;
        actionController.setMainApp(main);
    }

    public void setTaskController(TaskController controller) {
        taskController = controller;
        actionController.setTaskController(taskController);
        resetTaskList();
    }

    /**
     * Okay to remove this method once switching to changeSettings is complete.
     */
    public void changeSaveLocation(String filePath) {
        String newInputString = "saveto ";
        newInputString = newInputString.concat(filePath);
        CommandObject commandObject = commandParser.parseCommand(newInputString);
        printString(processCommand(commandObject));
    }
    
    public void changeSettings(String filePath, Boolean randomColorsEnabled, Boolean notificationsEnabled) {
        String feedback = actionController.changeSettings(filePath, randomColorsEnabled, notificationsEnabled);
        undoController.clear();
        resetTaskList();
        updateView();
        printString(feedback);
    }

    public static String notifyWithError(String error) {
        main.showErrorNotification("Error", error);
        return error;
    }

    public static String notifyWithInfo(String message) {
        main.showInfoNotification("Information", message);
        return message;
    }

    public String getSaveDirectory() {
        return modelManager.getFileDirectory();
    }

    public Boolean areRandomColorsEnabled() {
        return modelManager.areRandomColorsEnabled();
    }
    
    public Boolean areNotificationsEnabled() {
        return modelManager.areNotificationsEnabled();
    }
    
    public UndoController getUndoController() {
        return undoController;
    }

    public ModelManager getModelManager() {
        return modelManager;
    }
}
