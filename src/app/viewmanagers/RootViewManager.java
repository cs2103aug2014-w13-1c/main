//@author A0111764L
/* RootView.fxml

<?xml version="1.0" encoding="UTF-8"?>

<?import javafx.scene.layout.BorderPane?>
<?import javafx.scene.layout.StackPane?>
<StackPane maxHeight="-Infinity" maxWidth="-Infinity" minHeight="700.0" minWidth="1000" prefHeight="700.0"
           prefWidth="1000.0" xmlns="http://javafx.com/javafx/8">
    <BorderPane maxHeight="-Infinity" maxWidth="-Infinity" minHeight="700.0" minWidth="1000" prefHeight="700.0"
                prefWidth="1000.0" xmlns="http://javafx.com/javafx/8">
    </BorderPane>
</StackPane>

 */
package app.viewmanagers;

import app.Main;
import app.services.LoggingService;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.fxmisc.richtext.StyleClassedTextArea;

import java.io.IOException;
import java.util.logging.Level;

/**
 * This is the root view manager of all view managers/components.
 *
 * The app layout is structured in StackPane, which allows for z-indexes.
 * Components are able to hide between one and another.
 *
 * A borderPane serves as the first child of the StackPane. The other children
 * are panes representing the Help and Settings views.
 */
public class RootViewManager {

    private Main mainApp;
    private StackPane rootLayout;
    private Pane settingsView;
    private Pane helpView;
    private BorderPane borderPane;
    private StyleClassedTextArea inputField;
    private TaskListViewManager taskListViewManager;
    private SettingsViewManager settingsViewManager;
    private HelpViewManager helpViewManager;
    private InputFieldViewManager inputFieldViewManager;
    private TitleBarViewManager titleBarViewManager;
    private SidebarViewManager sidebarViewManager;

    /**
     * Initializes all view components. The order is important, and shouldn't
     * be changed.
     * @param primaryStage The Stage object where all components live in.
     */
    public void initLayout(Stage primaryStage) {
        LoggingService.getLogger().log(Level.INFO, "Initializing layout.");
        try {
            this.initRootLayout(primaryStage);
            this.initSettingsView();
            this.initHelpView();
            this.showTitleBarView();
            this.showSidebar();
            this.showInputField();
            this.showTaskListView();
        } catch (IOException e) {
            LoggingService.getLogger().log(Level.SEVERE, e.getMessage());
        }
    }

    /**
     * This is the first component to be initialized, as all other
     * components live within this.
     * @param primaryStage The Stage object where all components live in.
     * @throws IOException Throw IO Exception if the FXML file cannot be found/loaded.
     */
    private void initRootLayout(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(mainApp.getResourceURL("views/RootView.fxml"));
        rootLayout = loader.load();
        borderPane = (BorderPane) rootLayout.getChildren().get(0);
        Scene scene = new Scene(rootLayout);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * The settings view is created at launch and hidden from the user
     * immediately by hiding it behind the borderPane.
     * @throws IOException Throw IO Exception if the FXML file cannot be found/loaded.
     */
    private void initSettingsView() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(mainApp.getResourceURL("views/SettingsView.fxml"));
        settingsView = loader.load();
        settingsViewManager = loader.getController();
        settingsViewManager.setRootViewManager(this);
        rootLayout.getChildren().add(settingsView);
        settingsView.toBack();
    }

    //@author A0111987X
    /**
     * Initialises the Help View.
     *
     * @throws IOException Throw IO Exception if the FXML file cannot be found/loaded.
     */
    private void initHelpView() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(mainApp.getResourceURL("views/HelpView.fxml"));
        helpView = loader.load();
        helpViewManager = loader.getController();
        helpViewManager.setRootViewManager(this);
        rootLayout.getChildren().add(helpView);
        helpView.toBack();
    }

    //@author A0111764L
    /**
     * Initializes the title bar.
     * @throws IOException Throw IO Exception if the FXML file cannot be found/loaded.
     */
    private void showTitleBarView() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(mainApp.getResourceURL("views/TitleBarView.fxml"));
        Pane titleBarView = loader.load();
        titleBarView.getStylesheets().add("app/stylesheets/titleBarView.css");
        titleBarViewManager = loader.getController();
        titleBarViewManager.setRootViewManager(this);
        borderPane.setTop(titleBarView);
    }

    /**
     * Initialize the task list view.
     * @throws IOException Throw IO Exception if the FXML file cannot be found/loaded.
     */
    private void showTaskListView() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(mainApp.getResourceURL("views/TaskListView.fxml"));
        ListView taskListView = loader.load();
        taskListView.getStylesheets().add("app/stylesheets/taskList.css");
        taskListView.getStyleClass().add("task-list");
        taskListViewManager = loader.getController();
        taskListViewManager.setRootViewManager(this);
        borderPane.setCenter(taskListView);
    }

    /**
     * Initialize the input field.
     */
    private void showInputField() {
        inputFieldViewManager = new InputFieldViewManager();
        inputFieldViewManager.setRootViewManager(this);
        inputField = inputFieldViewManager.getInputField();
        borderPane.setBottom(new StackPane(inputField));
    }

    /**
     * Initialize the sidebar.
     * @throws IOException Throw IO Exception if the FXML file cannot be found/loaded.
     */
    private void showSidebar() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(mainApp.getResourceURL("views/SidebarView.fxml"));
        VBox sidebar = loader.load();
        sidebar.getStylesheets().add("app/stylesheets/sidebar.css");
        sidebar.getStyleClass().add("sidebar");
        sidebarViewManager = loader.getController();
        sidebarViewManager.setRootViewManager(this);
        borderPane.setLeft(sidebar);
    }

    // Getters and Setters

    /**
     * Called by SettingsView, this saves the user's settings through CommandController.
     * @param filePath The file path string of watdo.json
     * @param enableRandomColors A boolean representing if the user enabled random colors in the task list.
     * @param enableNotifications A boolean representing if the user enabled notifications to show up.
     */
    public void saveSettings(String filePath, Boolean enableRandomColors, Boolean enableNotifications) {
        getMainApp().getCommandController().changeSettings(filePath, enableRandomColors, enableNotifications);
        closeSettings();
    }

    /**
     * Hide the settings view and places focus in the input field.
     */
    public void closeSettings() {
        settingsView.toBack();
        settingsViewManager.cancelFocusOnButton();
        inputField.requestFocus();
    }

    /**
     * Fill the settings view with the user's settings and bring it to th front.
     */
    public void openSettings() {
        settingsViewManager.setAbsolutePathToDirectory(getMainApp().getCommandController().getSaveDirectory());
        settingsViewManager.setRandomColorsEnabled(getMainApp().getCommandController().areRandomColorsEnabled());
        settingsViewManager.setNotificationsEnabled(getMainApp().getCommandController().areNotificationsEnabled());
        settingsView.toFront();
        settingsViewManager.focusOnButton();
    }

    //@author A0111987X
    /**
     * Brings help view to front.
     */
    public void openHelp() {
        helpView.toFront();
        helpViewManager.focusOnButton();
    }

    /**
     * Sends help view to back.
     */
    public void closeHelp() {
        helpView.toBack();
        helpViewManager.cancelFocusOnButton();
        inputField.requestFocus();
    }

    //@author A0111764L
    /**
     * Set back-reference to the Main instance.
     * @param mainApp The Main instance where this RootViewManager instance was created from.
     */
    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }

    /**
     * Get back-reference to the Main instance.
     * @return The Main instance where this RootViewManager instance was created from.
     */
    public Main getMainApp() {
        return mainApp;
    }

    /**
     * @return reference to instantiated TaskListViewManager object.
     */
    public TaskListViewManager getTaskListViewManager() {
        return taskListViewManager;
    }

    /**
     * @return reference to instantiated InputFieldViewManager object.
     */
    public InputFieldViewManager getInputFieldViewManager() {
        return inputFieldViewManager;
    }

    /**
     * @return reference to instantiated TitleBarViewManager object.
     */
    public TitleBarViewManager getTitleBarViewManager() {
        return titleBarViewManager;
    }

    //@author A0111987X
    /**
     * Populates the inputField with a specified string and brings it into focus.
     *
     * @param text String to populate the inputField with.
     */
    public void setAndFocusInputField(String text) {
        if (!inputField.getText().equals(text)) {
            inputFieldViewManager.setFromButton(true);
            inputField.replaceText(text);
            inputFieldViewManager.setFromButton(false);
            inputField.positionCaret(text.length());
            inputField.requestFocus();
        } else {
            inputField.requestFocus();
        }
    }

    //@author A0111764L
    /**
     * Called whenever the list experiences a change of state.
     * This greys out the undo/redo button based on whether there
     * are available undo/redo states.
     */
    public void refreshSidebar() {
        sidebarViewManager.refreshUndoButton();
        sidebarViewManager.refreshRedoButton();
    }
}
