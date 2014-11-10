//@author A0111764L

package app;

import app.controllers.CommandController;
import app.controllers.TaskController;
import app.controllers.UndoController;
import app.exceptions.InvalidInputException;
import app.services.LoggingService;
import app.viewmanagers.RootViewManager;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.util.Duration;

import org.controlsfx.control.Notifications;
import org.controlsfx.dialog.Dialogs;

import java.net.URL;
import java.util.logging.Level;

/**
 * This is the entry point of the application. By default, if there are no
 * command arguments passed into this, the application will launch normally.
 * If there are command arguments, the application will launch as an
 * integration test with the command arguments simulated as user inputs.
 *
 * Main also acts as the bridge between the View and Controller components by
 * providing getters.
 */
public class Main extends Application {

    private static String[] commandArguments;
    
    private Stage primaryStage;

    private CommandController commandController;
    private TaskController taskController;
    private UndoController undoController;
    private RootViewManager rootViewManager;

    /**
     * Entry method.
     * NOTE: The order of view and controller component initialization
     * is important, and should not be swapped.
     * @param stage The Stage object which the JavaFX application lives in.
     * @throws Exception Only for integration tests. If the input is invalid, throw an InvalidInputException.
     */
    @Override
    public void start(Stage stage) throws Exception {
        LoggingService.getLogger().log(Level.INFO, "Launching app");

        createPrimaryStage(stage);

        initViewComponent();

        initControllerComponents();

        if (isTesting()) {
            sendCommandsToInput();
        } else {
            waitForUserInput();
        }
    }

    /**
     * Focus on the input field. Called at the end after all components have been initialized.
     */
    private void waitForUserInput() {
        rootViewManager.setAndFocusInputField("");
    }

    /**
     * Manually send each command to input field and execute them as if the user pressed enter.
     * This simulates user actions.
     * @throws InvalidInputException Throw InvalidInputException if the command is invalid.
     */
    private void sendCommandsToInput() throws InvalidInputException {
        for (String command : commandArguments) {
            rootViewManager.getInputFieldViewManager().checkCommandLengthAndExecute(command);
        }
    }

    /**
     * Apply title and resizable settings for the Stage object.
     * @param stage The The Stage object which the JavaFX application lives in.
     */
    private void createPrimaryStage(Stage stage) {
        primaryStage = stage;
        primaryStage.setTitle("wat do");
        primaryStage.setResizable(false);
    }

    /**
     * Initialize the root layout, a StackPane.
     */
    private void initViewComponent() {
        rootViewManager = new RootViewManager();
        rootViewManager.setMainApp(this);
        rootViewManager.initLayout(primaryStage);
    }

    /**
     * Initializes components responsible for handling commands
     * and managing of tasks.
     */
    private void initControllerComponents() {
        commandController = new CommandController();
        taskController = TaskController.getTaskController();
        undoController = UndoController.getUndoController();
        commandController.setMainApp(this);
        taskController.setMainApp(this);
        commandController.setTaskController(taskController);
        commandController.setUndoController(undoController);
        commandController.updateView();
    }

    /**
     * Using ControlsFX's Notification API, display a notification
     * with an Info icon at the top right hand corner of the screen.
     * @param title The title of the notification.
     * @param message The message to be shown.
     */
    public void showInfoNotification(String title, String message) {
        // Actual use case.
        if (!isTesting() && getCommandController().areNotificationsEnabled()) {
            Notifications
                    .create()
                    .position(Pos.TOP_RIGHT)
                    .title(title)
                    .text(message)
                    .hideAfter(new Duration(1500))
                    .showInformation();
        }
        // If false, currently in test mode so no dialogs are used.
    }

    /**
     * Using ControlsFX's Notification API, display a notification
     * with an Error icon at the top right hand corner of the screen.
     * @param title The title of the notification.
     * @param error The error message to be shown.
     */
    public void showErrorNotification(String title, String error) {
        if (getCommandController().areNotificationsEnabled()) {
            Notifications
                    .create()
                    .position(Pos.TOP_RIGHT)
                    .title(title)
                    .text(error)
                    .hideAfter(new Duration(1500))
                    .showError();
        }
    }

    /**
     * Using ControlsFX's Dialog API, display a dialog with an Error
     * icon to the user.
     * @param title The title of the dialog.
     * @param error The error message to be shown.
     */
    public void showErrorDialog(String title, String error) {
        if (!isTesting()) {
            Dialogs.create()
                .owner(primaryStage)
                .title(title)
                .masthead("Error")
                .message(error)
                .showError();
        } // If in test mode, don't show an error dialog.
    }

    /**
     * Convert a relative path string to an URL. This is used for
     * stylesheets and resources (icons).
     * @param relativePath The relative path from the src location to the resource.
     * @return An URL object containing the path to the resource.
     */
    public URL getResourceURL(String relativePath) {
        return this.getClass().getResource(relativePath);
    }

    /**
     * Getter for the Stage object
     * @return The Stage object
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * Getter for the CommandController component
     * @return The instantiated CommandController object.
     */
    public CommandController getCommandController() {
        return commandController;
    }

    /**
     * Getter for the TaskController component
     * @return The instantiated TaskController object.
     */
    public TaskController getTaskController() {
        return taskController;
    }

    /**
     * Getter for the RootViewManager component
     * @return The instantiated RootViewManager object.
     */
    public RootViewManager getRootViewManager() {
        return rootViewManager;
    }

    /**
     * Entry point of the application.
     * Arguments can be passed in through this method, and they
     * are used for integration testing.
     * @param args (For integration testing) Simulated user command inputs in a String array.
     */
    public static void main(String[] args) {
        commandArguments = args;
        launch(args);
    }

    /**
     * Checks if the commandArguments array is empty. If it's empty, it means
     * that the application is not under test, and should be launched normally.
     * @return True if application is under testing.
     */
    private boolean isTesting() {
        return commandArguments.length > 0;
    }
}
