package app.viewmanagers;

import app.helpers.LoggingService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.util.logging.Level;

public class SidebarViewManager {

    @FXML
    private Button displayButton;

    @FXML
    private Button addButton;

    @FXML
    private Button searchButton;

    @FXML
    private Button showDoneButton;

    @FXML
    private Button helpButton;

    @FXML
    private Button settingsButton;

    private RootViewManager rootViewManager;

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        Button[] buttons = {displayButton, addButton, searchButton, helpButton, settingsButton, showDoneButton};
        for (Button button : buttons) {
            button.setOnAction((e) -> clickedButton(button));
        }
    }

    private void clickedButton(Button button) {
        LoggingService.getLogger().log(Level.INFO, "Clicked on: " + button.getId());
        switch (button.getId()) {
            case "displayButton":
                rootViewManager.setAndFocusInputField("display all");
                break;
            case "addButton":
                rootViewManager.setAndFocusInputField("add ");
                break;
            case "searchButton":
                rootViewManager.setAndFocusInputField("search ");
                break;
            case "showDoneButton":
                rootViewManager.setAndFocusInputField("display done");
                break;
            case "helpButton":
                rootViewManager.openHelp();
                break;
            case "settingsButton":
                rootViewManager.openSettings();
                break;
            default:
                break;
        }
    }

    public void setRootViewManager(RootViewManager rootViewManager) {
        this.rootViewManager = rootViewManager;
    }
}
