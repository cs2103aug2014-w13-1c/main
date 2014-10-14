package app;

import app.controllers.*;
import app.helpers.LoggingService;
import app.viewcontrollers.RootViewController;
import javafx.application.Application;
import javafx.stage.Stage;

import org.controlsfx.dialog.Dialogs;

import java.net.URL;
import java.util.logging.Level;

public class Main extends Application {

    private Stage primaryStage;

    private CommandController commandController;
    private RootViewController rootViewController;

    @Override
    public void start(Stage stage) throws Exception {
        LoggingService.getLogger().log(Level.INFO, "Launching app");

        createPrimaryStage(stage);
        initViewComponent();
        initControllerComponent();

        rootViewController.setAndFocusInputField("");
    }

    private void createPrimaryStage(Stage stage) {
        primaryStage = stage;
        primaryStage.setTitle("wat do");
        primaryStage.setResizable(false);
    }

    private void initViewComponent() {
        rootViewController = new RootViewController();
        rootViewController.setMainApp(this);
        rootViewController.initLayout(primaryStage);
    }

    private void initControllerComponent() {
        commandController = new CommandController();
        commandController.setMainApp(this);
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

    public RootViewController getRootViewController() {
        return rootViewController;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
