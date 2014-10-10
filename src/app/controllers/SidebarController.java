package app.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

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
        displayButton.setOnAction((event) -> rootViewController.setAndFocusInputField("display"));
        addButton.setOnAction((event) -> rootViewController.setAndFocusInputField("add "));
        searchButton.setOnAction((event) -> rootViewController.setAndFocusInputField("search "));
        settingsButton.setOnAction((event) -> rootViewController.openSettings());
    }

    public void setRootViewController(RootViewController rootViewController) {
        this.rootViewController = rootViewController;
    }
}
