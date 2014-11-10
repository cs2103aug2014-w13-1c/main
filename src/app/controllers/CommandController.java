package app.controllers;
//@author A0114914L

import app.Main;
import app.helpers.CommandObject;
import app.helpers.Keyword;
import app.services.ParsingService;
import app.services.LoggingService;
import app.model.ModelManager;
import app.model.TodoItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;


/**
 * This class is the main controller. It is called by the Main to create the other controllers (ActionController,
 * TaskController and UndoController) and initialise ModelManager.
 * Skeleton based on jolly's CE2.
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
    private static ParsingService parsingService;
    private static UndoController undoController;

    private static String modelManagerError;
    
    private ArrayList<TodoItem> currentList;
    
    /**
     * Method for printing message.
     * 
     * @param message
     */
    protected void printString(String message) {
        System.out.print(message);
    }

    /**
     * This method will take in the command word word and will return an enum of different command types.
     * 
     * @param commandWord
     * @return CommandType an enum of different command types
     */
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

    /**
     * This method will take in a CommandObject and get the CommandType from determineCommandType method.
     * It will do a simple switch logic, determined by the command word type and call the correct method from
     * ActionController. It will return a feedback string that will be displayed by the display later on.
     * 
     * @param commandObject
     * @return feedback
     */
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
    /**
     * Constructor for CommandController. It will also initialise CommandParser, ModelManager, ActionController,
     * taskController and UndoController. During initialisation of ModelManager, it will catch an IOException 
     * from ModelManager and do the necessary error catching depending on the type of the error. It will then 
     * pass the model manager during creation of ActionController and will set the main app for 
     * ActionController.
     */
    public CommandController() {
        parsingService = new ParsingService();
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
    }

    /**
     * This method will be called by the InputFieldViewManager to start parsing the input string typed by the
     * user. It will also call the CommandParser method to change the input string into a CommandObject.
     * It will then print the result for debugging purpose. The result is not shown in the user interface.
     * 
     * @param inputString
     */
    public void parseCommand(String inputString) {
        printString("Parsing: \"" + inputString + "\"\n");
        CommandObject commandObject = parsingService.parseCommand(inputString);
        printString(processCommand(commandObject));
    }

    /**
     * This method is called by the InputFieldViewManager to detect the keywords in the input string.
     * The input string will be passed by the InputFieldViewManager and will be passed to CommandParser to get
     * the keywords. It will return an ArrayList of Keyword.
     * 
     * @param inputString
     * @return ArrayList<Keyword> an ArrayList of Keyword
     */
    public ArrayList<Keyword> parseKeywords(String inputString) {
        return ParsingService.getKeywords(inputString);
    }

    /**
     * This method is used to convert an ArrayList of TodoItem into an ObservableList of TodoItem.
     * It uses javafx library to do the conversion.
     * 
     * @param todoList
     * @return ObservableList<TodoItem> an ObservableList of TodoItem
     */
    public ObservableList<TodoItem> convertList(ArrayList<TodoItem> todoList) {
        return FXCollections.observableArrayList(todoList);
    }

    /**
     * This method will update the view (user interface) with the list of current todo items to be shown to the
     * user. This method will be called by the processCommand method to update the view after "updating" (CRUD,
     * sort, search) the todo items.
     */
    public void updateView() {
        updateTitle();
        main.getRootViewManager().getTaskListViewManager().updateView(convertList(currentList));
    }

    /**
     * An overloading method for updateView. It will take in an ArrayList<TodoItem> and view that ArrayList in
     * the view.
     * 
     * @param todoItems an ArrayList of TodoItem
     */
    public void updateView(ArrayList<TodoItem> todoItems) {
        updateTitle();
        main.getRootViewManager().getTaskListViewManager().updateView(convertList(todoItems));
    }

    /**
     * This method will update the title bar of the view accordingly depending on the display type which
     * can be retrieved from the TaskController.
     */
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
                main.getRootViewManager().getTitleBarViewManager().setSortStyle(TaskController.SortingStyle.TASKNAME_ENDDATE);
                break;
            case 1:
                main.getRootViewManager().getTitleBarViewManager().setSortStyle(TaskController.SortingStyle.STARTDATE_PRIORITY);
                break;
            case 2:
                main.getRootViewManager().getTitleBarViewManager().setSortStyle(TaskController.SortingStyle.ENDDATE_PRIORITY);
                break;
            case 3:
                main.getRootViewManager().getTitleBarViewManager().setSortStyle(TaskController.SortingStyle.PRIORITY_ENDDATE);
                break;
        }
    }

    /**
     * This method will get the ArrayList<TodoItem> from the ModelManager.
     * 
     * @return ArrayList<TodoItem>
     */
    public static ArrayList<TodoItem> getTaskList() {
        if (modelManager == null) {
            main.showErrorDialog("FILE ERROR", modelManagerError);
            return new ArrayList<TodoItem>();
        }
        return modelManager.getTodoItemList();
    }

    /**
     * This method will update the currentList with the undone TodoItem retrived from TaskController.
     */
    public void resetTaskList() {
        currentList = taskController.getUndoneTasks();
    }

    /**
     * This method is called by Main to set the main app of this class.
     * 
     * @param main
     */
    public void setMainApp(Main main) {
        CommandController.main = main;
    }

    //@author A0111987X
    /**
     * This method is called by Main to set the TaskController of this class.
     * @param controller
     */
    public void setTaskController(TaskController controller) {
        taskController = controller;
        actionController.setTaskController(taskController);
        resetTaskList();
    }
    
    //@author A0116703N
    /**
     * Sets the undoController in use. Called by Main, or any tester methods.
     * 
     * @param controller The undoController to be used by this controller.
     */
    public void setUndoController(UndoController controller) {
        undoController = controller;
        actionController.setUndoController(undoController);
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

    //@author A0114914L
    /**
     * This method will call the Main to show an error notification if Main is not null.
     * It will return the error string.
     * 
     * @param error
     * @return error
     */
    public static String notifyWithError(String error) {
        if (main != null) {
            main.showErrorNotification("Error", error);
        }
        return error;
    }

    /**
     * This method will call the Main to show an info notication if Main is not null.
     * It will return the message string.
     * 
     * @param message
     * @return message
     */
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
    /**
     * @return The current setting for displaying tasks in random colors
     */
    public Boolean areRandomColorsEnabled() {
        return modelManager.areRandomColorsEnabled();
    }
    
    /**
     * @return The current setting for whether notifications are displayed
     */
    public Boolean areNotificationsEnabled() {
        try {
            return modelManager.areNotificationsEnabled();
        } catch (NullPointerException e) {
            return false;
        }
    }
    
    //@author A0114914L
    /**
     * Getter for undoController.
     * 
     * @return undoController
     */
    public UndoController getUndoController() {
        return undoController;
    }

    /**
     * Getter for modelManager.
     * 
     * @return modelManager
     */
    public ModelManager getModelManager() {
        return modelManager;
    }
    
    //@author A0116703N
    /**
     * Getter for actionController.
     * 
     * @return actionController
     */
    public ActionController getActionController() {
        return actionController;
    }
    
    /**
     * Invocation to open the settings window.
     */
    public void openSettings() {
        main.getRootViewManager().openSettings();
    }
    
    /**
     * Invocation to open the settings window.
     */
    public void openHelp() {
        main.getRootViewManager().openHelp();
    }
}
