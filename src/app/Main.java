package app;

import app.controllers.InputFieldController;
import app.controllers.SidebarController;
import app.controllers.TaskListViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;

    private TextField inputField;
    private ListView taskListView;

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("wat do?");

        initRootLayout();
        showSidebar();
        showInputField();
        showTaskListView();
    }

    private void showTaskListView() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("views/TaskListView.fxml"));
            taskListView = loader.load();

            rootLayout.setCenter(taskListView);
            TaskListViewController controller = loader.getController();
            controller.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showInputField() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("views/InputField.fxml"));
            inputField = loader.load();
            inputField.getStylesheets().add(getClass().getResource("stylesheets/TextField.css").toExternalForm());
            inputField.getStyleClass().add("text-field");

            rootLayout.setBottom(inputField);

            InputFieldController controller = loader.getController();
            controller.setMainApp(this);
        } catch (IOException e) {
           e.printStackTrace();
        }
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

    public TextField getInputField() {
        return inputField;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
