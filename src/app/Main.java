package app;

import app.controllers.CommandController;
import app.controllers.TaskController;
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

    @Override
    public void start(Stage stage) throws Exception {
        LoggingService.getLogger().log(Level.INFO, "Launching app");

        createPrimaryStage(stage);
        initViewComponent();
        initControllerComponents();

        if (commandArguments.length == 0) {
            // Actual use case
            rootViewManager.setAndFocusInputField("");
        } else {
            // Testing
            for (String command : commandArguments) {
                rootViewManager.getInputFieldViewManager().checkCommandLengthAndExecute(command);
            }
            rootViewManager.getInputFieldViewManager().checkCommandLengthAndExecute("exit");
        }
    }

    private void createPrimaryStage(Stage stage) {
        primaryStage = stage;
        primaryStage.setTitle("wat do");
        primaryStage.setResizable(false);
    }

    private void initViewComponent() {
        rootViewManager = new RootViewManager();
        rootViewManager.setMainApp(this);
        rootViewManager.initLayout(primaryStage);
    }

    private void initControllerComponents() {
        commandController = new CommandController();
        taskController = taskController.getTaskController();
        commandController.setMainApp(this);
        taskController.setMainApp(this);
        commandController.setTaskController(taskController);
        commandController.updateView();
    }

    public void showInfoNotification(String title, String message) {
        // Actual use case.
        if (commandArguments.length == 0 && getCommandController().areNotificationsEnabled()) {
            Notifications.create().position(Pos.TOP_RIGHT).title(title).text(message).hideAfter(new Duration(1000)).showInformation();
        }
        // If false, currently in test mode so no dialogs are used.
    }

    public void showErrorNotification(String title, String error) {
        if (getCommandController().areNotificationsEnabled()) {
            Notifications.create().position(Pos.TOP_RIGHT).title(title).text(error).hideAfter(new Duration(1000)).showError();
        }
    }

    public void showErrorDialog(String title, String error) {
        Dialogs.create()
                .owner(primaryStage)
                .title(title)
                .masthead("Error")
                .message(error).showError();
    }

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

    public static void main(String[] args) {
        commandArguments = args;
        launch(args);
    }
}
