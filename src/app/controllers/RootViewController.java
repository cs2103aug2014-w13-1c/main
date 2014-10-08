package app.controllers;

import app.Main;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.fxmisc.richtext.StyleClassedTextArea;

import javax.swing.border.Border;
import java.io.IOException;

/**
 * Created by jin on 8/10/14.
 */
public class RootViewController {

    private Main mainApp;
    private StackPane rootLayout;
    private BorderPane borderPane;
    private StyleClassedTextArea inputField;
    private ListView taskListView;
    private TaskListViewController taskListViewController;

    public void initLayout(Stage primaryStage) {
        this.initRootLayout(primaryStage);
        this.showSidebar();
        this.showInputField();
        this.showTaskListView();
    }

    private void initRootLayout(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(mainApp.getClass().getResource("views/RootLayout.fxml"));
            rootLayout = loader.load();
            borderPane = (BorderPane) rootLayout.getChildren().get(0);

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showTaskListView() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(mainApp.getClass().getResource("views/TaskListView.fxml"));
            taskListView = loader.load();
            taskListView.getStylesheets().add("app/stylesheets/taskList.css");
            taskListView.getStyleClass().add("task-list");

            borderPane.setCenter(taskListView);
            taskListViewController = loader.getController();
            taskListViewController.setRootViewController(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showInputField() {
        InputFieldController inputFieldController = new InputFieldController();
        inputFieldController.setRootViewController(this);
        inputField = inputFieldController.getInputField();
        borderPane.setBottom(new StackPane(inputField));
    }

    private void showSidebar() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(mainApp.getClass().getResource("views/Sidebar.fxml"));
            VBox sidebar = loader.load();
            sidebar.getStylesheets().add("app/stylesheets/sidebar.css");
            sidebar.getStyleClass().add("sidebar");

            borderPane.setLeft(sidebar);

            SidebarController controller = loader.getController();
            controller.setRootViewController(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }

    public Main getMainApp() {
        return mainApp;
    }

    public StyleClassedTextArea getInputField() {
        return inputField;
    }

    public TaskListViewController getTaskListViewController() {
        return taskListViewController;
    }

    public void setAndFocusInputField(String text) {
        inputField.replaceText(text);
        inputField.positionCaret(text.length());
        inputField.requestFocus();
    }
}
