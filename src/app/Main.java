package app;

import app.controllers.CommandController;
import app.controllers.InputFieldController;
import app.controllers.SidebarController;
import app.controllers.TaskListViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import org.controlsfx.dialog.Dialogs;

import org.fxmisc.richtext.InlineCssTextArea;

import java.io.IOException;

public class Main extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
    private InlineCssTextArea inputField;
    private ListView taskListView;
    private CommandController commandController;
    private TaskListViewController taskListViewController;

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("wat do?");
        this.primaryStage.setResizable(false);

        initRootLayout();
        showSidebar();
        showInputField();
        showTaskListView();

        commandController = new CommandController();
        commandController.setMainApp(this);
        commandController.updateView();

        Dialogs.create()
                .owner(primaryStage)
                .title("Welcome")
                .masthead(null)
                .message("wat will you do today?")
                .showInformation();

        inputField.requestFocus();
    }

    private void showTaskListView() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("views/TaskListView.fxml"));
            taskListView = loader.load();

            rootLayout.setCenter(taskListView);
            taskListViewController = loader.getController();
            taskListViewController.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showInputField() {
        InputFieldController inputFieldController = new InputFieldController();
        inputFieldController.setMainApp(this);
        inputField = inputFieldController.getInputField();
        rootLayout.setBottom(new StackPane(inputField));
    }

    private void showSidebar() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("views/Sidebar.fxml"));
            VBox sidebar = loader.load();
            sidebar.getStylesheets().add("app/stylesheets/sidebar.css");
            sidebar.getStyleClass().add("sidebar");

            rootLayout.setLeft(sidebar);

            SidebarController controller = loader.getController();
            controller.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("views/RootLayout.fxml"));
            rootLayout = loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public InlineCssTextArea getInputField() { return inputField; }
    public void setAndFocusInputField(String text) {
        inputField.requestFocus();
        inputField.replaceText(text);
        inputField.positionCaret(text.length());
    }

    public TaskListViewController getTaskListViewController() { return taskListViewController; }

    public CommandController getCommandController() { return commandController; }

    public static void main(String[] args) {
        launch(args);
    }
}
