package app;

import app.controllers.CommandController;
import app.controllers.TaskController;
import app.helpers.LoggingService;
import app.viewmanagers.RootViewManager;
import javafx.application.Application;
import javafx.stage.Stage;
import org.controlsfx.dialog.Dialogs;

import java.net.URL;
import java.util.logging.Level;

public class Main extends Application {

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

        rootViewManager.setAndFocusInputField("");
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

    public void showInfoDialog(String title, String message) {
        Dialogs.create()
                .owner(primaryStage)
                .title(title)
                .masthead(null)
                .message(message)
                .showInformation();
    }

    public void showErrorDialog(String title, String error) {
        Dialogs.create()
                .owner(primaryStage)
                .title(title)
                .masthead(null)
                .message(error)
                .showError();
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
        launch(args);
    }
}
