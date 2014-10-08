package app;

import app.controllers.*;
import javafx.application.Application;
import javafx.stage.Stage;

import org.controlsfx.dialog.Dialogs;

public class Main extends Application {

    private Stage primaryStage;

    private CommandController commandController;
    private RootViewController rootViewController;

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("wat do?");
        this.primaryStage.setResizable(false);

        rootViewController = new RootViewController();
        rootViewController.setMainApp(this);
        rootViewController.initLayout(primaryStage);

        commandController = new CommandController();
        commandController.setMainApp(this);
        commandController.setTaskList(commandController.getTaskList());
        commandController.updateView();

        showInfoDialog("Welcome", "wat will you do today?");

        rootViewController.getInputField().requestFocus();
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

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public TaskListViewController getTaskListViewController() {
        return rootViewController.getTaskListViewController();
    }

    public CommandController getCommandController() {
        return commandController;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
