package app;

import app.controllers.CommandController;
import app.controllers.TaskController;
import app.helpers.LoggingService;
import app.viewmanagers.RootViewManager;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.stage.Stage;
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
        commandController.setTaskList(commandController.getTaskList());
        commandController.updateView();
    }

    public void showInfoNotification(String title, String message) {
        // Actual use case.
        if (commandArguments.length == 0) {
            Notifications.create().position(Pos.TOP_RIGHT).title(title).text(message).showInformation();
        }
        // If false, currently in test mode so no dialogs are used.
    }

    public void showErrorNotification(String title, String error) {
        Notifications.create().position(Pos.TOP_RIGHT).title(title).text(error).showError();
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
