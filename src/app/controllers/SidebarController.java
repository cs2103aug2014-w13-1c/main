package app.controllers;

import app.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.fxmisc.richtext.InlineCssTextArea;

public class SidebarController {

    @FXML
    private Button displayButton;

    @FXML
    private Button addButton;

    @FXML
    private Button searchButton;

    @FXML
    private Button settingsButton;

    // Reference to the main application
    private Main main;

    public SidebarController() {

    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        displayButton.setOnAction((event) -> {
            setAndFocusInputField("display");
        });

        addButton.setOnAction((event) -> {
            setAndFocusInputField("add ");
        });

        searchButton.setOnAction((event) -> {
            setAndFocusInputField("search ");
        });

        settingsButton.setOnAction((event) -> {
            setAndFocusInputField("settings");
        });

    }

    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param main
     */
    public void setMainApp(Main main) {
        this.main = main;

        // Add observable list data to the table
        // personTable.setItems(mainApp.getPersonData());
    }

    private void setAndFocusInputField(String text) {
        InlineCssTextArea input = main.getInputField();
        input.requestFocus();
        input.replaceText(text);
        input.positionCaret(text.length());
    }
}
