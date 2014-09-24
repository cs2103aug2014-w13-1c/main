package app;

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
            ListView taskListView = loader.load();

            rootLayout.setCenter(taskListView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showInputField() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("views/InputField.fxml"));
            TextField inputField = loader.load();

            rootLayout.setBottom(inputField);
        } catch (IOException e) {
           e.printStackTrace();
        }
    }

    private void showSidebar() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("views/Sidebar.fxml"));
            VBox sidebar = loader.load();

            rootLayout.setLeft(sidebar);
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

    public static void main(String[] args) {
        launch(args);
    }
}
