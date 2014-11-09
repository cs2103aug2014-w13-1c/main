package app;

//author A0111764L
import app.controllers.CommandController;
import app.controllers.TaskController;
import app.helpers.InvalidInputException;
import app.helpers.LoggingService;
import app.viewmanagers.RootViewManager;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import org.controlsfx.dialog.Dialogs;

import java.net.URL;
import java.util.logging.Level;

public class Main extends Application {

    private static String[] commandArguments;
    
    private Stage primaryStage;

    private CommandController commandController;
    private TaskController taskController;
    private RootViewManager rootViewManager;

    /**
     * Entry method.
     * NOTE: The order of view and controller component initialization
     * is important, and should not be swapped.
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

    private void waitForUserInput() {
        rootViewManager.setAndFocusInputField("");
    }

    private void sendCommandsToInput() throws InvalidInputException {
        for (String command : commandArguments) {
            rootViewManager.getInputFieldViewManager().checkCommandLengthAndExecute(command);
        }
        rootViewManager.getInputFieldViewManager().checkCommandLengthAndExecute("exit");
    }

    /**
     * Creates the Stage object where all GUI components will live in.
     * @param stage
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
        taskController = taskController.getTaskController();
        commandController.setMainApp(this);
        taskController.setMainApp(this);
        commandController.setTaskController(taskController);
        commandController.updateView();
    }

    /**
     * Using ControlsFX's Notification API, display a notification
     * with an Info icon at the top right hand corner of the screen.
     * @param title
     * @param message
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
     * @param title
     * @param error
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
     * @param title
     * @param error
     */
    public void showErrorDialog(String title, String error) {
        Dialogs.create()
                .owner(primaryStage)
                .title(title)
                .masthead("Error")
                .message(error)
                .showError();
    }

    /**
     * Convert a relative path string to an URL. This is used for
     * stylesheets and resources (icons).
     * @param relativePath
     * @return
     */
    public URL getResourceURL(String relativePath) {
        return this.getClass().getResource(relativePath);
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public CommandController getCommandController() {
        return commandController;
    }

    public TaskController getTaskController() {
        return taskController;
    }

    public RootViewManager getRootViewManager() {
        return rootViewManager;
    }

    /**
     * Entry point of the application.
     * Arguments can be passed in through this method, and they
     * are used for integration testing.
     * @param args
     */
    public static void main(String[] args) {
        commandArguments = args;
        launch(args);
    }

    private boolean isTesting() {
        return commandArguments.length > 0;
    }
}