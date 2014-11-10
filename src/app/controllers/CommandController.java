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
 * This class is the main controller.
 * Skeleton based on jolly's CE2.
 *
 * @author ryan
 */

public class CommandController {
    protected enum CommandType {
        ADD, DELETE, DISPLAY, CLEAR, EXIT, SORT, SEARCH, UPDATE,
        DONE, UNDONE, HELP, SETTINGS, SAVETO, INVALID, INVALID_DATE,
        UNDO, REDO
    }

    // Errors
    private final String ERROR_INVALID_DATE = "Error. Invalid Date\n";
    private final String ERROR_INVALID_COMMAND_WORD = "Error. Unidentified command word.\n";
    
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
        } else if (commandWord.equalsIgnoreCase("sort")) {
            return CommandType.SORT;
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
                feedback = actionController.add(commandObject);
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
                feedback = actionController.delete(commandObject, currentList);
                resetTaskList();
                updateView();
                return feedback;
            case SORT :
                feedback = actionController.sort(commandObject);
                resetTaskList();
                updateView();
                return feedback;
            case SEARCH :
                feedback = actionController.search(commandObject);
                currentList = actionController.getReturnList();
                if (currentList.isEmpty()) {
                    taskController.setDisplayType(TaskController.DisplayType.UNDONE);
                    resetTaskList();
                    updateView();
                } else {
                    main.getRootViewManager().getTitleBarViewManager().setTitle("Search results for: \"" +
                                                                                commandObject.getInputString().substring(7) + "\"");
                    updateView(actionController.getReturnList());
                }
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
                feedback = notifyWithError(ERROR_INVALID_COMMAND_WORD);
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
        return FXCollections.observableArrayList(todoList);
    }

    public void updateView() {
        updateTitle();
        main.getRootViewManager().getTaskListViewManager().updateView(convertList(currentList));
    }

    public void updateView(ArrayList<TodoItem> todoItems) {
        updateTitle();
        main.getRootViewManager().getTaskListViewManager().updateView(convertList(todoItems));
    }

    private void updateTitle() {
        switch (taskController.getDisplayType()) {
            case ALL:
                main.getRootViewManager().getTitleBarViewManager().setSortControlsVisible(true);
                main.getRootViewManager().getTitleBarViewManager().setTitle("All tasks");
                break;
            case DONE:
                main.getRootViewManager().getTitleBarViewManager().setSortControlsVisible(true);
                main.getRootViewManager().getTitleBarViewManager().setTitle("Done tasks");
                break;
            case UNDONE:
                main.getRootViewManager().getTitleBarViewManager().setSortControlsVisible(true);
                main.getRootViewManager().getTitleBarViewManager().setTitle("Undone tasks");
                break;
            case OVERDUE:
                main.getRootViewManager().getTitleBarViewManager().setSortControlsVisible(true);
                main.getRootViewManager().getTitleBarViewManager().setTitle("Overdue tasks");
                break;
            case SEARCH:
                main.getRootViewManager().getTitleBarViewManager().setSortControlsVisible(false);
                break;
        }
        
        switch (taskController.getSortingStyle()) {
            case 0:
                main.getRootViewManager().getTitleBarViewManager().setSortStyleByIndex(0);
                break;
            case 1:
                main.getRootViewManager().getTitleBarViewManager().setSortStyleByIndex(1);
                break;
            case 2:
                main.getRootViewManager().getTitleBarViewManager().setSortStyleByIndex(2);
                break;
            case 3:
                main.getRootViewManager().getTitleBarViewManager().setSortStyleByIndex(3);
                break;
        }
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
    
    //@author A0116703N
    /**
     * Calls the changeSettings method in modelManager. Mostly used by the SettingsView
     * to change the user's settings from the settings window. 
     * 
     * @param filePath The new file directory to be used. 
     * @param randomColorsEnabled The new random color display setting.
     * @param notificationsEnabled The new notification display setting.
     */
    public void changeSettings(String filePath, Boolean randomColorsEnabled, Boolean notificationsEnabled) {
        String feedback = actionController.changeSettings(filePath, randomColorsEnabled, notificationsEnabled);
        
        // The data in modelManager has changed, so we refresh the data to be displayed and clear the undo stack.
        undoController.clear();
        resetTaskList();
        updateView();
        
        // Finally do a System.out.println.
        printString(feedback);
    }

    public static String notifyWithError(String error) {
        main.showErrorNotification("Error", error);
        return error;
    }

    public static String notifyWithInfo(String message) {
        if (main != null) {
            main.showInfoNotification("Information", message);
        }
        return message;
    }

    //@author A0111987X
    /**
     * Gets the current file directory from ModelManager.
     *
     * @return current file directory
     */
    public String getSaveDirectory() {
        return modelManager.getFileDirectory();
    }

    //@author A0116703N
    public Boolean areRandomColorsEnabled() {
        return modelManager.areRandomColorsEnabled();
    }
    
    public Boolean areNotificationsEnabled() {
        try {
            return modelManager.areNotificationsEnabled();
        } catch (NullPointerException e) {
            return false;
        }
    }
    
    public UndoController getUndoController() {
        return undoController;
    }

    public ModelManager getModelManager() {
        return modelManager;
    }
}
