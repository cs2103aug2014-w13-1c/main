package app.controllers;

import app.helpers.LoggingService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;
import java.util.logging.Level;

public class SidebarController {

    @FXML
    private Button displayButton;

    @FXML
    private Button addButton;

    @FXML
    private Button searchButton;

    @FXML
    private Button settingsButton;

    private RootViewController rootViewController;

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        Button[] buttons = {displayButton, addButton, searchButton, settingsButton};
        for (Button button : buttons) {
            button.setOnAction((e) -> clickedButton(button));
        }
    }

    private void clickedButton(Button button) {
        LoggingService.getLogger().log(Level.INFO, "Clicked on: " + button.getId());
        switch (button.getId()) {
            case "displayButton":
                rootViewController.setAndFocusInputField("display");
                break;
            case "addButton":
                rootViewController.setAndFocusInputField("add ");
                break;
            case "searchButton":
                rootViewController.setAndFocusInputField("search ");
                break;
            case "settingsButton":
                rootViewController.openSettings();
                break;
            default:
                break;
        }
    }

    public void setRootViewController(RootViewController rootViewController) {
        this.rootViewController = rootViewController;
    }
}
