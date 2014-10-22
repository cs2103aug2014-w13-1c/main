package app.viewmanagers;

import app.Main;
import app.helpers.LoggingService;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import org.fxmisc.richtext.StyleClassedTextArea;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

/**
 * Created by jin on 8/10/14.
 */
public class RootViewManager {

    private Main mainApp;
    private StackPane rootLayout;
    private Pane settingsView;
    private Pane helpView;
    private BorderPane borderPane;
    private StyleClassedTextArea inputField;
    private ListView taskListView;

    private TaskListViewManager taskListViewManager;
    private SettingsViewManager settingsViewManager;
    private HelpViewManager helpViewManager;

    public void initLayout(Stage primaryStage) {
        LoggingService.getLogger().log(Level.INFO, "Initializing layout.");

        try {
            this.initRootLayout(primaryStage);
            this.initSettingsView();
            this.initHelpView();
            this.showSidebar();
            this.showInputField();
            this.showTaskListView();
        } catch (IOException e) {
            LoggingService.getLogger().log(Level.SEVERE, e.getMessage());
        }

    }

    private void initRootLayout(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(mainApp.getResourceURL("views/RootView.fxml"));
        rootLayout = loader.load();
        borderPane = (BorderPane) rootLayout.getChildren().get(0);

        Scene scene = new Scene(rootLayout);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void initSettingsView() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(mainApp.getResourceURL("views/SettingsView.fxml"));
        settingsView = loader.load();

        settingsViewManager = loader.getController();
        settingsViewManager.setRootViewManager(this);

        rootLayout.getChildren().add(settingsView);
        settingsView.toBack();
    }

    private void initHelpView() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(mainApp.getResourceURL("views/HelpView.fxml"));
        helpView = loader.load();

        helpViewManager = loader.getController();
        helpViewManager.setRootViewManager(this);

        rootLayout.getChildren().add(helpView);
        helpView.toBack();
    }

    private void showTaskListView() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(mainApp.getResourceURL("views/TaskListView.fxml"));
        taskListView = loader.load();
        taskListView.getStylesheets().add("app/stylesheets/taskList.css");
        taskListView.getStyleClass().add("task-list");

        taskListViewManager = loader.getController();
        taskListViewManager.setRootViewManager(this);

        borderPane.setCenter(taskListView);
    }

    private void showInputField() {
        InputFieldViewManager inputFieldViewManager = new InputFieldViewManager();
        inputFieldViewManager.setRootViewManager(this);
        inputField = inputFieldViewManager.getInputField();

        borderPane.setBottom(new StackPane(inputField));
    }

    private void showSidebar() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(mainApp.getResourceURL("views/SidebarView.fxml"));
        VBox sidebar = loader.load();
        sidebar.getStylesheets().add("app/stylesheets/sidebar.css");
        sidebar.getStyleClass().add("sidebar");

        SidebarViewManager controller = loader.getController();
        controller.setRootViewManager(this);

        borderPane.setLeft(sidebar);
    }

    // Getters and Setters

    public void closeSettings(File filePath) {
        if (filePath != null) {
            getMainApp().getCommandController().changeSaveLocation(filePath.toString());
        }
        settingsView.toBack();
        settingsViewManager.cancelFocusOnButton();
        inputField.requestFocus();
    }

    public void openSettings() {
        settingsView.toFront();
        settingsViewManager.focusOnButton();
    }

    public void openHelp() {
        helpView.toFront();
        helpViewManager.focusOnButton();
    }

    public void closeHelp() {
        helpView.toBack();
        helpViewManager.cancelFocusOnButton();
        inputField.requestFocus();
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

    public TaskListViewManager getTaskListViewManager() {
        return taskListViewManager;
    }

    public void setAndFocusInputField(String text) {
//        mainApp.getPrimaryStage().hide();
//        mainApp.getPrimaryStage().show();

        mainApp.getPrimaryStage().requestFocus();
//        rootLayout.requestFocus();
//        borderPane.requestFocus();

        inputField.replaceText(text);
        inputField.positionCaret(text.length());
        inputField.requestFocus();
    }

}
